package com.example.usermicroservice.service;

import com.example.usermicroservice.entity.User;
import com.example.usermicroservice.exception.UserAlreadyPresent;
import com.example.usermicroservice.exception.UserNotFound;

public interface UserService {

    public User addUser(User user) throws UserAlreadyPresent;

    public User updateUser(User user) throws UserNotFound;

    public User getUser(int number) throws UserNotFound;

    public String deleteUser(int registrationNumber) throws UserNotFound;
}
