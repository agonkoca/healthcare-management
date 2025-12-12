package com.healthcare.management.config;

import com.healthcare.management.cli.HealthApp;
import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.AppointmentStatus;
import com.healthcare.management.entity.Doctor;
import com.healthcare.management.entity.Patient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final HealthApp app;

    public DataInitializer(HealthApp app) {
        this.app = app;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // Only initialize if repository is empty
        if (app.countPatients() == 0 && app.countDoctors() == 0) {
            Patient p1 = app.registerPatient("Arbnor H", "+38344123456", "arbnor@example.com", 30);
            Patient p2 = app.registerPatient("Luljeta K", "+38344123457", "luljeta@example.com", 28);

            Doctor d1 = app.createDoctor("Dr. Bujar", "+38344110000", "bujar@clinic.com", "Internal Medicine");
            Doctor d2 = app.createDoctor("Dr. Ema", "+38344110001", "ema@clinic.com", "Pediatrics");

            Appointment a1 = app.scheduleAppointment(p1.getId(), d1.getId(), LocalDate.now().plusDays(3));
            Appointment a2 = app.scheduleAppointment(p2.getId(), d2.getId(), LocalDate.now().plusDays(7));

            // send notifications
            app.notifyPatientByEmail(p1.getId(), "Termini u krijua: " + a1.getDate());
            app.notifyPatientBySMS(p2.getId(), "Termini juaj eshte ne daten: " + a2.getDate());

            System.out.println("[DataInitializer] Sample data created: patients=" + app.countPatients() + ", doctors=" + app.countDoctors() + ", appointments=" + app.countAppointments());
        }
    }
}