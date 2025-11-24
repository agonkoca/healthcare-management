package com.healthcare.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor extends Person {
    private String speciality;
    public Doctor() {}
    public Doctor(String name, String phone, String email, String speciality)
    {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String getDetails() {
        return "Doktori: " + name +
                ", Specializimi: " + speciality +
                ", Tel: " + phone +
                ", Email: " + email;
    }
}
