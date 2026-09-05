package com.sunrise.dental.api;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class BackendServer {

    public static void main(String[] args) {

        try {

            HttpServer server =
                    HttpServer.create(
                            new InetSocketAddress(8080),
                            0
                    );

            server.createContext(
                    "/api/login",
                    new LoginHandler()
            );
            server.createContext(
        "/api/appointments/register",
        new RegisterAppointmentHandler()
);
            server.createContext(
        "/api/appointments/search",
        new SearchAppointmentHandler()
);

            server.setExecutor(null);

            server.start();

            System.out.println(
                    "Sunrise Dental Web Service started successfully."
            );

            System.out.println(
                    "Server URL: http://localhost:8080"
            );

            System.out.println(
                    "Login API: http://localhost:8080/api/login"
            );

        } catch (Exception e) {

            System.out.println(
                    "Failed to start web service."
            );

            e.printStackTrace();
        }
    }
}