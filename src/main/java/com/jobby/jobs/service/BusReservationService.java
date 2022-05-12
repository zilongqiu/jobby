package com.jobby.jobs.service;

import com.jobby.jobs.dto.mapper.TicketMapper;
import com.jobby.jobs.dto.mapper.TripMapper;
import com.jobby.jobs.dto.mapper.TripScheduleMapper;
import com.jobby.jobs.dto.model.bus.*;
import com.jobby.jobs.dto.model.user.UserDto;
import com.jobby.jobs.exception.EntityType;
import com.jobby.jobs.exception.ExceptionType;
import com.jobby.jobs.exception.JobbyException;
import com.jobby.jobs.model.bus.*;
import com.jobby.jobs.model.user.User;
import com.jobby.jobs.repository.bus.*;
import com.jobby.jobs.repository.user.UserRepository;
import com.jobby.jobs.service.interfaces.BusReservationInterface;
import com.jobby.jobs.util.RandomStringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.jobby.jobs.exception.EntityType.*;
import static com.jobby.jobs.exception.ExceptionType.*;

@Service
public class BusReservationService implements BusReservationInterface {
    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retruns all the available stops in the database.
     */
    @Override
    public Set<StopDto> getAllStops() {
        return StreamSupport
                .stream(stopRepository.findAll().spliterator(), false)
                .map(stop -> modelMapper.map(stop, StopDto.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Returns the Stop details based on stop code.
     */
    @Override
    public StopDto getStopByCode(String stopCode) {
        Optional<Stop> stop = Optional.ofNullable(stopRepository.findByCode(stopCode));
        if (stop.isPresent()) {
            return modelMapper.map(stop.get(), StopDto.class);
        }
        throw exception(STOP, ENTITY_NOT_FOUND, stopCode);
    }

    /**
     * Fetch AgencyDto from userDto
     */
    @Override
    public AgencyDto getAgency(UserDto userDto) {
        User user = getUser(userDto.getEmail());
        if (user != null) {
            Optional<Agency> agency = Optional.ofNullable(agencyRepository.findByOwner(user));
            if (agency.isPresent()) {
                return modelMapper.map(agency.get(), AgencyDto.class);
            }
            throw exceptionWithId(AGENCY, ENTITY_NOT_FOUND, 2, user.getEmail());
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    /**
     * Register a new agency from the Admin signup flow
     */
    @Override
    public AgencyDto addAgency(AgencyDto agencyDto) {
        User admin = getUser(agencyDto.getOwner().getEmail());
        if (admin != null) {
            Optional<Agency> agency = Optional.ofNullable(agencyRepository.findByName(agencyDto.getName()));
            if (!agency.isPresent()) {
                Agency agencyModel = new Agency()
                        .setName(agencyDto.getName())
                        .setDetails(agencyDto.getDetails())
                        .setCode(RandomStringUtil.getAlphaNumericString(8, agencyDto.getName()))
                        .setOwner(admin);
                agencyRepository.save(agencyModel);
                return modelMapper.map(agencyModel, AgencyDto.class);
            }
            throw exception(AGENCY, DUPLICATE_ENTITY, agencyDto.getName());
        }
        throw exception(USER, ENTITY_NOT_FOUND, agencyDto.getOwner().getEmail());
    }

    /**
     * Updates the agency with given Bus information
     */
    @Transactional
    public AgencyDto updateAgency(AgencyDto agencyDto, BusDto busDto) {
        Agency agency = getAgency(agencyDto.getCode());
        if (agency != null) {
            if (busDto != null) {
                Optional<Bus> bus = Optional.ofNullable(busRepository.findByCodeAndAgency(busDto.getCode(), agency));
                if (!bus.isPresent()) {
                    Bus busModel = new Bus()
                            .setAgency(agency)
                            .setCode(busDto.getCode())
                            .setCapacity(busDto.getCapacity())
                            .setMake(busDto.getMake());
                    busRepository.save(busModel);
                    if (agency.getBuses() == null) {
                        agency.setBuses(new HashSet<>());
                    }
                    agency.getBuses().add(busModel);
                    return modelMapper.map(agencyRepository.save(agency), AgencyDto.class);
                }
                throw exceptionWithId(BUS, DUPLICATE_ENTITY, 2, busDto.getCode(), agencyDto.getCode());
            } else {
                //update agency details case
                agency.setName(agencyDto.getName())
                        .setDetails(agencyDto.getDetails());
                return modelMapper.map(agencyRepository.save(agency), AgencyDto.class);
            }
        }
        throw exceptionWithId(AGENCY, ENTITY_NOT_FOUND, 2, agencyDto.getOwner().getEmail());
    }

    /**
     * Returns trip details based on trip_id
     */
    @Override
    public TripDto getTripById(Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);
        if (trip.isPresent()) {
            return TripMapper.toTripDto(trip.get());
        }
        throw exception(TRIP, ENTITY_NOT_FOUND, tripID.toString());
    }

    /**
     * Creates two new Trips with the given information in tripDto object
     */
    @Override
    @Transactional
    public List<TripDto> addTrip(TripDto tripDto) {
        Stop sourceStop = getStop(tripDto.getSourceStopCode());
        if (sourceStop != null) {
            Stop destinationStop = getStop(tripDto.getDestinationStopCode());
            if (destinationStop != null) {
                if (!sourceStop.getCode().equalsIgnoreCase(destinationStop.getCode())) {
                    Agency agency = getAgency(tripDto.getAgencyCode());
                    if (agency != null) {
                        Bus bus = getBus(tripDto.getBusCode());
                        if (bus != null) {
                            //Each new trip creation results in a to and a fro trip
                            List<TripDto> trips = new ArrayList<>(2);
                            Trip toTrip = new Trip()
                                    .setSourceStop(sourceStop)
                                    .setDestinationStop(destinationStop)
                                    .setAgency(agency)
                                    .setBus(bus)
                                    .setJourneyTime(tripDto.getJourneyTime())
                                    .setFare(tripDto.getFare());
                            trips.add(TripMapper.toTripDto(tripRepository.save(toTrip)));

                            Trip froTrip = new Trip()
                                    .setSourceStop(destinationStop)
                                    .setDestinationStop(sourceStop)
                                    .setAgency(agency)
                                    .setBus(bus)
                                    .setJourneyTime(tripDto.getJourneyTime())
                                    .setFare(tripDto.getFare());
                            trips.add(TripMapper.toTripDto(tripRepository.save(froTrip)));
                            return trips;
                        }
                        throw exception(BUS, ENTITY_NOT_FOUND, tripDto.getBusCode());
                    }
                    throw exception(AGENCY, ENTITY_NOT_FOUND, tripDto.getAgencyCode());
                }
                throw exception(TRIP, ENTITY_EXCEPTION, "");
            }
            throw exception(STOP, ENTITY_NOT_FOUND, tripDto.getDestinationStopCode());
        }
        throw exception(STOP, ENTITY_NOT_FOUND, tripDto.getSourceStopCode());
    }

    /**
     * Fetch all the trips for a given agency
     */
    @Override
    public List<TripDto> getAgencyTrips(String agencyCode) {
        Agency agency = getAgency(agencyCode);
        if (agency != null) {
            List<Trip> agencyTrips = tripRepository.findByAgency(agency);
            if (!agencyTrips.isEmpty()) {
                return agencyTrips
                        .stream()
                        .map(trip -> TripMapper.toTripDto(trip))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        throw exception(AGENCY, ENTITY_NOT_FOUND, agencyCode);
    }

    /**
     * Returns a list of trips between given source and destination stops.
     */
    @Override
    public List<TripDto> getAvailableTripsBetweenStops(String sourceStopCode, String destinationStopCode) {
        List<Trip> availableTrips = findTripsBetweenStops(sourceStopCode, destinationStopCode);
        if (!availableTrips.isEmpty()) {
            return availableTrips
                    .stream()
                    .map(trip -> TripMapper.toTripDto(trip))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Function to locate all the trips between src and dest stops and then
     * filter the results as per the given date based on data present in
     * trip schedule collection.
     */
    @Override
    public List<TripScheduleDto> getAvailableTripSchedules(String sourceStopCode, String destinationStopCode, String tripDate) {
        List<Trip> availableTrips = findTripsBetweenStops(sourceStopCode, destinationStopCode);
        if (!availableTrips.isEmpty()) {
            return availableTrips
                    .stream()
                    .map(trip -> getTripSchedule(TripMapper.toTripDto(trip), tripDate, true))
                    .filter(tripScheduleDto -> tripScheduleDto != null)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Returns TripScheduleDto based on trip details and trip date,
     * optionally creates a schedule if its not found and if the createSchedForTrip
     * parameter is set to true.
     */
    @Override
    public TripScheduleDto getTripSchedule(TripDto tripDto, String tripDate, boolean createSchedForTrip) {
        Optional<Trip> trip = tripRepository.findById(tripDto.getId());
        if (trip.isPresent()) {
            Optional<TripSchedule> tripSchedule = Optional.ofNullable(tripScheduleRepository.findByTripDetailAndTripDate(trip.get(), tripDate));
            if (tripSchedule.isPresent()) {
                return TripScheduleMapper.toTripScheduleDto(tripSchedule.get());
            } else {
                if (createSchedForTrip) { //create the schedule
                    TripSchedule tripSchedule1 = new TripSchedule()
                            .setTripDetail(trip.get())
                            .setTripDate(tripDate)
                            .setAvailableSeats(trip.get().getBus().getCapacity());
                    return TripScheduleMapper.toTripScheduleDto(tripScheduleRepository.save(tripSchedule1));
                } else {
                    throw exceptionWithId(TRIP, ENTITY_NOT_FOUND, 2, tripDto.getId().toString(), tripDate);
                }
            }
        }
        throw exception(TRIP, ENTITY_NOT_FOUND, tripDto.getId().toString());
    }

    /**
     * Method to book ticket for a given trip schedule
     */
    @Override
    @Transactional
    public TicketDto bookTicket(TripScheduleDto tripScheduleDto, UserDto userDto) {
        User user = getUser(userDto.getEmail());
        if (user != null) {
            Optional<TripSchedule> tripSchedule = tripScheduleRepository.findById(tripScheduleDto.getId());
            if (tripSchedule.isPresent()) {
                Ticket ticket = new Ticket()
                        .setCancellable(false)
                        .setJourneyDate(tripSchedule.get().getTripDate())
                        .setPassenger(user)
                        .setTripSchedule(tripSchedule.get())
                        .setSeatNumber(tripSchedule.get().getTripDetail().getBus().getCapacity() - tripSchedule.get().getAvailableSeats());
                ticketRepository.save(ticket);
                tripSchedule.get().setAvailableSeats(tripSchedule.get().getAvailableSeats() - 1); //reduce availability by 1
                tripScheduleRepository.save(tripSchedule.get());//update schedule
                return TicketMapper.toTicketDto(ticket);
            }
            throw exceptionWithId(TRIP, ENTITY_NOT_FOUND, 2, tripScheduleDto.getTripId().toString(), tripScheduleDto.getTripDate());
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    /**
     * Search for all Trips between src and dest stops
     */
    private List<Trip> findTripsBetweenStops(String sourceStopCode, String destinationStopCode) {
        Optional<Stop> sourceStop = Optional
                .ofNullable(stopRepository.findByCode(sourceStopCode));
        if (sourceStop.isPresent()) {
            Optional<Stop> destStop = Optional
                    .ofNullable(stopRepository.findByCode(destinationStopCode));
            if (destStop.isPresent()) {
                List<Trip> availableTrips = tripRepository.findAllBySourceStopAndDestinationStop(sourceStop.get(), destStop.get());
                if (!availableTrips.isEmpty()) {
                    return availableTrips;
                }
                return Collections.emptyList();
            }
            throw exception(STOP, ENTITY_NOT_FOUND, destinationStopCode);
        }
        throw exception(STOP, ENTITY_NOT_FOUND, sourceStopCode);
    }

    /**
     * Fetch user from UserDto
     */
    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Fetch Stop from stopCode
     */
    private Stop getStop(String stopCode) {
        return stopRepository.findByCode(stopCode);
    }

    /**
     * Fetch Bus from busCode, since it is unique we don't have issues of finding duplicate Buses
     */
    private Bus getBus(String busCode) {
        return busRepository.findByCode(busCode);
    }

    /**
     * Fetch Agency from agencyCode
     */
    private Agency getAgency(String agencyCode) {
        return agencyRepository.findByCode(agencyCode);
    }

    /**
     * Returns a new RuntimeException
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return JobbyException.throwException(entityType, exceptionType, args);
    }

    /**
     * Returns a new RuntimeException
     */
    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, Integer id, String... args) {
        return JobbyException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
