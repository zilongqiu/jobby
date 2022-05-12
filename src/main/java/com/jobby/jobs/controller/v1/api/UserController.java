package com.jobby.jobs.controller.v1.api;

import com.jobby.jobs.controller.v1.request.UserSignupRequest;
import com.jobby.jobs.dto.model.user.UserDto;
import com.jobby.jobs.dto.response.Response;
import com.jobby.jobs.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@Slf4j
@Api(value = "jobby-application", description = "Operations pertaining to user management in the Jobby application")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles the incoming POST API "/v1/user/signup"
     */
    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        return Response.ok().setPayload(registerUser(userSignupRequest, false));
    }

    /**
     * Register a new user in the database
     */
    private UserDto registerUser(UserSignupRequest userSignupRequest, boolean isAdmin) {
        UserDto userDto = new UserDto()
                .setEmail(userSignupRequest.getEmail())
                .setPassword(userSignupRequest.getPassword())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setMobileNumber(userSignupRequest.getMobileNumber())
                .setAdmin(isAdmin);

        return userService.signup(userDto);
    }

    /*
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Getting users");
        return userService.getUsers();
    }

    @PostMapping
    public void postUser(@RequestBody User user) {
        log.info("Create new user");
        userService.createUser(user);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Delete user");
        userService.deleteUser(userId);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ) {
        log.info("Update user");
        userService.updateUser(userId, name, email);
    }
    */
}
