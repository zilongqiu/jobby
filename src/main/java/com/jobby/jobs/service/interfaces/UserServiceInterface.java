package com.jobby.jobs.service.interfaces;

import com.jobby.jobs.dto.model.user.UserDto;

public interface UserServiceInterface {
    /**
     * Register a new user
     */
    UserDto signup(UserDto userDto);

    /**
     * Search an existing user
     */
    UserDto findUserByEmail(String email);

    /**
     * Update profile of the user
     */
    UserDto updateProfile(UserDto userDto);

    /**
     * Update password
     */
    UserDto changePassword(UserDto userDto, String newPassword);
}
