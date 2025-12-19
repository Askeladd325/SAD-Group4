import javax.swing.*;
import java.awt.*;

public class patientrecord extends JFrame {

    public patientrecord() {
        setTitle("Patient Record");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setUndecorated(true);

        Color bgColor = Color.decode("#CAE9F5");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(Color.decode("#86C5D7"));
        bannerPanel.setPreferredSize(new Dimension(600, 50));
        JLabel bannerLabel = new JLabel("PATIENT RECORD", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bannerLabel.setForeground(Color.BLACK);
        bannerPanel.add(bannerLabel, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        mainPanel.add(bannerPanel, gbc);

        JPanel idDatePanel = new JPanel(new GridLayout(1, 2, 40, 0));
        idDatePanel.setOpaque(false);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.setOpaque(false);
        idPanel.add(new JLabel("PATIENT ID: 676767"));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setOpaque(false);
        datePanel.add(new JLabel("DATE: Dec 32, 2025"));

        idDatePanel.add(idPanel);
        idDatePanel.add(datePanel);
        mainPanel.add(idDatePanel, setGbc(gbc, 0, y++));

        JPanel symptomsPanel = new JPanel(new BorderLayout());
        symptomsPanel.setBackground(Color.decode("#86C5D7"));
        JLabel symptomsLabel = new JLabel("PATIENT SYMPTOMS:");
        JTextArea symptomsArea = new JTextArea(4, 40);
        symptomsArea.setLineWrap(true);
        symptomsArea.setWrapStyleWord(true);
        JScrollPane symptomsScroll = new JScrollPane(symptomsArea);
        symptomsPanel.add(symptomsLabel, BorderLayout.NORTH);
        symptomsPanel.add(symptomsScroll, BorderLayout.CENTER);
        mainPanel.add(symptomsPanel, setGbc(gbc, 0, y++));

        mainPanel.add(createArea("DOCTOR'S FINDINGS:", 4), setGbc(gbc, 0, y++));
        mainPanel.add(createArea("DIAGNOSIS:", 3), setGbc(gbc, 0, y++));
        mainPanel.add(createArea("PRESCRIPTIONS:", 3), setGbc(gbc, 0, y++));

        JPanel doctorClosePanel = new JPanel(new BorderLayout());
        doctorClosePanel.setOpaque(false);

        JPanel doctorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doctorPanel.setOpaque(false);
        doctorPanel.add(new JLabel("DOCTOR: Dr. Senku Ishigami"));

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setOpaque(false);
        JButton closeButton = new JButton("CLOSE");
        closeButton.setPreferredSize(new Dimension(100, 30));
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);

        doctorClosePanel.add(doctorPanel, BorderLayout.WEST);
        doctorClosePanel.add(closePanel, BorderLayout.EAST);

        gbc.gridy++;
        mainPanel.add(doctorClosePanel, setGbc(gbc, 0, y++));

        setContentPane(mainPanel);
    }

    private static JPanel createArea(String labelText, int rows) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#86C5D7"));
        JLabel label = new JLabel(labelText);
        JTextArea area = new JTextArea(rows, 40);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(area);
        panel.add(label, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private static GridBagConstraints setGbc(GridBagConstraints gbc, int x, int y) {
        GridBagConstraints newGbc = (GridBagConstraints) gbc.clone();
        newGbc.gridx = x;
        newGbc.gridy = y;
        newGbc.gridwidth = 2;
        return newGbc;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new patientrecord().setVisible(true));
    }
}
