package com.sunrise.dental.service;

import com.sunrise.dental.model.Bill;
import org.junit.Test;

import static org.junit.Assert.*;

public class BillingServiceTest {

    @Test
    public void testValidBillCalculation() {

        BillingService billingService =
                new BillingService();

        Bill bill =
                billingService.calculateBill(
                        "APT001"
                );

        assertNotNull(bill);

        double expectedTotal =
                bill.getTreatmentCost()
                + bill.getConsultationFee();

        assertEquals(
                expectedTotal,
                bill.getTotalAmount(),
                0.001
        );
    }

    @Test
    public void testUnknownAppointment() {

        BillingService billingService =
                new BillingService();

        Bill bill =
                billingService.calculateBill(
                        "APT999"
                );

        assertNull(bill);
    }

    @Test
    public void testEmptyAppointmentNumber() {

        BillingService billingService =
                new BillingService();

        Bill bill =
                billingService.calculateBill(
                        ""
                );

        assertNull(bill);
    }
}