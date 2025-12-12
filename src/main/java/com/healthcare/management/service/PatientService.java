package com.healthcare.management.service;

import com.healthcare.management.entity.Patient;
import com.healthcare.management.exception.HealthcareException;
import com.healthcare.management.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient register(String name, String phone, String email, int age) {
        validateRequiredFields(name, phone, email, age);

        String trimmedName = name.trim();
        String trimmedPhone = phone.trim();
        String trimmedEmail = email.trim();

        ensureUniqueContact(null, trimmedEmail, trimmedPhone);

        Patient patient = new Patient(trimmedName, trimmedPhone, trimmedEmail, age);
        return patientRepository.save(patient);
    }

    public Patient update(Integer patientId, String name, String phone, String email, Integer age) {
        Patient patient = getById(patientId);

        if (name != null && !name.isBlank()) {
            patient.setName(name.trim());
        }
        if (phone != null && !phone.isBlank()) {
            patient.setPhone(phone.trim());
        }
        if (email != null && !email.isBlank()) {
            patient.setEmail(email.trim());
        }
        if (age != null) {
            validateAge(age);
            patient.setAge(age);
        }

        ensureUniqueContact(patient.getId(), patient.getEmail(), patient.getPhone());
        return patientRepository.save(patient);
    }

    public void delete(Integer patientId) {
        Patient patient = getById(patientId);
        patientRepository.delete(patient);
    }

    public Patient getById(Integer patientId) {
        if (patientId == null) {
            throw new HealthcareException("Patient ID is required.");
        }

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new HealthcareException("Patient with ID: " + patientId + " was not found."));
    }

    public Patient getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new HealthcareException("Email is required.");
        }

        String trimmedEmail = email.trim();
        return patientRepository.findByEmailIgnoreCase(trimmedEmail)
                .orElseThrow(() ->
                        new HealthcareException("Patient with email: " + trimmedEmail + " was not found."));
    }

    public List<Patient> searchByName(String name) {
        if (name == null || name.isBlank()) {
            throw new HealthcareException("Name is required for search.");
        }

        return patientRepository.findByNameContainingIgnoreCase(name.trim());
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    private void validateRequiredFields(String name, String phone, String email, Integer age) {
        if (name == null || name.isBlank()) {
            throw new HealthcareException("Name is required.");
        }
        if (phone == null || phone.isBlank()) {
            throw new HealthcareException("Phone is required.");
        }
        if (email == null || email.isBlank()) {
            throw new HealthcareException("Email is required.");
        }
        if (age == null) {
            throw new HealthcareException("Age is required.");
        }

        validateAge(age);
    }

    private void validateAge(int age) {
        if (age <= 0) {
            throw new HealthcareException("Age must be greater than zero.");
        }
    }

    private void ensureUniqueContact(Integer currentId, String email, String phone) {
        if (email != null) {
            patientRepository.findByEmailIgnoreCase(email)
                    .filter(existing -> !existing.getId().equals(currentId))
                    .ifPresent(existing -> {
                        throw new HealthcareException("A patient with this email already exists.");
                    });
        }

        if (phone != null) {
            patientRepository.findByPhone(phone)
                    .filter(existing -> !existing.getId().equals(currentId))
                    .ifPresent(existing -> {
                        throw new HealthcareException("A patient with this phone already exists.");
                    });
        }
    }
}
