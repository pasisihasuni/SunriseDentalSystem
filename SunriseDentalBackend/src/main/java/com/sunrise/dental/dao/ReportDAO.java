package com.sunrise.dental.dao;

import com.sunrise.dental.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    // ------------------------------------
    // REPORT 1 - APPOINTMENTS BY DATE
    // ------------------------------------
    public List<String[]> getAppointmentsByDate(String date) {

        List<String[]> results = new ArrayList<>();

        String sql = """
                SELECT
                    a.appointment_no,
                    p.name AS patient_name,
                    a.dentist_name,
                    t.treatment_type,
                    a.appointment_time,
                    a.status
                FROM appointments a
                INNER JOIN patients p
                    ON a.patient_id = p.patient_id
                INNER JOIN treatments t
                    ON a.treatment_id = t.treatment_id
                WHERE a.appointment_date = ?
                ORDER BY a.appointment_time
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDate(
                    1,
                    java.sql.Date.valueOf(date)
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                String[] row = {
                    resultSet.getString("appointment_no"),
                    resultSet.getString("patient_name"),
                    resultSet.getString("dentist_name"),
                    resultSet.getString("treatment_type"),
                    resultSet.getString("appointment_time"),
                    resultSet.getString("status")
                };

                results.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    // ------------------------------------
    // REPORT 2 - APPOINTMENTS BY TREATMENT
    // ------------------------------------
    public List<String[]> getTreatmentSummary() {

        List<String[]> results = new ArrayList<>();

        String sql = """
                SELECT
                    t.treatment_type,
                    COUNT(a.appointment_no) AS appointment_count
                FROM treatments t
                LEFT JOIN appointments a
                    ON t.treatment_id = a.treatment_id
                GROUP BY
                    t.treatment_id,
                    t.treatment_type
                ORDER BY appointment_count DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                String[] row = {
                    resultSet.getString("treatment_type"),
                    String.valueOf(
                            resultSet.getInt(
                                    "appointment_count"
                            )
                    )
                };

                results.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}