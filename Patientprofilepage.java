import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

public class Patientprofilepage extends JFrame {
    private int patientID;

    public Patientprofilepage(int patientID) throws IOException, SQLException {
        this.patientID = patientID;
        setTitle("Patient Profile Page");
        setSize(1000, 900);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Patient panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#86C5D8")),
                "Patient Profile",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                Color.decode("#86C5D8")));

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
        JLabel name = new JLabel();
        JLabel gender = new JLabel();
        JLabel age = new JLabel();
        JLabel address = new JLabel();
        JLabel status = new JLabel("<html><b>Status: </b>Active</html>");
        JLabel birthDate = new JLabel();
        JLabel contact = new JLabel();
        detailsPanel.add(name);
        detailsPanel.add(gender);
        detailsPanel.add(age);
        detailsPanel.add(address);
        detailsPanel.add(status);
        detailsPanel.add(birthDate);
        detailsPanel.add(contact);

        JLabel[] labels = Queries.profileInfo(patientID);
        name.setText("<html><b>Name: </b>" + labels[0].getText() + "</html>");
        gender.setText("<html><b>Gender: </b>" + labels[1].getText() + "</html>");
        age.setText("<html><b>Age: </b>" + labels[2].getText() + "</html>");
        address.setText("<html><b>Address: </b>" + labels[3].getText() + "</html>");
        birthDate.setText("<html><b>Birth Date: </b>" + labels[4].getText() + "</html>");
        contact.setText("<html><b>Contact Number: </b>" + labels[5].getText() + "</html>");

        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        dashboard.setFont("Arial", Font.PLAIN, 16, name, gender, age, address, status, birthDate, contact);

        profilePanel.add(imageLabel, BorderLayout.WEST);
        profilePanel.add(detailsPanel, BorderLayout.CENTER);

        String[] columns = { "Date of Visit", "Diagnosis", "Severity", "Status", "Total Visits" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable historyTable = new JTable(model);
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
                Color.decode("#86C5D8")));
        tableScroll.getViewport().setBackground(Color.decode("#CAE9F5"));
        tableScroll.setOpaque(false);

        // Buttons
        JButton recordButton = new JButton("RECORD");
        JButton deleteButton = new JButton("DELETE");
        JButton viewRecordButton = new JButton("VIEW RECORD");
        Queries.displayConsultationRecord(historyTable, patientID);

        // RECORD action
        recordButton.addActionListener(e -> {
            new ConsultationOverlay(historyTable, patientID);
        });

        // Deletebutton
        deleteButton.addActionListener(e -> {
            int selectedRow = historyTable.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel model2 = (DefaultTableModel) historyTable.getModel();
                model2.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });

        // view record button
        viewRecordButton.addActionListener(e -> {

            // Open Patient Record form
            SwingUtilities.invokeLater(() -> new patientrecord().setVisible(true));
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(recordButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewRecordButton);

        JPanel patientPanelContent = new JPanel(new BorderLayout());
        patientPanelContent.setBackground(Color.decode("#CAE9F5"));
        profilePanel.setBackground(Color.decode("#CAE9F5"));
        detailsPanel.setBackground(Color.decode("#CAE9F5"));
        buttonPanel.setBackground(Color.decode("#CAE9F5"));

        patientPanelContent.add(profilePanel, BorderLayout.NORTH);
        patientPanelContent.add(tableScroll, BorderLayout.CENTER);
        patientPanelContent.add(buttonPanel, BorderLayout.SOUTH);

        // Other panels
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.decode("#CAE9F5"));
        dashboardPanel.add(new JLabel("Welcome to Dashboard", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel appointmentPanelContent = new JPanel(new BorderLayout());
        appointmentPanelContent.setBackground(Color.decode("#CAE9F5"));
        appointmentPanelContent.add(new JLabel("Appointment Section", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel settingsPanelContent = new JPanel(new BorderLayout());
        settingsPanelContent.setBackground(Color.decode("#CAE9F5"));
        settingsPanelContent.add(new JLabel("Settings Section", SwingConstants.CENTER), BorderLayout.CENTER);
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(patientPanelContent, "Patient");

        CardLayout cl = (CardLayout) contentPanel.getLayout();

        cl.show(contentPanel, "Patient");

        // Layout
        getContentPane().setLayout(new BorderLayout());
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

    public static GridBagConstraints gbc(int x, int y, int anchor, double weightx, double weighty, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;

        return gbc;
    }

    public static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = resized.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return resized;
    }

    public static JPanel sidePanelIconContainers(JLabel label) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);

        return panel;
    }

    public static BufferedImage roundImage(BufferedImage original, int diameter) {
        BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = rounded.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(original, 0, 0, diameter, diameter, null);
        g2.dispose();

        return rounded;
    }
}
