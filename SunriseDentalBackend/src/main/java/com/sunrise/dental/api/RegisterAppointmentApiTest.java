package com.sunrise.dental.api;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RegisterAppointmentApiTest {

    public static void main(String[] args) {

        try {

            URL url = new URL(
                    "http://localhost:8080/api/appointments/register"
            );

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded"
            );

            String data =
                    "appointmentNo=APT001"
                    + "&patientName=Kamal%20Perera"
                    + "&address=Matara"
                    + "&contactNumber=0771234567"
                    + "&dentistName=Dr%20Silva"
                    + "&treatmentId=2"
                    + "&appointmentDate=2026-09-10"
                    + "&appointmentTime=10:30";

            try (OutputStream os =
                         connection.getOutputStream()) {

                os.write(
                        data.getBytes(StandardCharsets.UTF_8)
                );
            }

            int responseCode =
                    connection.getResponseCode();

            System.out.println(
                    "Response Code: " + responseCode
            );

            Scanner scanner =
                    new Scanner(
                            responseCode >= 200 && responseCode < 300
                                    ? connection.getInputStream()
                                    : connection.getErrorStream()
                    );

            StringBuilder response =
                    new StringBuilder();

            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }

            scanner.close();

            System.out.println(
                    "Response: " + response
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}