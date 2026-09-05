package com.sunrise.dental.service;

import com.sunrise.dental.model.Appointment;
import com.sunrise.dental.model.Patient;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class AppointmentServiceTest {

    private Patient createValidPatient() {

        Patient patient = new Patient();

        patient.setName("Test Patient");
        patient.setAddress("Matara");
        patient.setContactNumber("0771234567");

        return patient;
    }

    private Appointment createValidAppointment(String appointmentNo) {

        Appointment appointment = new Appointment();

        appointment.setAppointmentNo(appointmentNo);
        appointment.setDentistName("Dr. Silva");
        appointment.setTreatmentId(1);

        appointment.setAppointmentDate(
                LocalDate.of(2026, 9, 20)
        );

        appointment.setAppointmentTime(
                LocalTime.of(10, 30)
        );

        return appointment;
    }

    @Test
    public void testEmptyAppointmentNumber() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        Appointment appointment =
                createValidAppointment("");

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Appointment number is required",
                actualResult
        );
    }

    @Test
    public void testEmptyPatientName() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        patient.setName("");

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_NAME"
                );

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Patient name is required",
                actualResult
        );
    }

    @Test
    public void testEmptyAddress() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        patient.setAddress("");

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_ADDRESS"
                );

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Patient address is required",
                actualResult
        );
    }

    @Test
    public void testEmptyContactNumber() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        patient.setContactNumber("");

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_CONTACT"
                );

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Contact number is required",
                actualResult
        );
    }

    @Test
    public void testEmptyDentistName() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_DENTIST"
                );

        appointment.setDentistName("");

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Dentist name is required",
                actualResult
        );
    }

    @Test
    public void testInvalidTreatment() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_TREATMENT"
                );

        appointment.setTreatmentId(0);

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Treatment type is required",
                actualResult
        );
    }

    @Test
    public void testMissingDate() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_DATE"
                );

        appointment.setAppointmentDate(null);

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Appointment date is required",
                actualResult
        );
    }

    @Test
    public void testMissingTime() {

        AppointmentService service =
                new AppointmentService();

        Patient patient =
                createValidPatient();

        Appointment appointment =
                createValidAppointment(
                        "TEST_APT_TIME"
                );

        appointment.setAppointmentTime(null);

        String actualResult =
                service.registerAppointment(
                        patient,
                        appointment
                );

        assertEquals(
                "Appointment time is required",
                actualResult
        );
    }
}