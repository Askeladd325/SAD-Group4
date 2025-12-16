package com.clinic.app;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    /**
     * Retrieves all patients from the database.
     */
    public List<Patient> getAllPatients() throws SQLException{
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, age, sex, last_visit FROM PATIENTS ORDER BY last_name";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getString("sex"),
                    // Parse the TEXT field back into a LocalDate object
                    LocalDate.parse(rs.getString("last_visit")) 
                ));
            }
        
        }
        return patients;
    }

    /**
     * Saves a new patient to the database.
     */
    public void addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO PATIENTS (first_name, last_name, age, sex, last_visit) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setInt(3, patient.getAge());
            pstmt.setString(4, patient.getSex());
            
            // Convert LocalDate to String (YYYY-MM-DD) for SQLite
            pstmt.setString(5, patient.getLastVisit().toString()); 
            
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a list of patients by their IDs.
     * (Needed for your toolbar's "Delete selected" action)
     */
    public void deletePatients(List<Integer> ids) throws SQLException {
        // Create a string of question marks: e.g., "(?, ?, ?)"
        String placeholders = String.join(",", java.util.Collections.nCopies(ids.size(), "?"));
        String sql = "DELETE FROM PATIENTS WHERE id IN (" + placeholders + ")";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setInt(i + 1, ids.get(i));
            }
            pstmt.executeUpdate();
        }
    }
}