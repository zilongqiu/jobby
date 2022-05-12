package com.jobby.jobs.config;

import com.jobby.jobs.model.bus.*;
import com.jobby.jobs.model.user.Role;
import com.jobby.jobs.model.user.User;
import com.jobby.jobs.model.user.UserRoles;
import com.jobby.jobs.repository.bus.*;
import com.jobby.jobs.repository.user.RoleRepository;
import com.jobby.jobs.repository.user.UserRepository;
import com.jobby.jobs.util.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            RoleRepository roleRepository,
            UserRepository userRepository,
            StopRepository stopRepository,
            AgencyRepository agencyRepository,
            BusRepository busRepository,
            TripRepository tripRepository,
            TripScheduleRepository tripScheduleRepository) {
        return args -> {
            //Create Admin and Passenger Roles
            Role adminRole = roleRepository.findByRole(UserRoles.ADMIN);
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setRole(UserRoles.ADMIN);
                roleRepository.save(adminRole);
            }

            Role userRole = roleRepository.findByRole(UserRoles.PASSENGER);
            if (userRole == null) {
                userRole = new Role();
                userRole.setRole(UserRoles.PASSENGER);
                roleRepository.save(userRole);
            }

            //Create an Admin user
            User admin = userRepository.findByEmail("admin@gmail.com");
            if (admin == null) {
                admin = new User()
                        .setEmail("admin@gmail.com")
                        .setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setMobileNumber("9425094250")
                        .setRoles(List.of(adminRole));
                userRepository.save(admin);
            }

            //Create a passenger user
            User passenger = userRepository.findByEmail("passenger@gmail.com");
            if (passenger == null) {
                passenger = new User()
                        .setEmail("passenger@gmail.com")
                        .setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
                        .setFirstName("Mira")
                        .setLastName("Jane")
                        .setMobileNumber("8000110008")
                        .setRoles(List.of(userRole));
                userRepository.save(passenger);
            }

            //Create four stops
            Stop stopA = stopRepository.findByCode("STPA");
            if (stopA == null) {
                stopA = new Stop()
                        .setName("Stop A")
                        .setDetail("Near hills")
                        .setCode("STPA");
                stopRepository.save(stopA);
            }

            Stop stopB = stopRepository.findByCode("STPB");
            if (stopB == null) {
                stopB = new Stop()
                        .setName("Stop B")
                        .setDetail("Near river")
                        .setCode("STPB");
                stopRepository.save(stopB);
            }

            Stop stopC = stopRepository.findByCode("STPC");
            if (stopC == null) {
                stopC = new Stop()
                        .setName("Stop C")
                        .setDetail("Near desert")
                        .setCode("STPC");
                stopRepository.save(stopC);
            }

            Stop stopD = stopRepository.findByCode("STPD");
            if (stopD == null) {
                stopD = new Stop()
                        .setName("Stop D")
                        .setDetail("Near lake")
                        .setCode("STPD");
                stopRepository.save(stopD);
            }

            //Create an Agency
            Agency agencyA = agencyRepository.findByCode("AGENCY-A");
            if (agencyA == null) {
                agencyA = new Agency()
                        .setName("Green Mile Agency")
                        .setCode("AGENCY-A")
                        .setDetails("Reaching desitnations with ease")
                        .setOwner(admin);
                agencyRepository.save(agencyA);
            }
            //Create a bus
            Bus busA = busRepository.findByCode("AGENCY-A-1");
            if (busA == null) {
                busA = new Bus()
                        .setCode("AGENCY-A-1")
                        .setAgency(agencyA)
                        .setCapacity(60);
                busRepository.save(busA);
            }

            //Add busA to set of buses owned by Agency 'AGENCYA'
            if (agencyA.getBuses() == null) {
                Set<Bus> buses = new HashSet<>();
                agencyA.setBuses(buses);
                agencyA.getBuses().add(busA);
                agencyRepository.save(agencyA);
            }

            //Create a Trip
            Trip trip = tripRepository.findBySourceStopAndDestinationStopAndBus(stopA, stopB, busA);
            if (trip == null) {
                trip = new Trip()
                        .setSourceStop(stopA)
                        .setDestinationStop(stopB)
                        .setBus(busA)
                        .setAgency(agencyA)
                        .setFare(100)
                        .setJourneyTime(60);
                tripRepository.save(trip);
            }

            //Create a trip schedule
            TripSchedule tripSchedule = tripScheduleRepository.findByTripDetailAndTripDate(trip, DateUtils.todayStr());
            if (tripSchedule == null) {
                tripSchedule = new TripSchedule()
                        .setTripDetail(trip)
                        .setTripDate(DateUtils.todayStr())
                        .setAvailableSeats(trip.getBus().getCapacity());
                tripScheduleRepository.save(tripSchedule);
            }
        };
    }


    /*
    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User user = new User(
                    "Zi-long QIU",
                    "zilong.qiu@gmail.com"
            );

            User user2 = new User(
                    "Ding Meng",
                    "ding.meng@gmail.com"
            );

            repository.saveAll(List.of(
                    user,
                    user2
            ));

        };
    }
    */
}
