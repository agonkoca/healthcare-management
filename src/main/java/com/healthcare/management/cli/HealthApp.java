package com.healthcare.management.cli;

import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.AppointmentStatus;
import com.healthcare.management.entity.Doctor;
import com.healthcare.management.entity.Patient;
import com.healthcare.management.exception.HealthcareException;
import com.healthcare.management.notification.EmailService;
import com.healthcare.management.notification.NotificationService;
import com.healthcare.management.notification.SMSService;
import com.healthcare.management.repository.AppointmentRepository;
import com.healthcare.management.repository.DoctorRepository;
import com.healthcare.management.repository.PatientRepository;
import com.healthcare.management.service.AppointmentService;
import com.healthcare.management.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class HealthApp {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;
    private final SMSService smsService;

    public HealthApp(PatientService patientService,
                     AppointmentService appointmentService,
                     PatientRepository patientRepository,
                     DoctorRepository doctorRepository,
                     AppointmentRepository appointmentRepository,
                     EmailService emailService,
                     SMSService smsService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    // Patient operations
    public Patient registerPatient(String name, String phone, String email, int age) {
        return patientService.register(name, phone, email, age);
    }

    public Patient updatePatient(Integer patientId, String name, String phone, String email, Integer age) {
        return patientService.update(patientId, name, phone, email, age);
    }

    public void deletePatient(Integer patientId) {
        patientService.delete(patientId);
    }

    public Patient getPatientById(Integer id) {
        return patientService.getById(id);
    }

    public Patient getPatientByEmail(String email) {
        return patientService.getByEmail(email);
    }

    public List<Patient> searchPatientsByName(String name) {
        return patientService.searchByName(name);
    }

    public List<Patient> getAllPatients() {
        return patientService.getAll();
    }

    // Doctor operations
    public Doctor createDoctor(String name, String phone, String email, String speciality) {
        Doctor doctor = new Doctor(name, phone, email, speciality);
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> getDoctorById(Integer id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Appointment operations
    public Appointment scheduleAppointment(Integer patientId, Integer doctorId, LocalDate date) {
        return appointmentService.schedule(patientId, doctorId, date);
    }

    public Appointment completeAppointment(Integer appointmentId, String report) {
        return appointmentService.complete(appointmentId, report);
    }

    public Appointment cancelAppointment(Integer appointmentId) {
        return appointmentService.cancel(appointmentId);
    }

    public List<Appointment> listAppointmentsByPatient(Integer patientId) {
        return appointmentService.getByPatient(patientId);
    }

    public List<Appointment> listAppointmentsByStatus(AppointmentStatus status) {
        return appointmentService.getByStatus(status);
    }

    public Appointment getAppointment(Integer appointmentId) {
        return appointmentService.getById(appointmentId);
    }

    // Notifications - choose Email or SMS implementation
    public void notifyPatientByEmail(Integer patientId, String message) {
        Patient p = patientService.getById(patientId);
        emailService.setRecipientEmail(p.getEmail());
        emailService.sendNotification(message);
    }

    public void notifyPatientBySMS(Integer patientId, String message) {
        Patient p = patientService.getById(patientId);
        smsService.setRecipientPhone(p.getPhone());
        smsService.sendNotification(message);
    }

    // Utility helpers
    public long countPatients() {
        return patientRepository.count();
    }

    public long countDoctors() {
        return doctorRepository.count();
    }

    public long countAppointments() {
        return appointmentRepository.count();
    }
}

