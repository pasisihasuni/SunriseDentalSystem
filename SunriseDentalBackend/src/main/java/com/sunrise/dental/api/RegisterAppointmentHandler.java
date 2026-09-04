package com.sunrise.dental.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sunrise.dental.model.Appointment;
import com.sunrise.dental.model.Patient;
import com.sunrise.dental.service.AppointmentService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class RegisterAppointmentHandler implements HttpHandler {

    private final AppointmentService appointmentService;

    public RegisterAppointmentHandler() {
        appointmentService = new AppointmentService();
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

        try {

            String requestBody = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Map<String, String> formData =
                    parseFormData(requestBody);

            String appointmentNo =
                    formData.get("appointmentNo");

            String patientName =
                    formData.get("patientName");

            String address =
                    formData.get("address");

            String contactNumber =
                    formData.get("contactNumber");

            String dentistName =
                    formData.get("dentistName");

            String treatmentIdText =
                    formData.get("treatmentId");

            String appointmentDateText =
                    formData.get("appointmentDate");

            String appointmentTimeText =
                    formData.get("appointmentTime");

            Patient patient = new Patient();

            patient.setName(patientName);
            patient.setAddress(address);
            patient.setContactNumber(contactNumber);

            Appointment appointment = new Appointment();

            appointment.setAppointmentNo(appointmentNo);
            appointment.setDentistName(dentistName);

            if (treatmentIdText != null
                    && !treatmentIdText.trim().isEmpty()) {

                appointment.setTreatmentId(
                        Integer.parseInt(treatmentIdText)
                );
            }

            if (appointmentDateText != null
                    && !appointmentDateText.trim().isEmpty()) {

                appointment.setAppointmentDate(
                        LocalDate.parse(appointmentDateText)
                );
            }

            if (appointmentTimeText != null
                    && !appointmentTimeText.trim().isEmpty()) {

                appointment.setAppointmentTime(
                        LocalTime.parse(appointmentTimeText)
                );
            }

            String result =
                    appointmentService.registerAppointment(
                            patient,
                            appointment
                    );

            boolean success =
                    result.equals(
                            "Appointment registered successfully"
                    );

            String response =
                    "{"
                    + "\"success\":" + success + ","
                    + "\"message\":\""
                    + escapeJson(result)
                    + "\""
                    + "}";

            if (success) {

                sendResponse(
                        exchange,
                        200,
                        response
                );

            } else {

                sendResponse(
                        exchange,
                        400,
                        response
                );
            }

        } catch (NumberFormatException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"success\":false,\"message\":\"Invalid treatment type\"}"
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Server error\"}"
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