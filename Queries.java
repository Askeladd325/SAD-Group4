import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Queries {
    // SAMPLE QUERY

    public static boolean addConsultationRecord(int patientID, String symptoms, String findings, String diagnoses,
            String prescription,
            String severity, String status, String doctorName) {
        String sql = "insert into consultationRecord(patientID, patientSymptoms, doctorFindings, diagnoses, prescriptions, severity, status, doctorName) values(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientID);
            ps.setString(2, symptoms);
            ps.setString(3, findings);
            ps.setString(4, diagnoses);
            ps.setString(5, prescription);
            ps.setString(6, severity);
            ps.setString(7, status);
            ps.setString(8, doctorName);
            int rows = ps.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("add consult error");
            return false;
        }
    }

    public static void displayConsultationRecord(JTable table, int patientID) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "select dateOfVisit, diagnoses, severity, status from consultationRecord where patientID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();

            int visitNumber = 1;
            while (rs.next()) {

                Date date = rs.getDate("dateOfVisit");
                String diagnoses = rs.getString("diagnoses");
                String severity = rs.getString("severity");
                String status = rs.getString("status");

                model.addRow(new Object[] { date, diagnoses, severity, status, visitNumber });
                visitNumber++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePatientForms(PatientFormDrafts draft) throws SQLException {
        String sqlPersonalInfo = "insert into personal_and_contactInformation (patientName, address, date_of_birth, PhoneNumber, Gender, Email, emergencyContact, relationship, emergencyContact_phoneNumber, guardianName, guardian_phoneNumber) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlMedicalHistory = "insert into medicalHistory(PatientID, reason_for_visit, past_medical_problems, medications, allergies) values(?, ?, ?, ?, ?)";
        String sqlBillingInfo = "insert into insurance_and_billingInfo (patientID, insurance_Provider, insuranceID, name, address, phoneNumber, billing_address, paymentMethod, cardNumber) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            psPersonalInfo.setString(8, draft.relationship);
            psPersonalInfo.setString(9, draft.emergencyPhone);
            psPersonalInfo.setString(10, draft.guardianName);
            psPersonalInfo.setString(11, draft.guardianPhone);

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
            psMedicalHistory.executeUpdate();

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
            psBillingInfo.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public static void displayPatient(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "select patientID, patientName, date_of_birth, gender from personal_and_contactInformation";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int patientID = rs.getInt("patientID");
                String name = rs.getString("patientname");
                Date dobSql = rs.getDate("date_of_birth");
                int age = 0;
                if (dobSql != null) {
                    LocalDate dob = dobSql.toLocalDate();
                    age = Period.between(dob, LocalDate.now()).getYears();
                }
                String gender = rs.getString("gender");

                model.addRow(
                        new Object[] { false, patientID, name, String.valueOf(age), gender != null ? gender : "", "" });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static JLabel[] patientCount() {
        JLabel lbl1 = new JLabel();
        JLabel lbl2 = new JLabel();

        String sql1 = "select count(*) as total from personal_and_contactInformation";
        String sql2 = "select count(distinct patientID) as total from consultationRecord where date(dateofvisit) = curdate()";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql1);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int totalPatient = rs.getInt("total");
                lbl1.setText(String.valueOf(totalPatient));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lbl1.setText("0");
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql2);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int todayPatient = rs.getInt("total");
                lbl2.setText(String.valueOf(todayPatient));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lbl2.setText("0");
        }
        return new JLabel[] { lbl1, lbl2 };
    }

    public static JLabel[] profileInfo(int patientID) throws SQLException {
        JLabel lbl1 = new JLabel();
        JLabel lbl2 = new JLabel();
        JLabel lbl3 = new JLabel();
        JLabel lbl4 = new JLabel();
        JLabel lbl5 = new JLabel();
        JLabel lbl6 = new JLabel();

        String sql = "Select patientName, gender, date_of_birth, address, phoneNumber from personal_and_contactinformation where patientID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbl1.setText(rs.getString("patientName"));
                    lbl2.setText(rs.getString("gender"));
                    Date dobSql = rs.getDate("date_of_birth");
                    int age = 0;
                    if (dobSql != null) {
                        LocalDate dob = dobSql.toLocalDate();
                        age = Period.between(dob, LocalDate.now()).getYears();
                    }
                    lbl3.setText(String.valueOf(age));
                    lbl4.setText(rs.getString("address"));
                    lbl5.setText(rs.getDate("Date_of_Birth").toString());
                    lbl6.setText(rs.getString("phoneNumber"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lbl1.setText("");
            lbl2.setText("");
            lbl3.setText("");
            lbl4.setText("");
            lbl5.setText("");
            lbl6.setText("");
        }

        return new JLabel[] { lbl1, lbl2, lbl3, lbl4, lbl5, lbl6 };
    }
}