import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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

        String sql = "SELECT pci.patientID, pci.patientName, pci.date_of_birth, pci.gender, " +
                "(SELECT MAX(cr.dateOfVisit) FROM consultationRecord cr WHERE cr.patientID = pci.patientID) AS lastVisit "
                +
                "FROM personal_and_contactInformation pci";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int patientID = rs.getInt("patientID");
                String name = rs.getString("patientName");
                Date dobSql = rs.getDate("date_of_birth");
                int age = 0;
                if (dobSql != null) {
                    LocalDate dob = dobSql.toLocalDate();
                    age = Period.between(dob, LocalDate.now()).getYears();
                }
                String gender = rs.getString("gender");
                Date lastVisit = rs.getDate("lastVisit");

                model.addRow(new Object[] {
                        false,
                        patientID,
                        name,
                        String.valueOf(age),
                        gender != null ? gender : "",
                        lastVisit != null ? lastVisit.toString() : ""
                });
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

    public static void consultationCount(JLabel label) {
        JLabel lbl = new JLabel();
        String sql = "Select count(*) as total from consultationRecord";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String consultationTotal = rs.getString("total");
                lbl.setText(String.valueOf(consultationTotal));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lbl.setText("0");
        }
    }

    public static int[] metrics() {
        String sqlNew = "select count(*) as newPatient from consultationRecord where dateofvisit >= now() - interval 6 hour";
        String sqlOld = "select count(*) as oldPatient from consultationRecord where dateofvisit < now() - interval 6 hour";

        int newP = 0;
        int old = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlNew);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    newP = rs.getInt(1);
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlOld);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    old = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[] { newP, old };
    }

    public static void displayAppointmentRecord(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "SELECT ar.appointmentId, pci.patientName, date AS date, time AS time, ar.status FROM appointmentRecord ar JOIN personal_and_contactinformation pci ON ar.patientId = pci.patientId ORDER BY ar.date";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("appointmentId"),
                        rs.getString("patientName"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status"),
                        "Update Status"
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void patientComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();

        String sql = "SELECT patientId, patientName FROM personal_and_contactinformation";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("patientId");
                String name = rs.getString("patientName");

                comboBox.addItem(name + " (ID: " + id + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertAppointment(int patientId, LocalDate date, LocalTime time, String reason) {
        String sql = "INSERT INTO appointmentRecord (patientId, date, time, reason, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(time));
            ps.setString(4, reason);
            ps.setString(5, "Pending");

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getAppointmentId(String patientName, java.sql.Time time) {
        int appointmentId = -1;
        String sql = "SELECT ar.appointmentId FROM appointmentRecord ar JOIN personal_and_contactinfo pci ON ar.patientId = pci.patientId WHERE CONCAT(pci.fname, ' ', pci.lname) = ? AND ar.time = ?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patientName);
            ps.setTime(2, time);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    appointmentId = rs.getInt("appointmentId");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointmentId;
    }

    public static void loadAppointments(DefaultTableModel model) {
        String sql = "SELECT p.PatientName, a.Time " +
                "FROM appointmentRecord a " +
                "JOIN personal_and_contactInformation p ON a.PatientID = p.PatientID " +
                "WHERE a.Date = ? " +
                "ORDER BY a.Time ASC";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));

            try (ResultSet rs = ps.executeQuery()) {
                int rowIndex = 0;
                while (rs.next()) {
                    String name = rs.getString("PatientName");
                    Time time = rs.getTime("Time");

                    if (rowIndex < model.getRowCount()) {
                        // Only update the second and third columns, keep the first column (image)
                        // intact
                        model.setValueAt(name, rowIndex, 1);
                        model.setValueAt(time.toString(), rowIndex, 2);
                    } else {
                        // If there are more rows in the database than the table, add new rows with a
                        // placeholder icon
                        model.addRow(new Object[] { new ImageIcon(/* default image */), name, time.toString() });
                    }

                    rowIndex++;
                }

                // Optionally remove extra rows if table has more rows than the result
                while (model.getRowCount() > rowIndex) {
                    model.removeRow(model.getRowCount() - 1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load appointments");
        }
    }

    public static void setTodaysAppointmentCount(JLabel label) {
        String sql = "SELECT COUNT(*) AS total FROM appointmentRecord WHERE Date = ?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            // set today's date
            LocalDate today = LocalDate.now();
            ps.setDate(1, java.sql.Date.valueOf(today));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    label.setText(String.valueOf(total));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            label.setText("0");
        }
    }

    public static void loadNextAppointments(DefaultTableModel model) {
        String sql = """
                    SELECT p.PatientName, a.PatientID
                    FROM appointmentRecord a
                    JOIN personal_and_contactInformation p ON a.PatientID = p.PatientID
                    WHERE a.Date >= CURDATE()
                    ORDER BY a.Date ASC, a.Time ASC
                    LIMIT ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // If you want to limit how many rows to load (e.g., 1 next appointment)
            ps.setInt(1, model.getRowCount());

            ResultSet rs = ps.executeQuery();
            int rowIndex = 0;

            while (rs.next() && rowIndex < model.getRowCount()) {
                // Keep the first column (image) intact
                model.setValueAt(rs.getString("PatientName"), rowIndex, 1);
                model.setValueAt(rs.getInt("PatientID"), rowIndex, 2);
                rowIndex++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}