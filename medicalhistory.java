import javax.swing.*;
import java.awt.*;

public class medicalhistory {
    public static void main(String[] args) {
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
            mainPanel.add(createFieldPanel("Reason for Visit:", bgColor), gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            mainPanel.add(createFieldPanel("List any current or past medical problems:", bgColor), gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            mainPanel.add(createFieldPanel("List any medication, dosage, duration:", bgColor), gbc);

            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            mainPanel.add(createFieldPanel("List any allergies to medication:", bgColor), gbc);

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
                frame.dispose(); // close current form
                insuranceandbillinginformation.main(new String[] {}); // open Insurance & Billing form
            });
            mainPanel.add(nextButton, gbc);

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }

    private static JPanel createFieldPanel(String labelText, Color bgColor) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(bgColor);

        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(400, 80));

        fieldPanel.add(label);
        fieldPanel.add(textField);

        return fieldPanel;
    }
}
