package com.jobby.jobs.controller;

import com.jobby.jobs.model.User;
import com.jobby.jobs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@Slf4j
public class UserController {

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
}
