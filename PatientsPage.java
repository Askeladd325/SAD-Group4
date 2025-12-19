import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    public PatientsPage() throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("PATIENTS MANAGER");
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        PIContainer.setOpaque(true);
        PIContainer.setBackground(Color.decode("#56C7D1"));

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
                PIContainer.setBackground(Color.decode("#56C7D1"));
                PIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
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
                CIContainer.setOpaque(false);
                CIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel,
                        appointmentLabel, mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                try {
                    new ConsultationPage().setVisible(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
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

        mainPanel.add(createMainPanel());

        add(layeredPane);

        loadData();
        switchToTab("LIST", listButton);
    }

    private void loadData() {
        model.setRowCount(0);
        gridViewPanel.removeAll();

        Queries.displayPatient(table);

        for (int i = 0; i < model.getRowCount(); i++) {
            String fullName = (String) model.getValueAt(i, 2);
            String age = model.getValueAt(i, 3).toString();
            String sex = (String) model.getValueAt(i, 4);
            gridViewPanel.add(createPatientCard(fullName, age, sex));
        }

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
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));

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

        rightPanel.add(inviteNew);
        rightPanel.add(userAvatar);
        rightPanel.add(bellButton);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
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
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String txt = searchField.getText();
                if (txt.isEmpty() || txt.equals("Search"))
                    sorter.setRowFilter(null);
                else
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txt));
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
        tabs.add(overviewButton);
        tabs.add(listButton);
        tabs.add(inviteButton);

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

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(140, 50);
            }
        };
        addBtn.setContentAreaFilled(false);
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> personalandcontactinfo.main(new String[] {}));

        p.add(addBtn);
        return p;
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
        JButton[] tabs = { overviewButton, listButton, inviteButton };
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

        JComboBox<String> actions = new JComboBox<>(new String[] { "Choose action", "Delete selected" });
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

        left.add(actions);

        totalPatientsLabel = new JLabel("Total patients: 0");
        totalPatientsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        bar.add(left, BorderLayout.WEST);
        bar.add(totalPatientsLabel, BorderLayout.EAST);
        return bar;
    }

    private JScrollPane createTablePanel() {
        String[] cols = { "", "ID", "First Name", "Age", "Sex", "Last Visit" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int c) {
                switch (c) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 0;
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionBackground(Color.decode("#56C7D1"));
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
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(184, 224, 238));
                setForeground(Color.BLACK);
                setFont(new Font("Arial", Font.BOLD, 14)); // Bold text
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(150, 200, 220)));
                return this;
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int viewRow = table.getSelectedRow();
                if (viewRow == -1)
                    return;

                int modelRow = table.convertRowIndexToModel(viewRow);
                int patientID = (int) model.getValueAt(modelRow, 1); // column 1 = ID

                openPatientProfile(patientID);
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
            Object value = model.getValueAt(i, 0);
            if (value instanceof Boolean && (Boolean) value) {
                sel++;
            }

        }
        selectedCountLabel.setText("Selected " + sel);
        totalPatientsLabel.setText("Total patients: " + model.getRowCount());
    }

    private void openPatientProfile(int patientID) {
        try {
            Patientprofilepage profile = new Patientprofilepage(patientID);
            profile.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open patient profile");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new PatientsPage().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}