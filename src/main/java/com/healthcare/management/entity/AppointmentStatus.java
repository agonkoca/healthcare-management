package com.healthcare.management.entity;

public enum AppointmentStatus {

    SCHEDULED("Planifikuar"),
    COMPLETED("Perfunduar"),
    CANCELLED("Anuluar");

    private String albanianLabel;

    AppointmentStatus(String Label){
        this.albanianLabel = Label;
    }

    public String getAlbanianLabel(){
        return albanianLabel;
    }

    @Override
    public String toString(){
        return albanianLabel;
    }
}
