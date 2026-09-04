package com.sunrise.dental.dao;

import com.sunrise.dental.database.DatabaseConnection;
import com.sunrise.dental.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AppointmentDAO {

    public boolean appointmentExists(String appointmentNo) {

        String sql = """
                SELECT appointment_no
                FROM appointments
                WHERE appointment_no = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, appointmentNo);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean addAppointment(Appointment appointment) {

        String sql = """
                INSERT INTO appointments
                (
                    appointment_no,
                    patient_id,
                    dentist_name,
                    treatment_id,
                    appointment_date,
                    appointment_time,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    appointment.getAppointmentNo()
            );

            statement.setInt(
                    2,
                    appointment.getPatientId()
            );

            statement.setString(
                    3,
                    appointment.getDentistName()
            );

            statement.setInt(
                    4,
                    appointment.getTreatmentId()
            );

            statement.setDate(
                    5,
                    java.sql.Date.valueOf(
                            appointment.getAppointmentDate()
                    )
            );

            statement.setTime(
                    6,
                    java.sql.Time.valueOf(
                            appointment.getAppointmentTime()
                    )
            );

            statement.setString(
                    7,
                    appointment.getStatus()
            );

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}