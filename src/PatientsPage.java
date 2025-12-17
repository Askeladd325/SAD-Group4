import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.*;
import java.time.LocalDate;

public class PatientsPage extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    
    private JLabel selectedCountLabel, totalPatientsLabel; 
    private JButton overviewButton, listButton, inviteButton;
    private JTextField searchField; 
    private JPanel cardPanel; 
    private CardLayout cardLayout = new CardLayout();
    
    private JPanel displaySwitcherPanel;
    private CardLayout displayLayout = new CardLayout();
    private JPanel gridViewPanel;

    private static final String DB_URL = "jdbc:sqlite:clinic_data.db";

    public PatientsPage() {
        initDatabase();
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        setTitle("PATIENTS MANAGER");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);
        
        loadData(); 
        switchToTab("LIST", listButton);
    }

    private void initDatabase() {
    try (Connection conn = DriverManager.getConnection(DB_URL);
         Statement stmt = conn.createStatement()) {
       
        stmt.execute("CREATE TABLE IF NOT EXISTS patients (id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT, age TEXT, sex TEXT, last_visit TEXT)");
       
        stmt.execute("CREATE TABLE IF NOT EXISTS notifications (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, is_read INTEGER DEFAULT 0, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
    } catch (SQLException e) { e.printStackTrace(); }
}

    private void loadData() {
        model.setRowCount(0); 
        gridViewPanel.removeAll(); 
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM patients")) {
            while (rs.next()) {
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String age = rs.getString("age");
                String sex = rs.getString("sex");
                model.addRow(new Object[]{false, fName, lName, age, sex, rs.getString("last_visit")});
                gridViewPanel.add(createPatientCard(fName + " " + lName, age, sex));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        gridViewPanel.revalidate();
        gridViewPanel.repaint();
        updateCounts();
    }

   private JPanel createPatientCard(String name, String age, String sex) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(new Color(245, 250, 252));
    
    // Border and Padding
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 230, 240), 1),
        BorderFactory.createEmptyBorder(15, 10, 15, 10)
    ));


    card.setPreferredSize(new Dimension(140, 160)); 
    card.setMaximumSize(new Dimension(140, 160));
    

    JLabel nameLbl = new JLabel(name);
    nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    nameLbl.setFont(new Font("Arial", Font.BOLD, 16));

    JLabel ageLbl = new JLabel("Age: " + age);
    ageLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    ageLbl.setFont(new Font("Arial", Font.PLAIN, 14));

    JLabel sexLbl = new JLabel("Sex: " + sex);
    sexLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    sexLbl.setFont(new Font("Arial", Font.PLAIN, 14));

    card.add(Box.createVerticalGlue());
    card.add(nameLbl);
    card.add(Box.createRigidArea(new Dimension(0, 8)));
    card.add(ageLbl);
    card.add(sexLbl);
    card.add(Box.createVerticalGlue());
    
    return card;
}
   private JPanel createHeader() {
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(new Color(234, 246, 251)); 
    header.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

  
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    leftPanel.setOpaque(false);
    
    JLabel title = new JLabel("PATIENTS MANAGER");
    title.setFont(new Font("Arial", Font.BOLD, 22));
    leftPanel.add(title);
    

    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
    rightPanel.setOpaque(false);

    JButton inviteNew = new JButton("Invite new patients +");
    inviteNew.setBackground(new Color(144, 194, 211)); 
    inviteNew.setForeground(Color.BLACK);
    inviteNew.setFocusPainted(false);
    inviteNew.setBorderPainted(false); 
    inviteNew.setFont(new Font("Arial", Font.PLAIN, 14));
    inviteNew.setCursor(new Cursor(Cursor.HAND_CURSOR));

    inviteNew.setMargin(new Insets(5, 15, 5, 15)); 
    inviteNew.addActionListener(e -> switchToTab("INVITES", inviteButton));

    JLabel userAvatar = new JLabel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape circularClip = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
            g2.setClip(circularClip);
            super.paintComponent(g2);
            g2.dispose();
        }
    };
    userAvatar.setPreferredSize(new Dimension(40, 40));
    try {
   
        ImageIcon icon = new ImageIcon("resources/images/doctor.png");
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        userAvatar.setIcon(new ImageIcon(img));
    } catch (Exception e) {
        userAvatar.setBackground(Color.GRAY);
        userAvatar.setOpaque(true);
    }

    JButton bellButton = new JButton("🔔");
    bellButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
    bellButton.setContentAreaFilled(false);
    bellButton.setBorderPainted(false);
    bellButton.setFocusPainted(false);
    bellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    JPopupMenu notifMenu = new JPopupMenu();
    notifMenu.setBackground(Color.WHITE);
    notifMenu.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 240)));
    
    bellButton.addActionListener(e -> {
        notifMenu.removeAll();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT message FROM notifications ORDER BY id DESC LIMIT 5")) {
            boolean hasNotif = false;
            while (rs.next()) {
                addNotificationItem(notifMenu, rs.getString("message"));
                hasNotif = true;
            }
            if (!hasNotif) addNotificationItem(notifMenu, "No new notifications.");
        } catch (SQLException ex) { addNotificationItem(notifMenu, "Error loading."); }
        notifMenu.show(bellButton, -230, bellButton.getHeight());
    });

    rightPanel.add(inviteNew);
    rightPanel.add(userAvatar);
    rightPanel.add(bellButton);

    header.add(leftPanel, BorderLayout.WEST);
    header.add(rightPanel, BorderLayout.EAST);
    return header;
}

private void addNotificationItem(JPopupMenu menu, String text) {
    JMenuItem item = new JMenuItem("<html><body style='width: 200px; padding: 5px;'>" + text + "</body></html>");
    item.setFont(new Font("Arial", Font.PLAIN, 12));
    item.setBackground(Color.WHITE);

    item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)));
    menu.add(item);
}
    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);
        
        
        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(createHeader(), BorderLayout.NORTH);
        northContainer.add(createTabAndSearchArea(), BorderLayout.SOUTH);
        main.add(northContainer, BorderLayout.NORTH);

        cardPanel = new JPanel(cardLayout);
        JPanel listViewWrapper = new JPanel(new BorderLayout());
        listViewWrapper.setBackground(Color.WHITE);
        listViewWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        listViewWrapper.add(createToggleBar(), BorderLayout.NORTH);
        
        displaySwitcherPanel = new JPanel(displayLayout);
        displaySwitcherPanel.setOpaque(false);
        displaySwitcherPanel.add(createTablePanel(), "TABLE_UI");
        
        gridViewPanel = new JPanel(new GridLayout(0, 5, 15, 15));
        gridViewPanel.setBackground(Color.WHITE);
        JScrollPane gridScroll = new JScrollPane(gridViewPanel);
        gridScroll.setBorder(null);
        displaySwitcherPanel.add(gridScroll, "GRID_UI");

        listViewWrapper.add(displaySwitcherPanel, BorderLayout.CENTER);
        listViewWrapper.add(createAddButtonArea(), BorderLayout.SOUTH);
        
        cardPanel.add(listViewWrapper, "LIST");
        cardPanel.add(new JLabel("Patients View", 0), "OVERVIEW");
        cardPanel.add(new JLabel("Invitations View", 0), "INVITES");
        
        main.add(cardPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel createCustomSearchField() {
        JPanel searchWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 243, 248)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        searchWrapper.setPreferredSize(new Dimension(160, 30));
        searchWrapper.setOpaque(false);

        searchField = new JTextField("Search");
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        searchField.setOpaque(false);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK); 
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String txt = searchField.getText();
                if (txt.isEmpty() || txt.equals("Search")) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txt));
            }
        });

        JLabel searchIcon = new JLabel("🔍 "); 
        searchWrapper.add(searchField, BorderLayout.CENTER);
        searchWrapper.add(searchIcon, BorderLayout.EAST);
        return searchWrapper;
    }

    private JPanel createTabAndSearchArea() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        tabs.setOpaque(false);
        overviewButton = createTabBtn("Patients Overview", "OVERVIEW");
        listButton = createTabBtn("Patients List", "LIST");
        inviteButton = createTabBtn("Invitations", "INVITES");
        tabs.add(overviewButton); tabs.add(listButton); tabs.add(inviteButton);

        p.add(tabs, BorderLayout.WEST);
        p.add(createCustomSearchField(), BorderLayout.EAST); 
        return p;
    }

    private JPanel createAddButtonArea() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 30));
        p.setBackground(Color.WHITE);
        
        JButton addBtn = new JButton("ADD") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(173, 216, 230)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 45, 45);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString("ADD", 25, 32);
                g2.drawOval(getWidth() - 40, 10, 30, 30);
                g2.drawLine(getWidth() - 32, 25, getWidth() - 18, 25);
                g2.drawLine(getWidth() - 25, 18, getWidth() - 25, 32);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(140, 50); }
        };
        addBtn.setContentAreaFilled(false);
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddDialog());
        
        p.add(addBtn);
        return p;
    }

    private void showAddDialog() {
    JTextField fName = new JTextField();
    JTextField lName = new JTextField();
    JTextField age = new JTextField();
    JTextField lastVisit = new JTextField(LocalDate.now().toString());
    JComboBox<String> sex = new JComboBox<>(new String[]{"Male", "Female"});
    Object[] message = {"First Name:", fName, "Last Name:", lName, "Age:", age, "Sex:", sex, "Last Visit (YYYY-MM-DD):", lastVisit
};

    int option = JOptionPane.showConfirmDialog(this, message, "Add New Patient", JOptionPane.OK_CANCEL_OPTION);
    
    if (option == JOptionPane.OK_OPTION && !fName.getText().isEmpty()) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
      
            String sqlPatient = "INSERT INTO patients(first_name, last_name, age, sex, last_visit) VALUES(?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPatient)) {
                pstmt.setString(1, fName.getText());
                pstmt.setString(2, lName.getText());
                pstmt.setString(3, age.getText());
                pstmt.setString(4, (String) sex.getSelectedItem());
                pstmt.setString(5, lastVisit.getText());
                pstmt.executeUpdate();
            }

            String sqlNotif = "INSERT INTO notifications(message) VALUES(?)";
            try (PreparedStatement pstmtNotif = conn.prepareStatement(sqlNotif)) {
                String fullMessage = "Added new patient: " + fName.getText() + " " + lName.getText();
                pstmtNotif.setString(1, fullMessage);
                pstmtNotif.executeUpdate();
            }

            loadData(); 
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
        }
    }
}

    private JButton createTabBtn(String txt, String name) {
        JButton b = new JButton(txt);
        b.setPreferredSize(new Dimension(200, 60));
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setFocusPainted(false); 
        b.setContentAreaFilled(true);
        b.setForeground(new Color(100, 100, 100));
        b.addActionListener(e -> switchToTab(name, b));
        return b;
    }

    private void switchToTab(String name, JButton btn) {
        cardLayout.show(cardPanel, name);
        JButton[] tabs = {overviewButton, listButton, inviteButton};
        for (JButton b : tabs) {
            b.setBackground(new Color(230, 243, 248));
            b.setForeground(new Color(60, 60, 60)); 
        }
        btn.setBackground(new Color(153, 204, 221));
        btn.setForeground(Color.BLACK); 
    }

    private JPanel createToggleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);

        JLabel listViewIcon = new JLabel("≡"); 
        listViewIcon.setFont(new Font("Arial", Font.BOLD, 26));
        listViewIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        listViewIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                displayLayout.show(displaySwitcherPanel, "TABLE_UI");
            }
        });

        JLabel gridViewIcon = new JLabel();
    try {
        
        ImageIcon gIcon = new ImageIcon("resources/images/tileview.png"); 
        Image gImg = gIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        gridViewIcon.setIcon(new ImageIcon(gImg));
    } catch (Exception e) {
        gridViewIcon.setText("☷"); 
        gridViewIcon.setFont(new Font("Arial", Font.BOLD, 15));
    }
    
    gridViewIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
    gridViewIcon.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            displayLayout.show(displaySwitcherPanel, "GRID_UI");
        }
    });

        selectedCountLabel = new JLabel("Selected 0");
        selectedCountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectedCountLabel.setForeground(Color.GRAY);

        left.add(listViewIcon);
        left.add(gridViewIcon);
        left.add(selectedCountLabel);
        
        JComboBox<String> actions = new JComboBox<>(new String[]{"Choose action", "Delete selected"});
        actions.setFont(new Font("Arial", Font.PLAIN, 16)); 
        actions.setForeground(Color.GRAY);
        actions.setBackground(Color.WHITE);
        actions.setBorder(null);
        actions.setOpaque(false);
        actions.setFocusable(false);
        actions.setPreferredSize(new Dimension(150, 20));
        actions.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
    @Override
    protected JButton createArrowButton() {
        JButton b = super.createArrowButton();
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        return b;
    }
});
actions.setBorder(BorderFactory.createEmptyBorder());
actions.setOpaque(false);
actions.setFocusable(false);

        actions.addActionListener(e -> {
            if ("Delete selected".equals(actions.getSelectedItem())) {
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement pstmt = conn.prepareStatement("DELETE FROM patients WHERE first_name=? AND last_name=?")) {
                    for (int i = model.getRowCount()-1; i >= 0; i--) {
                        if ((Boolean)model.getValueAt(i, 0)) {
                            pstmt.setString(1, (String)model.getValueAt(i, 1));
                            pstmt.setString(2, (String)model.getValueAt(i, 2));
                            pstmt.executeUpdate();
                            model.removeRow(i);
                        }
                    }
                } catch (SQLException ex) { ex.printStackTrace(); }
                loadData(); 
                actions.setSelectedIndex(0);
            }
        });
        left.add(actions);

        totalPatientsLabel = new JLabel("Total patients: 0");
        totalPatientsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        bar.add(left, BorderLayout.WEST);
        bar.add(totalPatientsLabel, BorderLayout.EAST);
        return bar;
    }


    private JScrollPane createTablePanel() {
        String[] cols = {"", "First Name", "Last Name", "Age", "Sex", "Last Visit"};
        model = new DefaultTableModel(cols, 0) {
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : String.class; }
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setFocusable(false); 
    table.setRowSelectionAllowed(true); 
    table.setSelectionBackground(table.getBackground());
    table.setSelectionForeground(table.getForeground());

     table.setRowHeight(55);
    table.setShowGrid(false);
    table.setIntercellSpacing(new Dimension(0, 0));
    table.setBackground(Color.WHITE);
    sorter = new TableRowSorter<>(model);
    table.setRowSorter(sorter);
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    
    for (int i = 1; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
        // Header Customization
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(184, 224, 238)); 
                setForeground(Color.BLACK);
                setFont(new Font("Arial", Font.BOLD, 14)); // Bold text
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(150, 200, 220)));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        model.addTableModelListener(e -> updateCounts());
        return scrollPane;
    }

    private void updateCounts() {
        int sel = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean)model.getValueAt(i, 0)) sel++;
        }
        selectedCountLabel.setText("Selected " + sel);
        totalPatientsLabel.setText("Total patients: " + model.getRowCount());
    }

    private JPanel createSidebar() {
    JPanel sidebar = new JPanel();
    // Tamang light blue color base sa image_93e3de.png
    sidebar.setBackground(new Color(184, 224, 238)); 
    sidebar.setPreferredSize(new Dimension(80, 0));
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
    sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

    // Top Section Icons (Documents, Dashboard, Patients, Appointments)
    // Ang tooltip ay hindi lilitaw bilang text, icon lamang ang visible
    sidebar.add(createSideButton("document.png", "Documents", false));
    sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

    sidebar.add(createSideButton("dashboard.jpg", "Dashboard", false));
    sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

    // Eto ang active button sa iyong screenshot
    sidebar.add(createSideButton("patient.png", "Patients", true));
    sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

    sidebar.add(createSideButton("appointment.png", "Appointments", false));

    // Spacer para itulak ang back button sa pinakababa
    sidebar.add(Box.createVerticalGlue());

    // Bottom Section (Back Arrow)
    sidebar.add(createSideButton("back.png", "Go Back", false));

    return sidebar;
}
   private JButton createSideButton(String iconName, String tooltip, boolean isActive) {
    JButton button = new JButton();
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Flat style: walang border at default background
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setToolTipText(tooltip);
    
    // Sukat ng button area
    button.setPreferredSize(new Dimension(80, 65));
    button.setMaximumSize(new Dimension(80, 65));

    if (isActive) {
        // Ang mas madilim na blue highlight para sa napiling tab
        button.setBackground(new Color(144, 194, 211)); 
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    } else {
        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }

    try {
        String path = "resources/images/" + iconName;
        ImageIcon icon = new ImageIcon(path);
        // Ginawang 32x32 para maging malinaw at sakto ang laki
        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(img));
    } catch (Exception e) {
        button.setText("?"); 
    }

    return button;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientsPage().setVisible(true));
    }
}