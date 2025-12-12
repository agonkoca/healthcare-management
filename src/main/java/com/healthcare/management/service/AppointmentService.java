package com.healthcare.management.service;

import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.AppointmentStatus;
import com.healthcare.management.entity.Doctor;
import com.healthcare.management.entity.Patient;
import com.healthcare.management.exception.HealthcareException;
import com.healthcare.management.repository.AppointmentRepository;
import com.healthcare.management.repository.DoctorRepository;
import com.healthcare.management.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment schedule(Integer patientId, Integer doctorId, LocalDate date) {
        if (patientId == null || doctorId == null || date == null) {
            throw new HealthcareException("Patient, doctor dhe date janë të detyrueshme.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new HealthcareException("Pacienti me ID: " + patientId + " nuk u gjet."));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new HealthcareException("Doktori me ID: " + doctorId + " nuk u gjet."));

        Appointment appointment = new Appointment(patient, doctor, date, AppointmentStatus.SCHEDULED);
        appointment.setReport(null);

        return appointmentRepository.save(appointment);
    }

    public Appointment complete(Integer appointmentId, String report) {

        if (appointmentId == null) {
            throw new HealthcareException("Appointment ID është i detyrueshëm.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new HealthcareException("Appointment me ID: " + appointmentId + " nuk u gjet."));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new HealthcareException("Termini i anuluar nuk mund të përfundohet.");
        }

        if (report == null || report.isBlank()) {
            throw new HealthcareException("Raporti është i detyrueshëm për përfundimin e terminit.");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setReport(report.trim());

        return appointmentRepository.save(appointment);
    }

    public Appointment cancel(Integer appointmentId) {

        if (appointmentId == null) {
            throw new HealthcareException("Appointment ID është i detyrueshëm.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new HealthcareException("Appointment me ID: " + appointmentId + " nuk u gjet."));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new HealthcareException("Termini i përfunduar nuk mund të anulohet.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setReport(null);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getByPatient(Integer patientId) {
        if (patientId == null) {
            throw new HealthcareException("Patient ID është i detyrueshëm.");
        }
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getByStatus(AppointmentStatus status) {
        if (status == null) {
            throw new HealthcareException("Status është i detyrueshëm.");
        }
        return appointmentRepository.findByStatus(status);
    }

    public Appointment getById(Integer appointmentId) {
        if (appointmentId == null) {
            throw new HealthcareException("Appointment ID është i detyrueshëm.");
        }
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new HealthcareException("Appointment me ID: " + appointmentId + " nuk u gjet."));
    }



}
