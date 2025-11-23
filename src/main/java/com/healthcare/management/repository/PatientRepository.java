package com.healthcare.management.repository;

import com.healthcare.management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<gitPatient, Integer> {
}
