import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppointmentPage {
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

    private static JPanel searchField() {
        JPanel searchWrapper = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.decode("#ADD9E6"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchWrapper.setOpaque(false);
        searchWrapper.setPreferredSize(new Dimension(500, 40));
        searchWrapper.setMaximumSize(new Dimension(500, 40));
        searchWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField searchField = new JTextField("Search");
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
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
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        JLabel searchIcon = new JLabel("🔍 ");
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        searchWrapper.add(searchField, BorderLayout.CENTER);
        searchWrapper.add(searchIcon, BorderLayout.EAST);

        return searchWrapper;
    }

    static class ComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
        private JComboBox<String> comboBox;
        private JTable table;

        public ComboBoxEditor(JTable table) {
            this.table = table;
            comboBox = new JComboBox<>(new String[] { "Pending", "Completed", "Cancelled" });
            comboBox.setFont(new Font("Arial", Font.BOLD, 18));
            comboBox.addActionListener(e -> {
                int row = table.getSelectedRow();
                int appointmentId = (int) table.getValueAt(row, 0);
                String newStatus = (String) comboBox.getSelectedItem();

                try (Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE appointmentRecord SET status = ? WHERE appointmentId = ?")) {

                    ps.setString(1, newStatus);
                    ps.setInt(2, appointmentId);
                    ps.executeUpdate();

                    table.setValueAt(newStatus, row, 4);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                fireEditingStopped();
            });
        }

        public Object getCellEditorValue() {
            return comboBox.getSelectedItem();
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            comboBox.setSelectedItem(value);
            return comboBox;
        }
    }

    static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        public ComboBoxRenderer() {
            super(new String[] { "Pending", "Completed", "Cancelled" });
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            return this;
        }
    }

    public static void main(String[] args) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel header = new JLabel("MEDICAL APPOINTMENT");
        dashboard.setFont("Arial", Font.BOLD, 32, header);

        JButton addAppointBtn = new JButton("ADD APPOINTMENT +");
        addAppointBtn.setBackground(Color.decode("#86C5D8"));
        addAppointBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        int width = 250;
        int height = 60;
        addAppointBtn.setPreferredSize(new Dimension(width, height));
        addAppointBtn.setMaximumSize(new Dimension(width, height));
        addAppointBtn.setMinimumSize(new Dimension(width, height));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.add(header);
        panel1.add(Box.createHorizontalGlue());
        panel1.add(addAppointBtn);

        dashboard.setBorder(50, 100, 0, 100, panel1);

        JPanel searchFieldGlue = new JPanel();
        searchFieldGlue.setLayout(new BoxLayout(searchFieldGlue, BoxLayout.X_AXIS));
        dashboard.setBorder(25, 90, 0, 0, searchFieldGlue);

        searchFieldGlue.add(searchField());
        searchFieldGlue.add(Box.createHorizontalGlue());

        String[] col = { "AppointmentID", "Patient's Name", "Date", "Time", "Status", "Action" };
        Object[][] row = {};

        DefaultTableModel model = new DefaultTableModel(row, col) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        JTable table = new JTable(model);
        table.setShowGrid(false);
        table.setFont(new Font("Arial", Font.PLAIN, 20));
        table.setRowHeight(50);
        table.setFocusable(false);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.getColumn("Action").setCellRenderer(new ComboBoxRenderer());
        table.getColumn("Action").setCellEditor(new ComboBoxEditor(table));

        Queries.displayAppointmentRecord(table);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JTextField searchField = (JTextField) ((JPanel) searchFieldGlue.getComponent(0)).getComponent(0);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateFilter() {
                String text = searchField.getText();
                if (text.trim().length() == 0 || text.equalsIgnoreCase("Search")) {
                    sorter.setRowFilter(null); // show all rows
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#86C5D8"));
        tableHeader.setForeground(Color.BLACK);
        tableHeader.setOpaque(true);
        tableHeader.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setFont(new Font("Arial", Font.BOLD, 24));
                label.setHorizontalAlignment(CENTER);
                label.setForeground(Color.BLACK);
                label.setBackground(Color.decode("#86C5D8"));
                label.setOpaque(true);
                return label;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(true);

        addAppointBtn.addActionListener(e -> {
            new addAppointmentOverlay(table);
        });

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        // panel3.setOpaque(true);
        // panel3.setBackground(Color.decode("#86C5D8"));

        dashboard.setBorder(0, 0, 0, 0, panel3);

        panel3.add(scrollPane);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        mainContent.add(panel1);
        mainContent.add(searchFieldGlue);
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(panel3);

        // ---------------------------- FOR SIDEBAR ------------------------------
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

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

        BufferedImage appointmentImage = ImageIO.read(new File("res/appointmentIcon.png"));
        BufferedImage appointmentResized = resizeImage(appointmentImage, 50, 50);
        JLabel appointmentLabel = new JLabel(new ImageIcon(appointmentResized));

        JPanel DIContainer = dashboard.sidePanelIconContainers(dashboardLabel);
        JPanel PIContainer = dashboard.sidePanelIconContainers(patientLabel);
        JPanel AIContainer = dashboard.sidePanelIconContainers(appointmentLabel);

        AIContainer.setOpaque(true);
        AIContainer.setBackground(Color.decode("#56C7D1"));

        dashboard.setBorder(20, 10, 20, 0, dashboardLabel);
        dashboard.setBorder(20, 10, 20, 0, patientLabel, appointmentLabel);

        sidePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }
        });

        DIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                DIContainer.setOpaque(true);
                DIContainer.setBackground(Color.decode("#98F3F5"));
                DIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                DIContainer.setOpaque(false);
                DIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                try {
                    dashboard.main(new String[] {});
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                frame.dispose();
            }
        });

        PIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                PIContainer.setOpaque(true);
                PIContainer.setBackground(Color.decode("#98F3F5"));
                PIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                PIContainer.setOpaque(false);
                PIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                try {
                    new PatientsPage().setVisible(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                frame.dispose();
            }
        });

        AIContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                AIContainer.setOpaque(true);
                AIContainer.setBackground(Color.decode("#98F3F5"));
                AIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                dashboard.expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                AIContainer.setBackground(Color.decode("#56C7D1"));
                AIContainer.setBorder(null);

                dashboard.collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, appointmentLabel,
                        mainPanel, original);
            }
        });

        layeredPane.add(mainPanel, Integer.valueOf(0));
        layeredPane.add(sidePanel, Integer.valueOf(1));
        sidePanel.add(profileLabel, gbc(0, 0, GridBagConstraints.NORTH, 1, 0.05, GridBagConstraints.NONE));
        sideOptions.add(DIContainer);
        sideOptions.add(PIContainer);
        sideOptions.add(AIContainer);
        sidePanel.add(sideOptions, gbc(0, 1, GridBagConstraints.NORTHWEST, 1, 1, GridBagConstraints.HORIZONTAL));
        mainPanel.add(mainContent);

        frame.add(layeredPane);
        frame.setVisible(true);
    }
}
