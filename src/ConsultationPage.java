import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.ArrayList;

public class ConsultationPage extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:healthcare.db";
    private JPanel patientListContainer;
    private JLabel lblTotal, lblUpcoming, lblCompleted, lblCancelled;
    private JTextField searchField;
    private ArrayList<JPanel> allRows = new ArrayList<>();
    
    // Hex color #F0F8FF constant
    private final Color SELECTED_COLOR = new Color(240, 248, 255);

    public ConsultationPage() {
        initDatabase();
        setupUI();
        refreshData(""); 

        setTitle("Medical Consultation Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
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
                        "('Samantha Jones', 'Initial Check-up', 'Nov 28, 2025 2:30 PM', 'Patient coughs.', 'Upcoming')," +
                        "('Mark Rivera', 'Routine Check', 'Dec 05, 2025 09:00 AM', 'Blood pressure check.', 'Upcoming')");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void setupUI() {
        JPanel root = new JPanel(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(173, 216, 230));
        sidebar.setPreferredSize(new Dimension(85, 0));

        JPanel topIconsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topIconsPanel.setOpaque(false);
        JLabel doctorAvatar = createCircularIcon("resources/images/doctor.png", 55, 55);
        doctorAvatar.setBorder(new EmptyBorder(20, 0, 60, 0)); 
        topIconsPanel.add(doctorAvatar);
        topIconsPanel.add(createSidebarButton("resources/images/dashboard.jpg", false));
        topIconsPanel.add(createSidebarButton("resources/images/patient.png", false));
        topIconsPanel.add(createSidebarButton("resources/images/consultation.png", true));
        topIconsPanel.add(createSidebarButton("resources/images/appointment.png", false));

        JPanel bottomIconsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottomIconsPanel.setOpaque(false);
        bottomIconsPanel.add(createSidebarButton("resources/images/back.png", false));
        sidebar.add(topIconsPanel, BorderLayout.CENTER);
        sidebar.add(bottomIconsPanel, BorderLayout.SOUTH);

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
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 25, 25));
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

        //  Stats
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

        root.add(sidebar, BorderLayout.WEST);
        root.add(mainContent, BorderLayout.CENTER);
        add(root);
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
            if(rsStats.next()){
                lblTotal.setText(String.valueOf(rsStats.getInt("t")));
                lblUpcoming.setText(String.valueOf(rsStats.getInt("u")));
                lblCompleted.setText(String.valueOf(rsStats.getInt("c")));
            }

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM consultations WHERE name LIKE ?");
            pstmt.setString(1, "%" + filterText + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                JPanel row = createPatientRow(rs.getString("name"), rs.getString("type"), rs.getString("date"), rs.getString("notes"), rs.getString("status"));
                allRows.add(row);
                patientListContainer.add(row);
                patientListContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
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
        String iconPath = (name.toLowerCase().contains("samantha")) ? "resources/images/female.png" : "resources/images/male.png";
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
        
        if(status.equalsIgnoreCase("Completed")) {
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
                } catch (Exception e) {}
                g2.dispose();
            }
        };
        label.setPreferredSize(new Dimension(w, h));
        label.setMinimumSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        return label;
    }

    private JButton createSidebarButton(String fileName, boolean isActive) {
        JButton btn = new JButton();
        try {
            ImageIcon icon = new ImageIcon(fileName);
            Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        btn.setPreferredSize(new Dimension(85, 65));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBackground(isActive ? new Color(133, 193, 212) : new Color(173, 216, 230));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConsultationPage().setVisible(true));
    }
}