package com.sunrise.dental.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sunrise.dental.service.ReportService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AppointmentsByDateHandler
        implements HttpHandler {

    private final ReportService reportService;

    public AppointmentsByDateHandler() {
        reportService = new ReportService();
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "METHOD_NOT_ALLOWED"
            );

            return;
        }

        String query =
                exchange.getRequestURI().getQuery();

        String date = null;

        if (query != null) {

            String[] pairs =
                    query.split("&");

            for (String pair : pairs) {

                String[] keyValue =
                        pair.split("=", 2);

                if (keyValue.length == 2
                        && keyValue[0].equals("date")) {

                    date = URLDecoder.decode(
                            keyValue[1],
                            StandardCharsets.UTF_8
                    );
                }
            }
        }

        List<String[]> results =
                reportService
                        .getAppointmentsByDate(date);

        if (results == null) {

            sendResponse(
                    exchange,
                    400,
                    "INVALID_DATE"
            );

            return;
        }

        StringBuilder response =
                new StringBuilder();

        for (String[] row : results) {

            response
                    .append(row[0]).append("|")
                    .append(row[1]).append("|")
                    .append(row[2]).append("|")
                    .append(row[3]).append("|")
                    .append(row[4]).append("|")
                    .append(row[5]).append("\n");
        }

        sendResponse(
                exchange,
                200,
                response.toString()
        );
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain"
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
}