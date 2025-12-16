package patientprofilepage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Patientprofilepage extends JFrame {

    public Patientprofilepage() {
        setTitle("Patient Profile Page");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(4, 1));
        sidebar.setBackground(new Color(60, 63, 65));
        sidebar.setPreferredSize(new Dimension(180, 0));

        JButton dashboardBtn   = new JButton("Dashboard", scaleIcon("/icons/dashboard.jpg", 75, 75));
        JButton patientBtn     = new JButton("Patient", scaleIcon("/icons/patient.png", 75, 75));
        JButton appointmentBtn = new JButton("Appointment", scaleIcon("/icons/appointment.png", 75, 75));
        JButton settingsBtn    = new JButton("Settings", scaleIcon("/icons/settings.png", 75, 75));

        JButton[] navButtons = {dashboardBtn, patientBtn, appointmentBtn, settingsBtn};
        for (JButton btn : navButtons) {
            btn.setForeground(Color.WHITE);
            btn.setBackground(Color.decode("#AEDCEB"));
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(10);
            btn.setPreferredSize(new Dimension(180, 80));
            btn.setMargin(new Insets(10, 10, 10, 10));
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            sidebar.add(btn);
        }

        // -patient panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.decode("#86C5D8")),
            "Patient Profile",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 14),
            Color.decode("#86C5D8")
        ));

        JLabel imageLabel = new JLabel();
        ImageIcon profileIcon = loadIcon("/icons/profile.jpg");
        if (profileIcon != null) {
            Image scaledImage = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setText("No Image");
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel detailsPanel = new JPanel(new GridLayout(7, 1));
        detailsPanel.add(new JLabel("Name: Mr. Teo Dizon"));
        detailsPanel.add(new JLabel("Sex: Male"));
        detailsPanel.add(new JLabel("Age: 20"));
        detailsPanel.add(new JLabel("Address: Pasig City"));
        detailsPanel.add(new JLabel("Status: Active"));
        detailsPanel.add(new JLabel("Birthday: Aug 23, 2005"));
        detailsPanel.add(new JLabel("Contact: 09362578916"));

        profilePanel.add(imageLabel, BorderLayout.WEST);
        profilePanel.add(detailsPanel, BorderLayout.CENTER);

        String[] columns = {"Date of Visit", "Diagnosis", "Severity", "Status", "Total Visits"};
        Object[][] data = {
            {"May 27, 2025", "Common Cold", "Low", "Cured", "1"}
        };
        JTable historyTable = new JTable(new DefaultTableModel(data, columns));
        historyTable.setBackground(Color.decode("#CAE9F5"));
        historyTable.setGridColor(Color.GRAY);
        historyTable.setSelectionBackground(Color.decode("#AEDCEB"));
        historyTable.setSelectionForeground(Color.BLACK);

        JScrollPane tableScroll = new JScrollPane(historyTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.decode("#86C5D8")),
            "Patient History",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 14),
            Color.decode("#86C5D8")
        ));
        tableScroll.getViewport().setBackground(Color.decode("#CAE9F5"));

        JButton recordButton = new JButton("RECORD", scaleIcon("/icons/record.png", 24, 24));
        JButton deleteButton = new JButton("DELETE", scaleIcon("/icons/delete.png", 24, 24));

        recordButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
            model.addRow(new Object[]{
                "Date:", "Diagnosis:", "Severity:", "Status:", model.getRowCount() + 1
            });
            JOptionPane.showMessageDialog(this, "New record added successfully!");
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = historyTable.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(recordButton);
        buttonPanel.add(deleteButton);

        JPanel patientPanelContent = new JPanel(new BorderLayout());
        patientPanelContent.setBackground(Color.decode("#CAE9F5"));
        profilePanel.setBackground(Color.decode("#CAE9F5"));
        detailsPanel.setBackground(Color.decode("#CAE9F5"));
        buttonPanel.setBackground(Color.decode("#CAE9F5"));

        patientPanelContent.add(profilePanel, BorderLayout.NORTH);
        patientPanelContent.add(tableScroll, BorderLayout.CENTER);
        patientPanelContent.add(buttonPanel, BorderLayout.SOUTH);

        // other panels
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.decode("#CAE9F5"));
        dashboardPanel.add(new JLabel("Welcome to Dashboard", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel appointmentPanelContent = new JPanel(new BorderLayout());
        appointmentPanelContent.setBackground(Color.decode("#CAE9F5"));
        appointmentPanelContent.add(new JLabel("Appointment Section", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel settingsPanelContent = new JPanel(new BorderLayout());
        settingsPanelContent.setBackground(Color.decode("#CAE9F5"));
        settingsPanelContent.add(new JLabel("Settings Section", SwingConstants.CENTER), BorderLayout.CENTER);

        // cardlayout
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(patientPanelContent, "Patient");
        contentPanel.add(appointmentPanelContent, "Appointment");
        contentPanel.add(settingsPanelContent, "Settings");

        CardLayout cl = (CardLayout) contentPanel.getLayout();
        dashboardBtn.addActionListener(e -> cl.show(contentPanel, "Dashboard"));
        patientBtn.addActionListener(e -> cl.show(contentPanel, "Patient"));
        appointmentBtn.addActionListener(e -> cl.show(contentPanel, "Appointment"));
        settingsBtn.addActionListener(e -> cl.show(contentPanel, "Settings"));

        cl.show(contentPanel, "Patient");

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = loadIcon(path);
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Patientprofilepage().setVisible(true));
    }
}