import java.sql.*;
import java.time.LocalDate;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Queries {
    // SAMPLE QUERY

    public static boolean addConsultationRecord(String symptoms, String findings, String diagnoses, String prescription,
            String severity, String status, String doctorName) {
        String sql = "insert into consultationRecord(patientSymptoms, doctorFindings, diagnoses, prescriptions, severity, status, doctorName) values(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, symptoms);
            ps.setString(2, findings);
            ps.setString(3, diagnoses);
            ps.setString(4, prescription);
            ps.setString(5, severity);
            ps.setString(6, status);
            ps.setString(7, doctorName);
            int rows = ps.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("add consult error");
            return false;
        }
    }

    public static void displayConsultationRecord(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "select dateOfVisit, diagnoses, severity, status, totalVisit from consultationRecord";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Date date = rs.getDate("dateOfVisit");
                String diagnoses = rs.getString("diagnoses");
                String severity = rs.getString("severity");
                String status = rs.getString("status");
                int visits = rs.getInt("totalVisit");

                model.addRow(new Object[] { date, diagnoses, severity, status, visits });
            }

        } catch (SQLException e) {
            System.out.println("display consult error");
            e.printStackTrace();
        }
    }

    public static void savePatientForms(PatientFormDrafts draft) throws SQLException {
        String sqlPersonalInfo = "insert into patient_and_contactInformation (patientName, address, date_of_birth, PhoneNumber, Gender, Email, emergencyContact, relationship, emergencyContact_phoneNumber, guardianName, guardian_phoneNumber) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlMedicalHistory = "insert into medicalHistory(PatientID, reason_for_visit, past_medical_problems, medications, allergies) values(?, ?, ?, ?, ?)";
        String sqlBillingInfo = "insert into insurance_and_billingInfor (patientID, insuranceProvider, insuranceID, name, address, phoneNumber, billing_address, paymentMethod, cardNumber) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        LocalDate dob = draft.getDOB();
        java.sql.Date sqlDob = (dob != null) ? java.sql.Date.valueOf(dob) : null;

        try {
            PreparedStatement psPersonalInfo = conn.prepareStatement(sqlPersonalInfo, Statement.RETURN_GENERATED_KEYS);
            psPersonalInfo.setString(1, draft.patientName);
            psPersonalInfo.setString(2, draft.address);
            psPersonalInfo.setDate(3, sqlDob);
            psPersonalInfo.setString(4, draft.phoneNumber);
            psPersonalInfo.setString(5, draft.gender);
            psPersonalInfo.setString(6, draft.email);
            psPersonalInfo.setString(7, draft.emergencyContact);
            psPersonalInfo.setString(8, draft.emergencyPhone);
            psPersonalInfo.setString(9, draft.guardianName);
            psPersonalInfo.setString(10, draft.guardianPhone);

            psPersonalInfo.executeUpdate();

            ResultSet rs = psPersonalInfo.getGeneratedKeys();
            if (!rs.next())
                throw new SQLException("Faield to get patientID");
            int patientID = rs.getInt(1);

            PreparedStatement psMedicalHistory = conn.prepareStatement(sqlMedicalHistory);
            psMedicalHistory.setInt(1, patientID);
            psMedicalHistory.setString(2, draft.reason);
            psMedicalHistory.setString(3, draft.pastProblems);
            psMedicalHistory.setString(4, draft.medications);
            psMedicalHistory.setString(5, draft.allergies);

            PreparedStatement psBillingInfo = conn.prepareStatement(sqlBillingInfo);
            psBillingInfo.setInt(1, patientID);
            psBillingInfo.setString(2, draft.insuranceProvider);
            psBillingInfo.setString(3, draft.insuranceID);
            psBillingInfo.setString(4, draft.name);
            psBillingInfo.setString(5, draft.insuranceAddress);
            psBillingInfo.setString(6, draft.insurancePhone);
            psBillingInfo.setString(7, draft.billingAddress);
            psBillingInfo.setString(8, draft.paymentMethod);
            psBillingInfo.setString(9, draft.cardNumber);

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}