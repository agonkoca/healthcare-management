package com.healthcare.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient extends Person {

    private int age;

    public Patient(){

    }
    public Patient(String name, String phone, String email, int age)
    {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String getDetails() {
        return "Pacineti: "+ name +
                ", Mosha: " + age +
                ", Tel: " + phone +
                ", Email: " + email;
    }
}
