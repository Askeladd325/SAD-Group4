import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class addAppointmentOverlay {
    public addAppointmentOverlay(JTable table) {
        JFrame frame = new JFrame();
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));

        JButton backButton = new JButton("BACK");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setBackground(Color.decode("#86C5D7"));

        backButton.addActionListener(e -> {
            frame.dispose();
        });

        JLabel header = new JLabel("Add Appointment");
        dashboard.setFont("Arial", Font.BOLD, 32, header);

        header.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.setBackground(Color.decode("#86C5D7"));

        int height = 75;
        int width = 1000;
        panel1.setPreferredSize(new Dimension(width, height));
        panel1.setMaximumSize(new Dimension(width, height));

        panel1.add(Box.createHorizontalStrut(20));
        panel1.add(backButton);
        panel1.add(header);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.setOpaque(false);

        JComboBox<String> comboBox = new JComboBox<>();
        Queries.patientComboBox(comboBox);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        int comboWidth = 300;
        int comboHeight = 50;
        comboBox.setPreferredSize(new Dimension(comboWidth, comboHeight));
        comboBox.setMaximumSize(new Dimension(comboWidth, comboHeight));
        comboBox.setMinimumSize(new Dimension(comboWidth, comboHeight));

        panel2.add(comboBox);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.setOpaque(false);
        panel3.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setDate(new Date());
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 22));

        JTextField dateTextField = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateTextField.setHorizontalAlignment(JTextField.CENTER);

        int dateWidth = 200;
        int dateHeight = 50;
        dateChooser.setPreferredSize(new DimensionUIResource(dateWidth, dateHeight));
        dateChooser.setMaximumSize(new DimensionUIResource(dateWidth, dateHeight));
        dateChooser.setMinimumSize(new DimensionUIResource(dateWidth, dateHeight));

        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        timeSpinner.setFont(new Font("Arial", Font.PLAIN, 22));

        JSpinner.DateEditor timeEditor = (JSpinner.DateEditor) timeSpinner.getEditor();
        timeEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

        timeSpinner.setPreferredSize(new DimensionUIResource(dateWidth, dateHeight));
        timeSpinner.setMaximumSize(new DimensionUIResource(dateWidth, dateHeight));
        timeSpinner.setMinimumSize(new DimensionUIResource(dateWidth, dateHeight));

        JLabel dateLbl = new JLabel("DATE: ");
        JLabel timeLbl = new JLabel("TIME: ");

        panel3.add(dateLbl);
        panel3.add(dateChooser);
        panel3.add(Box.createHorizontalGlue());
        panel3.add(timeLbl);
        panel3.add(timeSpinner);

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
        panel4.setOpaque(false);

        JLabel reasonLbl = new JLabel("REASON");
        reasonLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        dashboard.setFont("Arial", Font.BOLD, 22, dateLbl, timeLbl, reasonLbl);

        JTextArea reasonTA = new JTextArea(3, 20);
        reasonTA.setFont(new Font("Arial", Font.PLAIN, 18));
        reasonTA.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        reasonTA.setLineWrap(true);
        reasonTA.setWrapStyleWord(true);

        int taWidth = 400;
        int taHeight = 200;
        reasonTA.setPreferredSize(new Dimension(taWidth, taHeight));
        reasonTA.setMaximumSize(new Dimension(taWidth, taHeight));
        reasonTA.setMinimumSize(new Dimension(taWidth, taHeight));

        panel4.add(reasonLbl);
        panel4.add(Box.createVerticalStrut(5));
        panel4.add(reasonTA);

        JPanel panel5 = new JPanel();
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));

        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.setFont(new Font("Arial", Font.PLAIN, 18));

        submitBtn.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            if (selected == null || selected.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select a patient.");
                return;
            }

            int patientId;
            try {
                patientId = Integer.parseInt(selected.replaceAll(".*ID: (\\d+)\\).*", "$1"));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid patient selection.");
                return;
            }

            Date date = dateChooser.getDate();
            if (date == null) {
                JOptionPane.showMessageDialog(frame, "Please select a date.");
                return;
            }
            java.time.LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();

            Date timeValue = (Date) timeSpinner.getValue();
            java.time.LocalTime localTime = new java.sql.Time(timeValue.getTime()).toLocalTime();

            String reason = reasonTA.getText().trim();
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a reason.");
                return;
            }

            Queries.insertAppointment(patientId, localDate, localTime, reason);
            Queries.displayAppointmentRecord(table);

            JOptionPane.showMessageDialog(frame, "Appointment added successfully!");
            frame.dispose();
        });

        panel5.add(submitBtn);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#CBE9F4"));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(panel1);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(panel2);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(panel3);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(panel4);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(panel5);

        contentPane.add(mainPanel, BorderLayout.CENTER);

        frame.setContentPane(contentPane);
        frame.setVisible(true);
    }
}
