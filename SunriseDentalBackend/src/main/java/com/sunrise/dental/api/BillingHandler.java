package com.sunrise.dental.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sunrise.dental.model.Bill;
import com.sunrise.dental.service.BillingService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BillingHandler implements HttpHandler {

    private final BillingService billingService;

    public BillingHandler() {
        billingService = new BillingService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method Not Allowed\"}"
            );

            return;
        }

        String query = exchange.getRequestURI().getQuery();

        String appointmentNo = null;

        if (query != null) {

            String[] pairs = query.split("&");

            for (String pair : pairs) {

                String[] keyValue =
                        pair.split("=", 2);

                if (keyValue.length == 2
                        && keyValue[0].equals("appointmentNo")) {

                    appointmentNo =
                            URLDecoder.decode(
                                    keyValue[1],
                                    StandardCharsets.UTF_8
                            );
                }
            }
        }

        if (appointmentNo == null
                || appointmentNo.trim().isEmpty()) {

            sendResponse(
                    exchange,
                    400,
                    "{\"success\":false,\"message\":\"Appointment number is required\"}"
            );

            return;
        }

        Map<String, String> details =
                billingService.getBillingDetails(
                        appointmentNo
                );

        Bill bill =
                billingService.calculateBill(
                        appointmentNo
                );

        if (details == null || bill == null) {

            sendResponse(
                    exchange,
                    404,
                    "{\"success\":false,\"message\":\"Appointment not found\"}"
            );

            return;
        }

        String response =
                "{"
                + "\"success\":true,"
                + "\"appointmentNo\":\""
                + escapeJson(details.get("appointmentNo")) + "\","
                + "\"patientName\":\""
                + escapeJson(details.get("patientName")) + "\","
                + "\"treatmentType\":\""
                + escapeJson(details.get("treatmentType")) + "\","
                + "\"treatmentCost\":\""
                + bill.getTreatmentCost() + "\","
                + "\"consultationFee\":\""
                + bill.getConsultationFee() + "\","
                + "\"totalAmount\":\""
                + bill.getTotalAmount() + "\","
                + "\"billDate\":\""
                + bill.getBillDate() + "\""
                + "}";

        sendResponse(
                exchange,
                200,
                response
        );
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