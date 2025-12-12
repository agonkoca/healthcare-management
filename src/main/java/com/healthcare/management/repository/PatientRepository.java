package com.healthcare.management.repository;

import com.healthcare.management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByEmailIgnoreCase(String email);
    Optional<Patient> findByPhone(String phone);
    List<Patient> findByNameContainingIgnoreCase(String name);
}
