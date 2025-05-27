package com.emsi.cc_spring_boot.service;

import com.emsi.cc_spring_boot.entity.User;

import java.util.List;

public interface UserService {
    User authenticate(String username, String password);
    boolean registerUser(String username, String password);
    boolean usernameExists(String username);
    List<User> getAllUsers();
    User findByUsername(String username);
}