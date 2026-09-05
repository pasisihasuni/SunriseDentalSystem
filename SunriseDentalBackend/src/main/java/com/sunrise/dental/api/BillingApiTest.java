package com.sunrise.dental.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BillingApiTest {

    public static void main(String[] args) {

        try {

            URL url = new URL(
                    "http://localhost:8080/api/billing/calculate"
                    + "?appointmentNo=APT001"
            );

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode =
                    connection.getResponseCode();

            Scanner scanner =
                    new Scanner(
                            responseCode >= 200
                                    && responseCode < 300
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
                    "Response Code: " + responseCode
            );

            System.out.println(
                    "Response: " + response
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}