package com.sunrise.dental.service;

import com.sunrise.dental.dao.BillingDAO;
import com.sunrise.dental.model.Bill;

import java.time.LocalDate;
import java.util.Map;

public class BillingService {

    private final BillingDAO billingDAO;

    public BillingService() {
        billingDAO = new BillingDAO();
    }

    public Bill calculateBill(String appointmentNo) {

        if (appointmentNo == null
                || appointmentNo.trim().isEmpty()) {

            return null;
        }

        Map<String, String> data =
                billingDAO.getBillingDetails(
                        appointmentNo.trim()
                );

        if (data == null) {
            return null;
        }

        double treatmentCost =
                Double.parseDouble(
                        data.get("treatmentCost")
                );

        double consultationFee =
                Double.parseDouble(
                        data.get("consultationFee")
                );

        double totalAmount =
                treatmentCost + consultationFee;

        Bill bill = new Bill();

        bill.setAppointmentNo(
                appointmentNo.trim()
        );

        bill.setTreatmentCost(
                treatmentCost
        );

        bill.setConsultationFee(
                consultationFee
        );

        bill.setTotalAmount(
                totalAmount
        );

        bill.setBillDate(
                LocalDate.now()
        );

        return bill;
    }

    public boolean saveBill(Bill bill) {

        return billingDAO.saveBill(bill);
    }

    public Map<String, String> getBillingDetails(
            String appointmentNo) {

        return billingDAO.getBillingDetails(
                appointmentNo
        );
    }
}