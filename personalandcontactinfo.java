
package sadforms;

import sadforms.medicalhistory;
import javax.swing.*;
import java.awt.*;

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

            gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2;
            panel.add(bannerPanel2, gbc);
            gbc.gridwidth = 1;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Patient Name:"), gbc);
            gbc.gridx = 1;
            JPanel namePanel = new JPanel(new GridLayout(1,3,5,0));
            namePanel.add(new JTextField("Last", 10));
            namePanel.add(new JTextField("First", 10));
            namePanel.add(new JTextField("Middle", 10));
            panel.add(namePanel, gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Address:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(20), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Date of Birth:"), gbc);
            gbc.gridx = 1;
            JPanel dobPanel = new JPanel(new GridLayout(1,3,5,0));
            dobPanel.add(new JTextField("MM", 4));
            dobPanel.add(new JTextField("DD", 4));
            dobPanel.add(new JTextField("YYYY", 6));
            panel.add(dobPanel, gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Phone Number:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(15), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Gender:"), gbc);
            gbc.gridx = 1;
            panel.add(new JComboBox<>(new String[]{"Male","Female","Other"}), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(20), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Emergency Contact Name:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(20), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Relationship:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(15), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Emergency Contact Phone:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(15), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("If patient is under the age of 18"), gbc);
            gbc.gridx = 1;
            panel.add(new JLabel(""), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Parent/Legal Guardian Name:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(20), gbc);
            y++;

            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel("Guardian Phone Number:"), gbc);
            gbc.gridx = 1;
            panel.add(new JTextField(15), gbc);
            y++;

            
            Dimension buttonSize = new Dimension(100, 30);
            gbc.gridwidth = 1;
            gbc.gridx = 0; gbc.gridy = y;
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
            frame.dispose(); // close this page then open the next page lezgo
            medicalhistory.main(new String[]{}); 
            });
            panel.add(nextButton, gbc);

            mainPanel.add(panel, new GridBagConstraints());
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}

