import javax.swing.*;
import java.awt.*;

public class medicalhistory {
    public static void open(PatientFormDrafts draft) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setUndecorated(true);
            frame.setSize(800, 700);
            frame.setLocationRelativeTo(null);

            Color bgColor = Color.decode("#CAE9F5");

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(bgColor);
            mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int y = 0;

            JPanel bannerPanel = new JPanel(new BorderLayout());
            bannerPanel.setBackground(Color.decode("#86C5D7"));
            bannerPanel.setPreferredSize(new Dimension(600, 50));

            JLabel bannerLabel = new JLabel("MEDICAL HISTORY", SwingConstants.CENTER);
            bannerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            bannerLabel.setForeground(Color.BLACK);
            bannerPanel.add(bannerLabel, BorderLayout.CENTER);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            mainPanel.add(bannerPanel, gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            JLabel reasonLbl = new JLabel("Reason for visit");
            JTextArea reason = new JTextArea(3, 20);
            reason.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            reason.setLineWrap(true);
            reason.setWrapStyleWord(true);
            gbc.insets = new Insets(10, 10, 2, 10);
            gbc.gridy = y++;
            mainPanel.add(reasonLbl, gbc);
            gbc.insets = new Insets(2, 10, 8, 10);
            gbc.gridy++;
            mainPanel.add(reason, gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            JLabel medicalProblemLbl = new JLabel("List any current or past medical problems");
            JTextArea medicalProblem = new JTextArea(3, 20);
            medicalProblem.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            medicalProblem.setLineWrap(true);
            medicalProblem.setWrapStyleWord(true);
            gbc.insets = new Insets(10, 10, 2, 10);
            gbc.gridy = y++;
            mainPanel.add(medicalProblemLbl, gbc);
            gbc.insets = new Insets(2, 10, 8, 10);
            gbc.gridy++;
            mainPanel.add(medicalProblem, gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            JLabel medicationLbl = new JLabel("List any medication, dosage, duration");
            JTextArea medication = new JTextArea(3, 20);
            medication.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            medication.setLineWrap(true);
            medication.setWrapStyleWord(true);
            gbc.insets = new Insets(10, 10, 2, 10);
            gbc.gridy = y++;
            mainPanel.add(medicationLbl, gbc);
            gbc.insets = new Insets(2, 10, 8, 10);
            gbc.gridy++;
            mainPanel.add(medication, gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            JLabel allegyLbl = new JLabel("List any allergies to medication");
            JTextArea allergy = new JTextArea(3, 20);
            allergy.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            allergy.setLineWrap(true);
            allergy.setWrapStyleWord(true);
            gbc.insets = new Insets(10, 10, 2, 10);
            gbc.gridy = y++;
            mainPanel.add(allegyLbl, gbc);
            gbc.insets = new Insets(2, 10, 8, 10);
            gbc.gridy++;
            mainPanel.add(allergy, gbc);

            gbc.insets = new Insets(10, 10, 2, 10);
            gbc.gridy = y++;
            Dimension buttonSize = new Dimension(100, 30);
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.WEST;
            JButton backButton = new JButton("< BACK");
            backButton.setPreferredSize(buttonSize);
            backButton.addActionListener(e -> {
                frame.dispose();
                personalandcontactinfo.main(new String[] {});
            });
            mainPanel.add(backButton, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JButton nextButton = new JButton("NEXT >");
            nextButton.setPreferredSize(buttonSize);
            nextButton.addActionListener(e -> {
                String reasonD = reason.getText().trim();
                String medicalProblemD = medicalProblem.getText().trim();
                String medicationD = medication.getText().trim();
                String allergyD = allergy.getText().trim();

                if (reasonD.isEmpty() || medicalProblemD.isEmpty() || medicationD.isEmpty() || allergyD.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                } else {
                    draft.reason = reasonD;
                    draft.pastProblems = medicalProblemD;
                    draft.medications = medicationD;
                    draft.allergies = allergyD;

                    insuranceandbillinginformation.open(draft);
                    frame.dispose();
                }
            });
            mainPanel.add(nextButton, gbc);

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
