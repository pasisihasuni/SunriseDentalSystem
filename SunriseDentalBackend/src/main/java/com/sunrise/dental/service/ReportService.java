package com.sunrise.dental.service;

import com.sunrise.dental.dao.ReportDAO;

import java.util.List;

public class ReportService {

    private final ReportDAO reportDAO;

    public ReportService() {
        reportDAO = new ReportDAO();
    }

    public List<String[]> getAppointmentsByDate(
            String date) {

        if (date == null
                || date.trim().isEmpty()) {

            return null;
        }

        try {

            java.time.LocalDate.parse(
                    date.trim()
            );

        } catch (Exception e) {

            return null;
        }

        return reportDAO.getAppointmentsByDate(
                date.trim()
        );
    }

    public List<String[]> getTreatmentSummary() {

        return reportDAO.getTreatmentSummary();
    }
}