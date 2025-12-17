import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class personalandcontactinfo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Patient Personal and Contact Information Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setLocationRelativeTo(null);
            frame.setUndecorated(true);

            Color bgColor = Color.decode("#CAE9F5");

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(bgColor);
            mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(bgColor);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            int y = 0;

            JPanel bannerPanel2 = new JPanel(new BorderLayout());
            bannerPanel2.setBackground(Color.decode("#86C5D7"));
            bannerPanel2.setPreferredSize(new Dimension(600, 50));

            JLabel bannerLabel2 = new JLabel("PERSONAL AND CONTACT INFORMATION", SwingConstants.CENTER);
            bannerLabel2.setFont(new Font("Arial", Font.BOLD, 20));
            bannerLabel2.setForeground(Color.BLACK);
            bannerPanel2.add(bannerLabel2, BorderLayout.CENTER);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            panel.add(bannerPanel2, gbc);
            gbc.gridwidth = 1;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Patient Name:"), gbc);
            gbc.gridx = 1;
            JPanel namePanel = new JPanel(new GridLayout(1, 3, 5, 0));
            JTextField last = new JTextField("Last", 10);
            JTextField first = new JTextField("First", 10);
            JTextField middle = new JTextField("M.I", 10);
            namePanel.add(last);
            namePanel.add(first);
            namePanel.add(middle);
            panel.add(namePanel, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Address:"), gbc);
            gbc.gridx = 1;
            JTextField address = new JTextField(20);
            panel.add(address, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Date of Birth:"), gbc);
            gbc.gridx = 1;
            JPanel dobPanel = new JPanel(new GridLayout(1, 3, 5, 0));
            JTextField month = new JTextField("MM", 4);
            JTextField day = new JTextField("DD", 4);
            JTextField year = new JTextField("YYYY", 6);
            dobPanel.add(month);
            dobPanel.add(day);
            dobPanel.add(year);
            panel.add(dobPanel, gbc);
            y++;

            namePanel.setOpaque(false);
            dobPanel.setOpaque(false);

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Phone Number:"), gbc);
            gbc.gridx = 1;
            JTextField phoneNumber = new JTextField(15);
            panel.add(phoneNumber, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Gender:"), gbc);
            gbc.gridx = 1;
            String[] genderOptions = { "Male", "Female", "Other" };
            JComboBox<String> genderBox = new JComboBox<>(genderOptions);
            panel.add(genderBox, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            JTextField email = new JTextField(20);
            panel.add(email, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Emergency Contact Name:"), gbc);
            gbc.gridx = 1;
            JTextField emergencyContactName = new JTextField(20);
            panel.add(emergencyContactName, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Relationship:"), gbc);
            gbc.gridx = 1;
            JTextField relationship = new JTextField(15);
            panel.add(relationship, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Emergency Contact Phone:"), gbc);
            gbc.gridx = 1;
            JTextField emergencyContact = new JTextField(15);
            panel.add(emergencyContact, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("If patient is under the age of 18"), gbc);
            gbc.gridx = 1;
            panel.add(new JLabel(""), gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Parent/Legal Guardian Name:"), gbc);
            gbc.gridx = 1;
            JTextField guardian = new JTextField(20);
            panel.add(guardian, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Guardian Phone Number:"), gbc);
            gbc.gridx = 1;
            JTextField guardianPhone = new JTextField(15);
            panel.add(guardianPhone, gbc);
            y++;

            Dimension buttonSize = new Dimension(100, 30);
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.WEST;

            JButton cancelButton = new JButton("CANCEL");
            cancelButton.setPreferredSize(buttonSize);
            cancelButton.addActionListener(e -> {
                frame.dispose();// close page only
            });
            panel.add(cancelButton, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JButton nextButton = new JButton("NEXT >");
            nextButton.setPreferredSize(buttonSize);
            nextButton.addActionListener(e -> {
                String monthText = month.getText().trim();
                String dayText = day.getText().trim();
                String yearText = year.getText().trim();

                PatientFormDrafts draft = new PatientFormDrafts();
                String fName = last.getText().trim();
                String lName = first.getText().trim();
                String mName = middle.getText().trim();
                String addressD = address.getText().trim();
                String pNumber = phoneNumber.getText().trim();
                String gender = (String) genderBox.getSelectedItem();
                String emailD = email.getText().trim();
                String eContactName = emergencyContactName.getText().trim();
                String relationshipD = relationship.getText().trim();
                String eContact = emergencyContact.getText().trim();
                String guardianD = guardian.getText().trim();
                String guardianContact = guardianPhone.getText().trim();

                if (fName.isEmpty() || lName.isEmpty() || mName.isEmpty() || addressD.isEmpty() ||
                        pNumber.isEmpty() || gender.isEmpty() || emailD.isEmpty() || eContactName.isEmpty() ||
                        relationshipD.isEmpty() || eContact.isEmpty() || guardianD.isEmpty()
                        || guardianContact.isEmpty()) {

                    JOptionPane.showMessageDialog(frame, "Please fill all fields");
                } else {
                    if (monthText.equals("MM") || dayText.equals("DD") || yearText.equals("YYYY")) {
                        JOptionPane.showMessageDialog(null, "Please enter valid MM/DD/YYYY");
                        return;
                    }

                    int cMonth, cDay, cYear;
                    try {
                        cMonth = Integer.parseInt(monthText);
                        cDay = Integer.parseInt(dayText);
                        cYear = Integer.parseInt(yearText);
                        int currentYear = LocalDate.now().getYear();

                        if (cMonth < 1 || cMonth > 12 || cDay < 1 || cDay > 31 || cYear < 1 || cYear > currentYear) {
                            JOptionPane.showMessageDialog(null, "Please enter valid MM/DD/YYYY");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter numeric valid MM/DD/YYYY");
                        return;
                    }

                    draft.patientName = fName + ", " + lName + " " + mName;
                    draft.address = addressD;
                    draft.year = cYear;
                    draft.day = cDay;
                    draft.month = cMonth;
                    draft.phoneNumber = pNumber;
                    draft.gender = gender;
                    draft.email = emailD;
                    draft.emergencyContact = eContactName;
                    draft.relationship = relationshipD;
                    draft.emergencyPhone = eContact;
                    draft.guardianName = guardianD;
                    draft.guardianPhone = guardianContact;

                    medicalhistory.open(draft);
                    frame.dispose();
                }
            });
            panel.add(nextButton, gbc);

            mainPanel.add(panel, new GridBagConstraints());
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
