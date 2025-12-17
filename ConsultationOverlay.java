import javax.swing.*;
import java.awt.*;

public class ConsultationOverlay{
    public ConsultationOverlay(){
        JFrame frame = new JFrame();
        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);

        JPanel contentPane = new JPanel();
        JPanel panel = new JPanel();
        JPanel headerPanel = new JPanel();
        JPanel bodyWrapper = new JPanel();
        JPanel bodyPanel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        headerPanel.setLayout(new BorderLayout());
        bodyWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));

        panel.setBackground(Color.decode("#CBE9F4"));
        headerPanel.setBackground(Color.decode("#86C5D7"));

        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        bodyWrapper.setOpaque(false);
        bodyPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("CONSULTATION RECORD");
        JLabel lbl1 = new JLabel("Patient Symptoms");
        JLabel lbl2 = new JLabel("Doctor's Findings");
        JLabel lbl3 = new JLabel("Diagnoses");
        JLabel lbl4 = new JLabel("Prescriptions");
        JLabel lbl5 = new JLabel("Doctor's Name");

        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lbl1.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl2.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl3.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl4.setAlignmentX(Component.LEFT_ALIGNMENT);

        int row = 4;
        int col = 66;
        JTextArea symptomsTA = new JTextArea(row, col);
        JTextArea findingsTA = new JTextArea(row, col);
        JTextArea diagnosesTA = new JTextArea(row, col);
        JTextArea prescriptionsTA = new JTextArea(row, col);

        symptomsTA.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        symptomsTA.setLineWrap(true);
        symptomsTA.setWrapStyleWord(true);
        findingsTA.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        findingsTA.setLineWrap(true);
        findingsTA.setWrapStyleWord(true);
        diagnosesTA.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        diagnosesTA.setLineWrap(true);
        diagnosesTA.setWrapStyleWord(true);
        prescriptionsTA.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        prescriptionsTA.setLineWrap(true);
        prescriptionsTA.setWrapStyleWord(true);

        symptomsTA.setAlignmentX(Component.LEFT_ALIGNMENT);
        findingsTA.setAlignmentX(Component.LEFT_ALIGNMENT);
        diagnosesTA.setAlignmentX(Component.LEFT_ALIGNMENT);
        prescriptionsTA.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        dashboard.setFont("Arial", Font.BOLD, 28, headerLabel);
        dashboard.setFont("Arial", Font.BOLD, 20, lbl1, lbl2, lbl3, lbl4, lbl5);
        symptomsTA.setFont(new Font("Arial", Font.PLAIN, 14));
        findingsTA.setFont(new Font("Arial", Font.PLAIN, 14));
        diagnosesTA.setFont(new Font("Arial", Font.PLAIN, 14));
        prescriptionsTA.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton saveButton = new JButton("SAVE");
        JButton cancelButton = new JButton("CANCEL");
    
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));

        saveButton.addActionListener(e -> {
            dashboard.notification(frame, "Form Saved Successfully");
            
            new Timer(1500, ev -> frame.dispose()).start();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
        });

        int width = 120;
        int height = 30;
        saveButton.setPreferredSize(new Dimension(width, height));
        saveButton.setMaximumSize(new Dimension(width, height));
        saveButton.setMinimumSize(new Dimension(width, height));
        cancelButton.setPreferredSize(new Dimension(width, height));
        cancelButton.setMaximumSize(new Dimension(width, height));
        cancelButton.setMinimumSize(new Dimension(width, height));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        int border = 30;
        dashboard.setBorder(border, border, border, border, panel);
        
        headerPanel.add(headerLabel);
        panel.add(headerPanel);
        bodyPanel.add(Box.createVerticalStrut(25));
        bodyPanel.add(lbl1);
        bodyPanel.add(symptomsTA);
        bodyPanel.add(Box.createVerticalStrut(30));
        bodyPanel.add(lbl2);
        bodyPanel.add(findingsTA);
        bodyPanel.add(Box.createVerticalStrut(30));
        bodyPanel.add(lbl3);
        bodyPanel.add(diagnosesTA);
        bodyPanel.add(Box.createVerticalStrut(30));
        bodyPanel.add(lbl4);
        bodyPanel.add(prescriptionsTA);
        bodyWrapper.add(bodyPanel);
        panel.add(bodyWrapper);
        panel.add(buttonPanel);

        contentPane.add(panel, BorderLayout.CENTER);
        frame.setContentPane(contentPane);

        frame.setVisible(true);
    }
}
