package com.sunrise.dental.service;

import com.sunrise.dental.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {

    @Test
    public void testValidLogin() {

        UserService userService = new UserService();

        User actualUser = userService.login(
                "admin",
                "admin123"
        );

        assertNotNull(actualUser);
        assertEquals("admin", actualUser.getUsername());
    }

    @Test
    public void testInvalidPassword() {

        UserService userService = new UserService();

        User actualUser = userService.login(
                "admin",
                "wrong123"
        );

        assertNull(actualUser);
    }

    @Test
    public void testEmptyUsername() {

        UserService userService = new UserService();

        User actualUser = userService.login(
                "",
                "admin123"
        );

        assertNull(actualUser);
    }

    @Test
    public void testEmptyPassword() {

        UserService userService = new UserService();

        User actualUser = userService.login(
                "admin",
                ""
        );

        assertNull(actualUser);
    }

    @Test
    public void testUnknownUsername() {

        UserService userService = new UserService();

        User actualUser = userService.login(
                "unknownuser",
                "admin123"
        );

        assertNull(actualUser);
    }
}