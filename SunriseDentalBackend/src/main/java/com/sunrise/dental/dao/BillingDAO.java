package com.sunrise.dental.dao;

import com.sunrise.dental.database.DatabaseConnection;
import com.sunrise.dental.model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BillingDAO {

    public java.util.Map<String, String> getBillingDetails(
            String appointmentNo) {

        String sql = """
                SELECT
                    a.appointment_no,
                    p.name AS patient_name,
                    t.treatment_type,
                    t.treatment_cost,
                    sf.fee_amount AS consultation_fee
                FROM appointments a
                INNER JOIN patients p
                    ON a.patient_id = p.patient_id
                INNER JOIN treatments t
                    ON a.treatment_id = t.treatment_id
                CROSS JOIN system_fees sf
                WHERE a.appointment_no = ?
                  AND sf.fee_name = 'Consultation Fee'
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, appointmentNo);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                java.util.Map<String, String> data =
                        new java.util.HashMap<>();

                data.put(
                        "appointmentNo",
                        resultSet.getString("appointment_no")
                );

                data.put(
                        "patientName",
                        resultSet.getString("patient_name")
                );

                data.put(
                        "treatmentType",
                        resultSet.getString("treatment_type")
                );

                data.put(
                        "treatmentCost",
                        resultSet.getString("treatment_cost")
                );

                data.put(
                        "consultationFee",
                        resultSet.getString("consultation_fee")
                );

                return data;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean saveBill(Bill bill) {

        String sql = """
                INSERT INTO bills
                (
                    appointment_no,
                    treatment_cost,
                    consultation_fee,
                    total_amount,
                    bill_date
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    bill.getAppointmentNo()
            );

            statement.setDouble(
                    2,
                    bill.getTreatmentCost()
            );

            statement.setDouble(
                    3,
                    bill.getConsultationFee()
            );

            statement.setDouble(
                    4,
                    bill.getTotalAmount()
            );

            statement.setDate(
                    5,
                    java.sql.Date.valueOf(
                            bill.getBillDate()
                    )
            );

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}