package com.clinic.app;

import java.time.LocalDateTime;

public class ConsultationItem {

    private String gender;

public ConsultationItem(String name, String type, LocalDateTime date, String notes, String status, String gender) {
    this.name = name;
    this.type = type;
    this.date = date;
    this.notes = notes;
    this.status = status;
    this.gender = gender;
}

public String getGender() {
    return gender;
}


    private String name;
    private String type;
    private LocalDateTime date;
    private String notes;
    private String status;

    public ConsultationItem(String name, String type, LocalDateTime date, String notes, String status) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.notes = notes;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
