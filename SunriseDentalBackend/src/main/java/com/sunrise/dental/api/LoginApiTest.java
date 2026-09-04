package com.sunrise.dental.api;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LoginApiTest {

    public static void main(String[] args) {

        try {

            URL url = new URL(
                    "http://localhost:8080/api/login"
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
                    "username=admin&password=admin123";

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