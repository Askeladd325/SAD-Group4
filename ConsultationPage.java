import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ConsultationPage extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:healthcare.db";
    private JPanel patientListContainer;
    private JLabel lblTotal, lblUpcoming, lblCompleted, lblCancelled;
    private JTextField searchField;
    private ArrayList<JPanel> allRows = new ArrayList<>();

    private final Color SELECTED_COLOR = new Color(240, 248, 255);

    public ConsultationPage() throws IOException {
        initDatabase();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Medical Consultation Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // ---------------------------- FOR SIDEBAR ------------------------------
        int frameWidth = this.getWidth();
        int frameHeight = this.getHeight();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, frameWidth, frameHeight);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBounds(80, 0, (frameWidth) - 80, frameHeight - 60);

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

        CIContainer.setOpaque(true);
        CIContainer.setBackground(Color.decode("#56C7D1"));

        dashboard.setBorder(20, 10, 20, 0, dashboardLabel);
        dashboard.setBorder(20, 10, 20, 0, patientLabel, consultLabel, appointmentLabel);

        sidePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }
        });

        DIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                DIContainer.setOpaque(true);
                DIContainer.setBackground(Color.decode("#98F3F5"));
                DIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                DIContainer.setOpaque(false);
                DIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                try {
                    dashboard.main(new String[] {});
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        });

        PIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                PIContainer.setOpaque(true);
                PIContainer.setBackground(Color.decode("#98F3F5"));
                PIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                PIContainer.setOpaque(false);
                PIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                try {
                    new PatientsPage().setVisible(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        });

        CIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                CIContainer.setOpaque(true);
                CIContainer.setBackground(Color.decode("#98F3F5"));
                CIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                CIContainer.setBackground(Color.decode("#56C7D1"));
                CIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }
        });

        AIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                AIContainer.setOpaque(true);
                AIContainer.setBackground(Color.decode("#98F3F5"));
                AIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                AIContainer.setOpaque(false);
                AIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
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

        mainPanel.add(setupUI());
        refreshData("");

        add(layeredPane);

    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS consultations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT, type TEXT, date TEXT, notes TEXT, status TEXT)");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM consultations");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO consultations (name, type, date, notes, status) VALUES " +
                        "('Teo Dizon', 'Follow-up', 'Nov 3, 2025, 10:00 AM', 'Discussed test result.', 'Completed')," +
                        "('Samantha Jones', 'Initial Check-up', 'Nov 28, 2025 2:30 PM', 'Patient coughs.', 'Upcoming'),"
                        +
                        "('Mark Rivera', 'Routine Check', 'Dec 05, 2025 09:00 AM', 'Blood pressure check.', 'Upcoming')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel setupUI() {
        JPanel root = new JPanel(new BorderLayout());

        // --- MAIN CONTENT ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("CONSULTATION PAGE");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 30)));

        // Search Bar
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchRow.setOpaque(false);
        searchRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField("Search") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(235, 235, 235));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));
                super.paintComponent(g);
                g2.dispose();
            }
        };
        searchField.setOpaque(false);
        searchField.setPreferredSize(new Dimension(220, 35));
        searchField.setBorder(new EmptyBorder(0, 35, 0, 10));
        searchField.setForeground(Color.GRAY);

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(10, 8, 20, 20);
        searchField.setLayout(null);
        searchField.add(searchIcon);

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText();
                refreshData(text.equals("Search") ? "" : text);
            }
        });

        JLabel filterBtn = new JLabel("FILTER");
        filterBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        filterBtn.setForeground(new Color(0, 191, 255));
        filterBtn.setBorder(new EmptyBorder(0, 30, 0, 0));

        searchRow.add(searchField);
        searchRow.add(filterBtn);
        header.add(searchRow);
        header.add(Box.createRigidArea(new Dimension(0, 30)));

        // Stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTotal = new JLabel("0");
        lblUpcoming = new JLabel("0");
        lblCompleted = new JLabel("0");
        lblCancelled = new JLabel("0");

        statsPanel.add(createStatItem(lblTotal, "Total Consultations"));
        statsPanel.add(createStatItem(lblUpcoming, "Upcoming Consultations"));
        statsPanel.add(createStatItem(lblCompleted, "Completed Consultations"));
        statsPanel.add(createStatItem(lblCancelled, "Cancelled Consultations"));

        // Patient List Container
        patientListContainer = new JPanel();
        patientListContainer.setLayout(new BoxLayout(patientListContainer, BoxLayout.Y_AXIS));
        patientListContainer.setBackground(Color.WHITE);

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        scrollWrapper.add(patientListContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollWrapper);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);

        JPanel topBody = new JPanel();
        topBody.setLayout(new BoxLayout(topBody, BoxLayout.Y_AXIS));
        topBody.setOpaque(false);
        topBody.setAlignmentX(Component.LEFT_ALIGNMENT);

        topBody.add(statsPanel);
        topBody.add(Box.createRigidArea(new Dimension(0, 35)));

        JLabel patientLabel = new JLabel("Patient");
        patientLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        patientLabel.setForeground(new Color(0, 191, 255));
        patientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        patientLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        topBody.add(patientLabel);

        body.add(topBody, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);

        mainContent.add(header, BorderLayout.NORTH);
        mainContent.add(body, BorderLayout.CENTER);

        root.add(mainContent, BorderLayout.CENTER);
        return root;
    }

    private JPanel createStatItem(JLabel countLabel, String text) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel desc = new JLabel("<html>" + text.replace(" ", "<br>") + "</html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 15));
        desc.setForeground(new Color(40, 40, 40));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(countLabel);
        p.add(Box.createRigidArea(new Dimension(0, 2)));
        p.add(desc);
        return p;
    }

    private void refreshData(String filterText) {
        patientListContainer.removeAll();
        allRows.clear();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rsStats = stmt.executeQuery("SELECT COUNT(*) as t, " +
                    "SUM(CASE WHEN status='Upcoming' THEN 1 ELSE 0 END) as u, " +
                    "SUM(CASE WHEN status='Completed' THEN 1 ELSE 0 END) as c FROM consultations");
            if (rsStats.next()) {
                lblTotal.setText(String.valueOf(rsStats.getInt("t")));
                lblUpcoming.setText(String.valueOf(rsStats.getInt("u")));
                lblCompleted.setText(String.valueOf(rsStats.getInt("c")));
            }

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM consultations WHERE name LIKE ?");
            pstmt.setString(1, "%" + filterText + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                JPanel row = createPatientRow(rs.getString("name"), rs.getString("type"), rs.getString("date"),
                        rs.getString("notes"), rs.getString("status"));
                allRows.add(row);
                patientListContainer.add(row);
                patientListContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        patientListContainer.revalidate();
        patientListContainer.repaint();
    }

    private JPanel createPatientRow(String name, String type, String date, String note, String status) {
        JPanel row = new JPanel(new BorderLayout());
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));
        row.setPreferredSize(new Dimension(0, 90));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(10, 20, 10, 20));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                for (JPanel p : allRows) {
                    p.setBackground(Color.WHITE);
                }

                row.setBackground(SELECTED_COLOR);
            }
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(300, 70));
        String iconPath = (name.toLowerCase().contains("samantha")) ? "resources/images/female.png"
                : "resources/images/male.png";
        JLabel avatar = createCircularIcon(iconPath, 55, 55);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 17));
        JLabel lblType = new JLabel("  " + type + "  ");
        lblType.setOpaque(true);
        lblType.setBackground(new Color(173, 216, 230));
        lblType.setFont(new Font("SansSerif", Font.BOLD, 11));
        namePanel.add(lblName);
        namePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        namePanel.add(lblType);
        leftPanel.add(avatar);
        leftPanel.add(namePanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JLabel lblNote = new JLabel(note);
        lblNote.setForeground(Color.GRAY);
        lblNote.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblDate);
        centerPanel.add(lblNote);
        centerPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 15));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(200, 70));
        JLabel badge = new JLabel(status, SwingConstants.CENTER);
        badge.setFont(new Font("SansSerif", Font.BOLD, 13));
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(110, 30));

        if (status.equalsIgnoreCase("Completed")) {
            badge.setBackground(new Color(129, 183, 193));
        } else {
            badge.setBackground(new Color(235, 245, 255));
        }
        rightPanel.add(badge);

        row.add(leftPanel, BorderLayout.WEST);
        row.add(centerPanel, BorderLayout.CENTER);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    private JLabel createCircularIcon(String path, int w, int h) {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new Ellipse2D.Double(0, 0, w, h));
                try {
                    ImageIcon icon = new ImageIcon(path);
                    g2.drawImage(icon.getImage(), 0, 0, w, h, this);
                } catch (Exception e) {
                }
                g2.dispose();
            }
        };
        label.setPreferredSize(new Dimension(w, h));
        label.setMinimumSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        return label;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ConsultationPage().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}