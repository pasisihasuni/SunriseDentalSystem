package com.sunrise.dental.service;

import com.sunrise.dental.dao.UserDAO;
import com.sunrise.dental.model.User;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public User login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        return userDAO.login(
                username.trim(),
                password
        );
    }
}