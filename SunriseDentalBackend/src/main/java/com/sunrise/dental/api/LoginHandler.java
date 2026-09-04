package com.sunrise.dental.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sunrise.dental.model.User;
import com.sunrise.dental.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {

    private final UserService userService;

    public LoginHandler() {
        userService = new UserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method Not Allowed\"}"
            );

            return;
        }

        String requestBody = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        Map<String, String> formData =
                parseFormData(requestBody);

        String username =
                formData.get("username");

        String password =
                formData.get("password");

        if (username == null
                || username.trim().isEmpty()
                || password == null
                || password.trim().isEmpty()) {

            sendResponse(
                    exchange,
                    400,
                    "{\"success\":false,\"message\":\"Username and password are required\"}"
            );

            return;
        }

        User user =
                userService.login(username, password);

        if (user != null) {

            String response =
                    "{"
                    + "\"success\":true,"
                    + "\"message\":\"Login successful\","
                    + "\"username\":\"" + escapeJson(user.getUsername()) + "\","
                    + "\"role\":\"" + escapeJson(user.getRole()) + "\""
                    + "}";

            sendResponse(
                    exchange,
                    200,
                    response
            );

        } else {

            sendResponse(
                    exchange,
                    401,
                    "{\"success\":false,\"message\":\"Invalid username or password\"}"
            );
        }
    }

    private Map<String, String> parseFormData(String data) {

        Map<String, String> values =
                new HashMap<>();

        if (data == null || data.isBlank()) {
            return values;
        }

        String[] pairs = data.split("&");

        for (String pair : pairs) {

            String[] keyValue =
                    pair.split("=", 2);

            if (keyValue.length == 2) {

                String key = URLDecoder.decode(
                        keyValue[0],
                        StandardCharsets.UTF_8
                );

                String value = URLDecoder.decode(
                        keyValue[1],
                        StandardCharsets.UTF_8
                );

                values.put(key, value);
            }
        }

        return values;
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (OutputStream os =
                     exchange.getResponseBody()) {

            os.write(bytes);
        }
    }

    private String escapeJson(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}