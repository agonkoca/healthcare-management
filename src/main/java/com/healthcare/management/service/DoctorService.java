package com.healthcare.management.service;

import com.healthcare.management.entity.Doctor;
import com.healthcare.management.exception.HealthcareException;
import com.healthcare.management.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor create(String name, String phone, String email, String speciality){
        String trimmedName = name.trim();
        String trimmedPhone = phone.trim();
        String trimmedEmail = email.trim();
        String trimmedSpeciality = speciality.trim();

        Doctor doctor = new Doctor(trimmedName, trimmedPhone, trimmedEmail, trimmedSpeciality);
        return doctorRepository.save(doctor);

    }

    public Doctor update(Integer doctorId, String name, String phone, String email, String speciality){
        Doctor doctor = getById(doctorId);

        if(name != null && !name.isBlank()){
            doctor.setName(name.trim());
        }
        if(phone != null && !phone.isBlank()){
            doctor.setPhone(phone.trim());
        }
        if(email != null && !email.isBlank()){
            doctor.setEmail(email.trim());
        }
        if(speciality != null && !speciality.isBlank()){
            doctor.setSpeciality(speciality.trim());
        }

        return doctorRepository.save(doctor);
    }

    public void delete(Integer doctorId){
        Doctor doctor = getById(doctorId);
        doctorRepository.delete(doctor);
    }

    public Doctor getById(Integer doctorId){
        if(doctorId == null){
            throw new HealthcareException("DoctorId is required");
        }
        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new HealthcareException("Doctor with ID: " + doctorId + " was not found."));
    }

    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public List<Doctor> getBySpecialty(String specialty) {
        if (specialty == null || specialty.isBlank()) {
            throw new HealthcareException("Specialty is required for search.");
        }

        return doctorRepository.findAll().stream()
                .filter(d -> d.getSpeciality() != null &&
                        d.getSpeciality().toLowerCase().contains(specialty.toLowerCase().trim()))
                .toList();
    }

    private void validateRequiredFields(String name, String phone, String email, String specialty) {
        if (name == null || name.isBlank()) {
            throw new HealthcareException("Name is required.");
        }
        if (phone == null || phone.isBlank()) {
            throw new HealthcareException("Phone is required.");
        }
        if (email == null || email.isBlank()) {
            throw new HealthcareException("Email is required.");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new HealthcareException("Specialty is required.");
        }
    }
}
