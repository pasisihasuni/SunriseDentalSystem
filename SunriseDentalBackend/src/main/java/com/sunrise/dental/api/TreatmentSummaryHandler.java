package com.sunrise.dental.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sunrise.dental.service.ReportService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TreatmentSummaryHandler
        implements HttpHandler {

    private final ReportService reportService;

    public TreatmentSummaryHandler() {
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

        List<String[]> results =
                reportService.getTreatmentSummary();

        StringBuilder response =
                new StringBuilder();

        for (String[] row : results) {

            response
                    .append(row[0])
                    .append("|")
                    .append(row[1])
                    .append("\n");
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