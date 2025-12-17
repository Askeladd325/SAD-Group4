import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class insuranceandbillinginformation {
    public static void open(PatientFormDrafts draft) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Insurance and Billing Information");
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

            JPanel bannerPanel = new JPanel(new BorderLayout());
            bannerPanel.setBackground(Color.decode("#86C5D7"));
            bannerPanel.setPreferredSize(new Dimension(600, 50));
            JLabel bannerLabel = new JLabel("INSURANCE AND BILLING INFORMATION", SwingConstants.CENTER);
            bannerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            bannerLabel.setForeground(Color.BLACK);
            bannerPanel.add(bannerLabel, BorderLayout.CENTER);
            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.gridwidth = 2;
            panel.add(bannerPanel, gbc);

            gbc.gridwidth = 1;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Insurance Provider:"), gbc);
            gbc.gridx = 1;
            JTextField insuranceProvider = new JTextField(20);
            panel.add(insuranceProvider, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("ID#:"), gbc);
            gbc.gridx = 1;
            JTextField insuranceID = new JTextField(15);
            panel.add(insuranceID, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Name of Policy Holder:"), gbc);
            gbc.gridx = 1;
            JPanel namePanel = new JPanel(new GridLayout(1, 3, 5, 0));
            JTextField last = new JTextField("Last", 10);
            JTextField first = new JTextField("First", 10);
            JTextField middle = new JTextField("Middle", 10);
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
            panel.add(new JLabel("Phone Number:"), gbc);
            gbc.gridx = 1;
            JTextField phoneNumber = new JTextField(15);
            panel.add(phoneNumber, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Billing Address:"), gbc);
            gbc.gridx = 1;
            JTextField billingAddress = new JTextField(20);
            panel.add(billingAddress, gbc);
            y++;

            gbc.gridx = 0;
            gbc.gridy = y;
            panel.add(new JLabel("Payment Method & Card Number:"), gbc);
            gbc.gridx = 1;
            JPanel paymentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            String[] paymentOption = { "----Select----", "Credit Card", "Debit Card", "Cash", "Insurance" };
            JComboBox<String> paymentBox = new JComboBox<>(paymentOption);
            paymentPanel.add(paymentBox);
            JTextField cardNumber = new JTextField(20);
            paymentPanel.add(cardNumber);
            panel.add(paymentPanel, gbc);
            y++;

            namePanel.setOpaque(false);
            paymentPanel.setOpaque(false);

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
                // medicalhistory.open(draft);
            });
            panel.add(backButton, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JButton submitButton = new JButton("SUBMIT");
            submitButton.setPreferredSize(buttonSize);
            submitButton.addActionListener(e -> {
                String insuranceProviderD = insuranceProvider.getText().trim();
                String insuranceIDD = insuranceID.getText().trim();
                String lName = last.getText().trim();
                String fName = first.getText().trim();
                String mName = middle.getText().trim();
                String fullName = (lName + ", " + fName + " " + mName);
                String addressD = address.getText().trim();
                String pNumber = phoneNumber.getText().trim();
                String billingAddressD = billingAddress.getText().trim();
                String paymentOptionD = (String) paymentBox.getSelectedItem();
                String cardNumberD = cardNumber.getText().trim();
                int methodIndex = paymentBox.getSelectedIndex();

                if (insuranceProviderD.isEmpty() || lName.isEmpty() || fName.isEmpty() || mName.isEmpty()
                        || addressD.isEmpty() || pNumber.isEmpty() || billingAddressD.isEmpty()
                        || methodIndex == -1 || paymentOptionD.isEmpty() || cardNumberD.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields");
                    return;
                } else {
                    draft.insuranceProvider = insuranceProviderD;
                    draft.insuranceID = insuranceIDD;
                    draft.name = fullName;
                    draft.insuranceAddress = addressD;
                    draft.insurancePhone = pNumber;
                    draft.billingAddress = billingAddressD;
                    draft.paymentMethod = paymentOptionD;
                    draft.cardNumber = cardNumberD;

                    try {
                        Queries.savePatientForms(draft);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    dashboard.notification(frame, "Saved Successfully!");
                    Timer timer = new Timer(2500, ev -> frame.dispose());
                    timer.setRepeats(false);
                    timer.start();
                }
            });
            panel.add(submitButton, gbc);

            mainPanel.add(panel, new GridBagConstraints());

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
