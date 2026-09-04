package com.sunrise.dental.dao;

import com.sunrise.dental.database.DatabaseConnection;
import com.sunrise.dental.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientDAO {

    public int addPatient(Patient patient) {

        String sql = """
                INSERT INTO patients (name, address, contact_number)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            statement.setString(1, patient.getName());
            statement.setString(2, patient.getAddress());
            statement.setString(3, patient.getContactNumber());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                ResultSet keys = statement.getGeneratedKeys();

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}