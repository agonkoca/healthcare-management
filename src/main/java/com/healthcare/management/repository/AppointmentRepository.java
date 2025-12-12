package com.healthcare.management.repository;
import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    List<Appointment> findByPatientId(Integer patientId);
    List<Appointment> findByStatus(AppointmentStatus status);
}
