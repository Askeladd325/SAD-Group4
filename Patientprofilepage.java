import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Patientprofilepage extends JFrame {

    public Patientprofilepage() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Patient Profile Page");
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

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

        // cardlayout
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(patientPanelContent, "Patient");

        CardLayout cl = (CardLayout) contentPanel.getLayout();

        cl.show(contentPanel, "Patient");

        int frameWidth = this.getWidth();
        int frameHeight = this.getHeight();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, frameWidth, frameHeight);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBounds(80, 0, (frameWidth), frameHeight);

        //For sidebar
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(0, 0, 80, frameHeight);
        sidePanel.setBackground(Color.decode("#AEDCEB"));
        sidePanel.setLayout(new GridBagLayout());
        dashboard.setBorder(15, 0, 0, 0, sidePanel);

        JPanel sideOptions = new JPanel();
        sideOptions.setLayout(new BoxLayout(sideOptions, BoxLayout.Y_AXIS));
        sideOptions.setOpaque(false);
        sideOptions.setMaximumSize(new Dimension(Integer.MAX_VALUE, sideOptions.getPreferredSize().height));

        BufferedImage original = ImageIO.read(new File("res/doctorProf.jpg"));
        int diameter = 70;

        BufferedImage rounded = roundImage(original, diameter);

        JLabel profileLabel = new JLabel(new ImageIcon(rounded));

        BufferedImage dashboardImage = ImageIO.read(new File("res/dashboardIcon.png"));
        BufferedImage dashboardResized = resizeImage(dashboardImage, 50, 50);
        JLabel dashboardLabel = new JLabel(new ImageIcon(dashboardResized));

        BufferedImage patientImage = ImageIO.read(new File("res/patientIcon.png"));
        BufferedImage patientResized = resizeImage(patientImage, 50, 50);
        JLabel patientLabel = new JLabel(new ImageIcon(patientResized));

        BufferedImage consultImage = ImageIO.read(new File("res/consultationIcon.png"));
        BufferedImage consultResized = resizeImage(consultImage, 50, 50);
        JLabel consultLabel = new JLabel(new ImageIcon(consultResized));

        BufferedImage appointmentImage = ImageIO.read(new File("res/appointmentIcon.png"));
        BufferedImage appointmentResized = resizeImage(appointmentImage, 50, 50);
        JLabel appointmentLabel = new JLabel(new ImageIcon(appointmentResized));

        JPanel DIContainer = dashboard.sidePanelIconContainers(dashboardLabel);
        JPanel PIContainer = dashboard.sidePanelIconContainers(patientLabel);
        JPanel CIContainer = dashboard.sidePanelIconContainers(consultLabel);
        JPanel AIContainer = dashboard.sidePanelIconContainers(appointmentLabel);

        PIContainer.setOpaque(true);
        PIContainer.setBackground(Color.decode("#56C7D1"));

        dashboard.setBorder(20, 10 , 20, 0, dashboardLabel);
        dashboard.setBorder(20, 10 , 20, 0, patientLabel, consultLabel, appointmentLabel);

        sidePanel.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e){
                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        DIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                DIContainer.setOpaque(true);
                DIContainer.setBackground(Color.decode("#98F3F5"));
                DIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                DIContainer.setOpaque(false);
                DIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e){
                try {
                    dashboard.main(new String[]{});
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        });

        PIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                PIContainer.setOpaque(true);
                PIContainer.setBackground(Color.decode("#98F3F5"));
                PIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                PIContainer.setBackground(Color.decode("#56C7D1"));
                PIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e){
                new ConsultationOverlay();
            }
        });

        CIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                CIContainer.setOpaque(true);
                CIContainer.setBackground(Color.decode("#98F3F5"));
                CIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                CIContainer.setOpaque(false);
                CIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        AIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                AIContainer.setOpaque(true);
                AIContainer.setBackground(Color.decode("#98F3F5"));
                AIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                AIContainer.setOpaque(false);
                AIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        layeredPane.add(mainPanel, Integer.valueOf(0));
        layeredPane.add(sidePanel, Integer.valueOf(1));
        sidePanel.add(profileLabel, gbc(0, 0, GridBagConstraints.NORTH, 1, 0.05, GridBagConstraints.NONE));
        sideOptions.add(DIContainer);
        sideOptions.add(PIContainer);
        sideOptions.add(CIContainer);
        sideOptions.add(AIContainer);
        sidePanel.add(sideOptions, gbc(0, 1, GridBagConstraints.NORTHWEST, 1, 1, GridBagConstraints.HORIZONTAL));

        mainPanel.add(contentPanel);

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(layeredPane, BorderLayout.CENTER);
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

    public static GridBagConstraints gbc(int x, int y, int anchor, double weightx, double weighty, int fill){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;

        return gbc;
    }

    public static BufferedImage resizeImage(BufferedImage original, int width, int height){
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = resized.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return resized;
    }

    public static JPanel sidePanelIconContainers(JLabel label){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);

        return panel;
    }

    public static BufferedImage roundImage(BufferedImage original, int diameter){
        BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = rounded.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(original, 0, 0, diameter, diameter, null);
        g2.dispose();

        return rounded;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Patientprofilepage().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}