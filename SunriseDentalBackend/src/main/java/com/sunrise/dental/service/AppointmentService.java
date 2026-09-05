package com.sunrise.dental.service;

import com.sunrise.dental.dao.AppointmentDAO;
import com.sunrise.dental.dao.PatientDAO;
import com.sunrise.dental.model.Appointment;
import com.sunrise.dental.model.Patient;

public class AppointmentService {

    private final PatientDAO patientDAO;
    private final AppointmentDAO appointmentDAO;
    
    public java.util.Map<String, String> searchAppointment(
        String appointmentNo) {

    if (appointmentNo == null
            || appointmentNo.trim().isEmpty()) {

        return null;
    }

    return appointmentDAO.searchAppointment(
            appointmentNo.trim()
    );
}

    public AppointmentService() {
        patientDAO = new PatientDAO();
        appointmentDAO = new AppointmentDAO();
    }

    public String registerAppointment(
            Patient patient,
            Appointment appointment) {

        // Check appointment number
        if (appointment.getAppointmentNo() == null
                || appointment.getAppointmentNo().trim().isEmpty()) {

            return "Appointment number is required";
        }

        // Check duplicate appointment number
        if (appointmentDAO.appointmentExists(
                appointment.getAppointmentNo())) {

            return "Appointment number already exists";
        }

        // Check patient name
        if (patient.getName() == null
                || patient.getName().trim().isEmpty()) {

            return "Patient name is required";
        }

        // Check address
        if (patient.getAddress() == null
                || patient.getAddress().trim().isEmpty()) {

            return "Patient address is required";
        }

        // Check contact number
        if (patient.getContactNumber() == null
                || patient.getContactNumber().trim().isEmpty()) {

            return "Contact number is required";
        }

        // Check dentist
        if (appointment.getDentistName() == null
                || appointment.getDentistName().trim().isEmpty()) {

            return "Dentist name is required";
        }

        // Check treatment
        if (appointment.getTreatmentId() <= 0) {

            return "Treatment type is required";
        }

        // Check date
        if (appointment.getAppointmentDate() == null) {

            return "Appointment date is required";
        }

        // Check time
        if (appointment.getAppointmentTime() == null) {

            return "Appointment time is required";
        }

        // Save patient first
        int patientId = patientDAO.addPatient(patient);

        if (patientId == -1) {
            return "Failed to save patient";
        }

        // Connect patient to appointment
        appointment.setPatientId(patientId);

        // New appointment status
        appointment.setStatus("Scheduled");

        // Save appointment
        boolean saved =
                appointmentDAO.addAppointment(appointment);

        if (saved) {
            return "Appointment registered successfully";
        }

        return "Failed to register appointment";
    }
}