import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class dashboard{
    
    private static GridBagConstraints gbc(int x, int y, int anchor, double weightx, double weighty, int fill){
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

    public static void setBorder(int top, int left, int bottom, int right, JComponent... comps){
        for(JComponent comp: comps){
            comp.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        }
    }

    private static JPanel sidePanelIconContainers(JLabel label){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);

        return panel;
    }

    private static BufferedImage roundImage(BufferedImage original, int diameter){
        BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = rounded.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(original, 0, 0, diameter, diameter, null);
        g2.dispose();

        return rounded;
    }

    private static BufferedImage resizeImage(BufferedImage original, int sizeWH){
        BufferedImage resized = new BufferedImage(sizeWH, sizeWH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = resized.createGraphics();
        g2.drawImage(original, 0, 0, sizeWH, sizeWH, null);
        g2.dispose();

        return resized;
    }

    public static void setFont(String fontName, int style, int size, JLabel... labels) {
        for(JLabel label: labels){
            label.setFont(new Font(fontName, style, size));
        }
    }

    public static BufferedImage loadImage(String path) throws IOException{
        return ImageIO.read(new File(path));
    }

    public static void notification(JFrame frame, String message) {
        JWindow accepted = new JWindow(frame);
        accepted.setLayout(new BorderLayout());
        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 20));
        lbl.setOpaque(true);
        lbl.setBackground(Color.BLACK);
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        accepted.add(lbl, BorderLayout.CENTER);

        accepted.pack();
        accepted.setLocationRelativeTo(frame);
        accepted.setLocation(accepted.getX(), accepted.getY() - 50);

        new Timer(2500, e -> accepted.setVisible(false)).start();

        accepted.setVisible(true);
    }

    private static void clearAllSelections(JTable... tables) {
        for(JTable table:tables){
            table.clearSelection();
        }
    }

    private static void expandSidebar(JPanel sidePanel, JLabel profileLabel, JLabel dashboardLabel, JLabel patientLabel, JLabel consultLabel, 
        JLabel appointmentLabel, JPanel mainPanel, BufferedImage original){ 
            int diameter = 150;

            BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = rounded.createGraphics();
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
            g2.drawImage(original, 0, 0, diameter, diameter, null);
            g2.dispose(); profileLabel.setIcon(new ImageIcon(rounded)); 
            
            sidePanel.setBounds(0, 0, 250, sidePanel.getHeight()); 

            dashboardLabel.setText("DASHBOARD"); 
            dashboardLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
            dashboardLabel.setIconTextGap(10); 

            patientLabel.setText("PATIENTS"); 
            patientLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
            patientLabel.setIconTextGap(10); 

            consultLabel.setText("CONSULTATION"); 
            consultLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
            consultLabel.setIconTextGap(10); 

            appointmentLabel.setText("APPOINTMENT"); 
            appointmentLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
            appointmentLabel.setIconTextGap(10); 

            mainPanel.revalidate(); 
            mainPanel.repaint(); 
    }

    private static void collapseSidebar(JPanel sidePanel, JLabel profileLabel, JLabel dashboardLabel, JLabel patientLabel, JLabel consultLabel, 
        JLabel appointmentLabel, JPanel mainPanel, BufferedImage original){ 
            int diameter = 70; 
            
            BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB); 
            Graphics2D g2 = rounded.createGraphics(); 
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter)); 
            g2.drawImage(original, 0, 0, diameter, diameter, null); 
            g2.dispose(); 

            profileLabel.setIcon(new ImageIcon(rounded)); 

            sidePanel.setBounds(0, 0, 80, 
            sidePanel.getHeight()); 

            dashboardLabel.setText(null); 
            patientLabel.setText(null); 
            consultLabel.setText(null); 
            appointmentLabel.setText(null); 
            
            mainPanel.revalidate(); 
            mainPanel.repaint(); 
    }

    class CalendarPanel extends JPanel {
        private YearMonth currentMonth;
        private JPanel calendarGrid;
        private JLabel monthLabel;

        public CalendarPanel() {
            this.currentMonth = YearMonth.now();
            setLayout(new BorderLayout());
            setBorder(new LineBorder(Color.GRAY, 1, true));
            setBackground(Color.WHITE);

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);

            JButton prevButton = new JButton("<");
            JButton nextButton = new JButton(">");

            // prevButton.setPreferredSize(new Dimension(40, 25));
            // nextButton.setPreferredSize(new Dimension(40, 25));

            // prevButton.setFont(new Font("Arial", Font.BOLD, 11));
            // nextButton.setFont(new Font("Arial", Font.BOLD, 11));

            prevButton.addActionListener(e -> changeMonth(-1));
            nextButton.addActionListener(e -> changeMonth(1));

            monthLabel = new JLabel("", SwingConstants.CENTER);
            monthLabel.setFont(new Font("Arial", Font.BOLD, 16));

            topPanel.add(prevButton, BorderLayout.WEST);
            topPanel.add(monthLabel, BorderLayout.CENTER);
            topPanel.add(nextButton, BorderLayout.EAST);

            add(topPanel, BorderLayout.NORTH);

            calendarGrid = new JPanel(new GridLayout(0, 7));
            add(calendarGrid, BorderLayout.CENTER);

            drawCalendar();
        }

        private void drawCalendar() {
            calendarGrid.removeAll();
            monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

            String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String day : weekdays) {
                JLabel lbl = new JLabel(day, SwingConstants.CENTER);
                lbl.setFont(new Font("Arial", Font.BOLD, 12));
                lbl.setBorder(new LineBorder(Color.LIGHT_GRAY));
                calendarGrid.add(lbl);
            }

            LocalDate firstDay = currentMonth.atDay(1);
            int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7;

            for (int i = 0; i < dayOfWeek; i++) {
                calendarGrid.add(new JLabel(""));
            }

            int daysInMonth = currentMonth.lengthOfMonth();
            for (int day = 1; day <= daysInMonth; day++) {
                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setFocusPainted(false);
                dayButton.setBackground(Color.WHITE);
                dayButton.setBorder(new LineBorder(Color.LIGHT_GRAY));

                LocalDate today = LocalDate.now();
                if (today.equals(currentMonth.atDay(day))) {
                    dayButton.setBackground(new Color(173, 216, 230));
                }

                calendarGrid.add(dayButton);
            }

            revalidate();
            repaint();
        }

        private void changeMonth(int delta) {
            currentMonth = currentMonth.plusMonths(delta);
            drawCalendar();
        }
    }

    public static void main(String[] args) throws Exception{
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame mainFrame = new JFrame("DASHBOARD");
        mainFrame.setSize(screenSize.width,screenSize.height);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBounds(80, 0, (mainFrame.getWidth() - 80), mainFrame.getHeight());

        //For sidebar
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(0, 0, 80, mainFrame.getHeight());
        sidePanel.setBackground(Color.decode("#AEDCEB"));
        sidePanel.setLayout(new GridBagLayout());
        setBorder(15, 0, 0, 0, sidePanel);

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

        JPanel DIContainer = sidePanelIconContainers(dashboardLabel);
        JPanel PIContainer = sidePanelIconContainers(patientLabel);
        JPanel CIContainer = sidePanelIconContainers(consultLabel);
        JPanel AIContainer = sidePanelIconContainers(appointmentLabel);

        DIContainer.setOpaque(true);
        DIContainer.setBackground(Color.decode("#56C7D1"));

        setBorder(20, 10 , 20, 0, dashboardLabel);
        setBorder(20, 10 , 20, 0, patientLabel, consultLabel, appointmentLabel);

        sidePanel.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }

            public void mouseExited(java.awt.event.MouseEvent e){
                collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        DIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                DIContainer.setBackground(Color.decode("#98F3F5"));
                DIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                DIContainer.setBackground(Color.decode("#56C7D1"));
                DIContainer.setBorder(null);

                collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        PIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                PIContainer.setOpaque(true);
                PIContainer.setBackground(Color.decode("#98F3F5"));
                PIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                PIContainer.setOpaque(false);
                PIContainer.setBorder(null);

                collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
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

                expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                CIContainer.setOpaque(false);
                CIContainer.setBorder(null);

                collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        AIContainer.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){
                AIContainer.setOpaque(true);
                AIContainer.setBackground(Color.decode("#98F3F5"));
                AIContainer.setBorder(BorderFactory.createLineBorder(Color.decode("#56C7D1"), 2));

                expandSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
            public void mouseExited(java.awt.event.MouseEvent e){
                AIContainer.setOpaque(false);
                AIContainer.setBorder(null);

                collapseSidebar(sidePanel, profileLabel, dashboardLabel, patientLabel, consultLabel, appointmentLabel, mainPanel, original);
            }
        });

        //for centerpanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        setBorder(55, 0, 0, 0, centerPanel);
        JLabel header = new JLabel("DASHBOARD");
        header.setFont(new Font("Arial", Font.BOLD, 55));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel circleRow = new JPanel();
        circleRow.setLayout(new BoxLayout(circleRow, BoxLayout.X_AXIS));
        circleRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel c1Container = new JPanel();
        JPanel c2Container = new JPanel();
        JPanel c3Container = new JPanel();
        JPanel textPanel1 = new JPanel();
        JPanel textPanel2 = new JPanel();
        JPanel textPanel3 = new JPanel();

        c1Container.setLayout(new BoxLayout(c1Container, BoxLayout.X_AXIS));
        c2Container.setLayout(new BoxLayout(c2Container, BoxLayout.X_AXIS));
        c3Container.setLayout(new BoxLayout(c3Container, BoxLayout.X_AXIS));
        textPanel1.setLayout(new BoxLayout(textPanel1, BoxLayout.Y_AXIS));
        textPanel2.setLayout(new BoxLayout(textPanel2, BoxLayout.Y_AXIS));
        textPanel3.setLayout(new BoxLayout(textPanel3, BoxLayout.Y_AXIS));

        BufferedImage c1 = ImageIO.read(new File("res/lBlue.png"));
        BufferedImage c1Rounded = roundImage(c1, 100);
        JLabel circle1 = new JLabel(new ImageIcon(c1Rounded));

        BufferedImage c2 = ImageIO.read(new File("res/lBlue.png"));
        BufferedImage c2Rounded = roundImage(c2, 100);
        JLabel circle2 = new JLabel(new ImageIcon(c2Rounded));

        BufferedImage c3 = ImageIO.read(new File("res/lBlue.png"));
        BufferedImage c3Rounded = roundImage(c3, 100);
        JLabel circle3 = new JLabel(new ImageIcon(c3Rounded));

        BufferedImage inner1 = ImageIO.read(new File("res/totalPatient.png"));
        BufferedImage innerImage1 = resizeImage(inner1, 70);
        JLabel innerLabel1 = new JLabel(new ImageIcon(innerImage1)); 

        BufferedImage inner2 = ImageIO.read(new File("res/todayPatient.png"));
        BufferedImage innerImage2 = resizeImage(inner2, 70);
        JLabel innerLabel2 = new JLabel(new ImageIcon(innerImage2)); 

        BufferedImage inner3 = ImageIO.read(new File("res/todayAppointment.png"));
        BufferedImage innerImage3 = resizeImage(inner3, 70);
        JLabel innerLabel3 = new JLabel(new ImageIcon(innerImage3)); 

        circle1.setLayout(new BorderLayout());
        circle2.setLayout(new BorderLayout());
        circle3.setLayout(new BorderLayout());

        circle1.add(innerLabel1);
        circle2.add(innerLabel2);
        circle3.add(innerLabel3);

        JLabel totalLbl = new JLabel("TOTAL PATIENTS");
        JLabel totalCount = new JLabel("67");
        JLabel todayLbl = new JLabel("TODAY'S PATIENT");
        JLabel todayCount = new JLabel("67");
        JLabel tAppointmentLbl = new JLabel("TODAY'S APPOINTMENT");
        JLabel tAppointmentCount = new JLabel("67");

        JPanel middleContainer = new JPanel();
        JPanel leftGlueContainer = new JPanel();
        JPanel graphMainContainer = new JPanel();
        JPanel graphContainer1 = new JPanel();
        JPanel graphContainer2 = new JPanel();
        JPanel graphContainer3 = new JPanel();
        JPanel graphContainer4 = new JPanel();
        JPanel graphContainer5 = new JPanel();

        middleContainer.setLayout(new BoxLayout(middleContainer, BoxLayout.X_AXIS));
        leftGlueContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        graphMainContainer.setLayout(new BoxLayout(graphMainContainer, BoxLayout.Y_AXIS));
        graphContainer1.setLayout(new BoxLayout(graphContainer1, BoxLayout.X_AXIS));
        graphContainer2.setLayout(new BoxLayout(graphContainer2, BoxLayout.X_AXIS));
        graphContainer3.setLayout(new BoxLayout(graphContainer3, BoxLayout.X_AXIS));
        graphContainer4.setLayout(new BoxLayout(graphContainer4, BoxLayout.X_AXIS));
        graphContainer5.setLayout(new BoxLayout(graphContainer5, BoxLayout.X_AXIS));

        graphContainer3.setAlignmentX(Component.LEFT_ALIGNMENT);
        graphContainer4.setAlignmentX(Component.LEFT_ALIGNMENT);
        graphContainer5.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel graphLbl = new JLabel("PATIENT SUMMARY");
        

        JPanel donutPanel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int size = 250;
                int holeSize = (int)(size * 0.4);

                int x = 0;
                int y = 0;

                g2.setColor(Color.decode("#6455CF"));
                g2.fillArc(x, y, size, size, 90, 180);
                g2.setColor(Color.decode("#24E71D"));
                g2.fillArc(x, y, size, size, 360, 90);
                g2.setColor(Color.decode("#DCFF4E"));
                g2.fillArc(x, y, size, size, 270, 90);

                int holeX = x + (size - holeSize) / 2;
                int holeY = y + (size - holeSize) / 2;
                g2.setColor(getBackground());
                g2.fillOval(holeX, holeY, holeSize, holeSize);
            }
        };  
        donutPanel.setPreferredSize(new Dimension(270,250));
        donutPanel.setOpaque(false);

        JLabel dataLbl1 = new JLabel("New Patients");
        JLabel dataLbl2 = new JLabel("Old Patients");
        JLabel dataLbl3 = new JLabel("Total Patients");
        
        int dSize = 40;
        BufferedImage d1 = ImageIO.read(new File("res/green.png"));
        BufferedImage d1Resized = resizeImage(d1, dSize, dSize);
        JLabel d1Lbl = new JLabel(new ImageIcon(d1Resized));

        BufferedImage d2 = ImageIO.read(new File("res/yellow.png"));
        BufferedImage d2Resized = resizeImage(d2, dSize, dSize);
        JLabel d2Lbl = new JLabel(new ImageIcon(d2Resized));
        
        BufferedImage d3 = ImageIO.read(new File("res/purple.png"));
        BufferedImage d3Resized = resizeImage(d3, dSize, dSize);
        JLabel d3Lbl = new JLabel(new ImageIcon(d3Resized));

        JPanel next = new JPanel();
        next.setLayout(new BoxLayout(next, BoxLayout.Y_AXIS));

        //Calendar

        int calendarWidth = 450;
        int calendarHeight = 275;

        CalendarPanel calendarPanel = new dashboard().new CalendarPanel();
        calendarPanel.setPreferredSize(new Dimension(calendarWidth, calendarHeight));
        calendarPanel.setMinimumSize(new Dimension(calendarWidth, calendarHeight));
        calendarPanel.setMaximumSize(new Dimension(calendarWidth, calendarHeight));
        
        //make center part centered
        JPanel middleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        middleWrapper.setOpaque(false);
        middleWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE,550));

        JPanel mainBottomPanel = new JPanel();
        JPanel mainAppointmentPanel = new JPanel();
        JPanel nextPatientPanel = new JPanel();
        JPanel appointmentRequestPanel = new JPanel();

        mainBottomPanel.setLayout(new BoxLayout(mainBottomPanel, BoxLayout.X_AXIS));
        mainAppointmentPanel.setLayout(new BoxLayout(mainAppointmentPanel, BoxLayout.Y_AXIS));
        nextPatientPanel.setLayout(new BoxLayout(nextPatientPanel, BoxLayout.Y_AXIS));
        appointmentRequestPanel.setLayout(new BoxLayout(appointmentRequestPanel, BoxLayout.Y_AXIS));

        int appointmentPanelWidth = 345;
        int appointmentPanelHeight = 310;
        mainAppointmentPanel.setPreferredSize(new Dimension(appointmentPanelWidth,appointmentPanelHeight));
        mainAppointmentPanel.setMinimumSize(new Dimension(appointmentPanelWidth, appointmentPanelHeight));
        mainAppointmentPanel.setMaximumSize(new Dimension(appointmentPanelWidth, appointmentPanelHeight));

        int nextPatientPanelWidth = 380;
        int nextPatientPanelHeight = 200;
        nextPatientPanel.setPreferredSize(new Dimension(nextPatientPanelWidth,nextPatientPanelHeight));
        nextPatientPanel.setMinimumSize(new Dimension(nextPatientPanelWidth, nextPatientPanelHeight));
        nextPatientPanel.setMaximumSize(new Dimension(nextPatientPanelWidth, nextPatientPanelHeight));

        int appointmentRequestWidth = 255;
        int appointmentRequestHeight = 262;
        appointmentRequestPanel.setPreferredSize(new Dimension(appointmentRequestWidth,appointmentRequestHeight));
        appointmentRequestPanel.setMinimumSize(new Dimension(appointmentRequestWidth, appointmentRequestHeight));
        appointmentRequestPanel.setMaximumSize(new Dimension(appointmentRequestWidth, appointmentRequestHeight));

        mainAppointmentPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        nextPatientPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        appointmentRequestPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel bottomLbl1 = new JLabel("Today's Appointment");
        JLabel bottomLbl2 = new JLabel("Next Patient's Details");
        JLabel bottomLbl3 = new JLabel("Appoinment Request");

        bottomLbl1.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomLbl2.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomLbl3.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomLbl1.setForeground(Color.decode("#00C6FF"));
        bottomLbl2.setForeground(Color.decode("#00C6FF"));
        bottomLbl3.setForeground(Color.decode("#00C6FF"));

        BufferedImage patientImg1 = ImageIO.read(new File("res/dblue.png"));
        BufferedImage patientImg2 = ImageIO.read(new File("res/purple.png"));
        BufferedImage patientImg3 = ImageIO.read(new File("res/yellow.png"));

        //Today's appointment table
        String[] column = {"Patient", "Name/Diagnosis", "Time"};
        Object[][] data = {
            {new ImageIcon(roundImage(patientImg1, 40)), "<html><div style='text-align:center'>John Doe <br><span style='font-weight:normal;'>Checkup<span></html></div>", "12:00 PM"},
            {new ImageIcon(roundImage(patientImg2, 40)), "<html><div style='text-align:center'>Johny Depp <br><span style='font-weight:normal;'>Checkup<span></div></html>", "1:00 PM"},
            {new ImageIcon(roundImage(patientImg3, 40)), "<html><div style='text-align:center'>Jason Statham <br><span style='font-weight:normal;'>Checkup<span></div></html>", "5:00 PM"}
        };

        DefaultTableModel appointmentModel = new DefaultTableModel(data, column) {
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable appointmentTable = new JTable(appointmentModel);
        appointmentTable.setShowGrid(false);
        appointmentTable.setFont(new Font("Arial", Font.BOLD, 14));
        appointmentTable.setBorder(BorderFactory.createEmptyBorder());
        appointmentTable.setRowHeight(60);
        appointmentTable.setFocusable(false);

        JTableHeader appointmentTableHeader = appointmentTable.getTableHeader();

        appointmentTable.setBackground(Color.decode("#CBE9F4"));
        appointmentTable.setOpaque(false);
        appointmentTableHeader.setOpaque(true);
        appointmentTableHeader.setBackground(Color.decode("#CBE9F4"));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        appointmentTableHeader.setDefaultRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = (JLabel) headerRenderer
                .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            lbl.setFont(new Font("Arial", Font.BOLD, 18));
            lbl.setBorder(BorderFactory.createEmptyBorder());
            lbl.setBackground(Color.WHITE);

            return lbl;
        });

        appointmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel appointmentColumn = appointmentTable.getColumnModel();
        appointmentColumn.getColumn(0).setPreferredWidth(65);
        appointmentColumn.getColumn(1).setPreferredWidth(190);
        appointmentColumn.getColumn(2).setPreferredWidth(90);


        DefaultTableCellRenderer universalRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                if (value instanceof Icon) {
                    setIcon((Icon) value);
                    setText("");
                } else {
                    setIcon(null);
                    setText(value != null ? value.toString() : "");
                }
                setHorizontalAlignment(JLabel.CENTER);
            }
        };

        for (int i = 0; i < appointmentTable.getColumnCount(); i++){
            appointmentTable.getColumnModel().getColumn(i).setCellRenderer(universalRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(true);

        //next appointment table
        BufferedImage nextAppointmentImg1 = ImageIO.read(new File("res/purple.png"));

        String[] nextAppointmentCol = {"Patient", "Name/Diagnosis", "Patient ID"};
        Object[][] nextAppointmentRows = {
            {new ImageIcon(roundImage(nextAppointmentImg1, 40)), "<html><div style='text-align:center;'>John Doe<br><span style='font-weight:normal;'>Checkup<span></div></html>", 4747582975L}
        };

        DefaultTableModel nextAppointmentModel = new DefaultTableModel(nextAppointmentRows, nextAppointmentCol) {
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable nextAppointmentTable = new JTable(nextAppointmentModel);
        nextAppointmentTable.setShowGrid(false);
        nextAppointmentTable.setFont(new Font("Arial", Font.BOLD, 14));
        nextAppointmentTable.setRowHeight(50);
        nextAppointmentTable.setFocusable(false); 

        JTableHeader nextAppointmentHeader = nextAppointmentTable.getTableHeader();

        nextAppointmentTable.setBackground(Color.decode("#CBE9F4"));
        nextAppointmentTable.setOpaque(false);
        nextAppointmentHeader.setOpaque(true);
        nextAppointmentHeader.setBackground(Color.decode("#CBE9F4"));

        nextAppointmentHeader.setDefaultRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = (JLabel) headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            lbl.setFont(new Font("Arial", Font.BOLD, 18));
            lbl.setBorder(BorderFactory.createEmptyBorder());
            lbl.setOpaque(false);

            return lbl;
        });

        nextAppointmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel nextAppointmentColumn = nextAppointmentTable.getColumnModel();
        nextAppointmentColumn.getColumn(0).setPreferredWidth(65);
        nextAppointmentColumn.getColumn(1).setPreferredWidth(190);
        nextAppointmentColumn.getColumn(2).setPreferredWidth(125);

        for (int i = 0; i < nextAppointmentTable.getColumnCount(); i++){
            nextAppointmentTable.getColumnModel().getColumn(i).setCellRenderer(universalRenderer);
        }

        JScrollPane nextAppointmentScrollPane = new JScrollPane(nextAppointmentTable);
        nextAppointmentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        nextAppointmentScrollPane.setOpaque(true);

        //Appointment Request Table
        BufferedImage appointmentRequestImg1 = ImageIO.read(new File("res/green.png"));

        String[] appointmentRequestCol = {"Patient", "Name/Diagnosis"};
        Object[][] appointmentRequestRow = {
            {new ImageIcon(roundImage(appointmentRequestImg1, 40)), "<html><div style='text-align:center;'>John Doe<br><span style='font-weight:normal;'>Checkup<span></div></html>"},
            {new ImageIcon(roundImage(appointmentRequestImg1, 40)), "<html><div style='text-align:center;'>Johnny Sins<br><span style='font-weight:normal;'>Checkup<span></div></html>"}
        };

        DefaultTableModel appointmentRequestModel = new DefaultTableModel(appointmentRequestRow, appointmentRequestCol) {
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable appointmentRequestTable = new JTable(appointmentRequestModel);
        appointmentRequestTable.setShowGrid(false);
        appointmentRequestTable.setFont(new Font("Arial", Font.BOLD, 14));
        appointmentRequestTable.setRowHeight(50);
        appointmentRequestTable.setFocusable(false); 

        JTableHeader appointmentRequestTableHeader = appointmentRequestTable.getTableHeader();
        appointmentRequestTable.setBackground(Color.decode("#CBE9F4"));
        appointmentRequestTable.setOpaque(false);
        appointmentRequestTableHeader.setOpaque(true);
        appointmentRequestTableHeader.setBackground(Color.decode("#CBE9F4"));

        appointmentRequestTableHeader.setDefaultRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = (JLabel) headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            lbl.setFont(new Font("Arial", Font.BOLD, 18));
            lbl.setBorder(BorderFactory.createEmptyBorder());
            lbl.setOpaque(false);

            return lbl;
        });

        appointmentRequestTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel appointmentRequestColumn = appointmentRequestTable.getColumnModel();
        appointmentRequestColumn.getColumn(0).setPreferredWidth(65);
        appointmentRequestColumn.getColumn(1).setPreferredWidth(190);

        for(int i = 0; i < appointmentRequestTable.getColumnCount(); i++){
            appointmentRequestTable.getColumnModel().getColumn(i).setCellRenderer(universalRenderer);
        }

        JScrollPane appointmentRequestScrollPane = new JScrollPane(appointmentRequestTable);
        appointmentRequestScrollPane.setBorder(BorderFactory.createEmptyBorder());
        appointmentRequestScrollPane.setOpaque(true);

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
        checkBoxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        checkBoxPanel.setBackground(Color.decode("#BCDDEB"));
        checkBoxPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#ADD9E6"), 2));

        BufferedImage checkImage = loadImage("res/checkBox.png");
        BufferedImage declineImage = loadImage("res/xBox.png");

        int size = 30;

        BufferedImage resizedCheck = resizeImage(checkImage, size);
        BufferedImage resizedDecline = resizeImage(declineImage, size);

        JButton checkBtn = new JButton(new ImageIcon(resizedCheck));
        JButton declineBtn = new JButton(new ImageIcon(resizedDecline));

        Dimension buttonSize = new Dimension(size, size);
        checkBtn.setPreferredSize(buttonSize);
        declineBtn.setPreferredSize(buttonSize);

        checkBtn.setBorderPainted(false);
        checkBtn.setContentAreaFilled(false);
        checkBtn.setFocusPainted(false);
        declineBtn.setBorderPainted(false);
        declineBtn.setContentAreaFilled(false);

        checkBtn.addActionListener(e -> {
            int row = appointmentRequestTable.getSelectedRow();
            if (row >= 0) {
                ((DefaultTableModel) appointmentRequestModel).removeRow(appointmentRequestTable.convertRowIndexToModel(row));
                
                notification(mainFrame, "Appointment added successfully!");
            }
        });

        declineBtn.addActionListener(e -> {
            int row = appointmentRequestTable.getSelectedRow();
            if (row >= 0) {
                ((DefaultTableModel) appointmentRequestModel)
                    .removeRow(appointmentRequestTable.convertRowIndexToModel(row));
                    notification(mainFrame, "Appointment Declined successfully!");
            }
        });

        MouseAdapter clearOthers = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Object src = e.getSource();

                if (src != appointmentTable) {
                    appointmentTable.clearSelection();
                }
                if (src != nextAppointmentTable) {
                    nextAppointmentTable.clearSelection();
                }
                if (src != appointmentRequestTable) {
                    appointmentRequestTable.clearSelection();
                }
            }
        };

        centerPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clearAllSelections(appointmentTable, nextAppointmentTable, appointmentRequestTable);
            }
        });

        appointmentTable.addMouseListener(clearOthers);
        nextAppointmentTable.addMouseListener(clearOthers);
        appointmentRequestTable.addMouseListener(clearOthers);

        checkBoxPanel.add(Box.createVerticalGlue());
        checkBoxPanel.add(checkBtn);
        checkBoxPanel.add(Box.createHorizontalStrut(-20));
        checkBoxPanel.add(declineBtn);
        checkBoxPanel.add(Box.createVerticalGlue());

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));

        bottomWrapper.add(mainBottomPanel);
        mainBottomPanel.add(mainAppointmentPanel);
        mainBottomPanel.add(Box.createRigidArea(new Dimension(35,0)));
        mainBottomPanel.add(nextPatientPanel);
        mainBottomPanel.add(Box.createRigidArea(new Dimension(35,0)));
        mainBottomPanel.add(appointmentRequestPanel);
        mainAppointmentPanel.add(bottomLbl1);
        mainAppointmentPanel.add(scrollPane);
        nextPatientPanel.add(bottomLbl2);
        nextPatientPanel.add(nextAppointmentScrollPane);
        appointmentRequestPanel.add(bottomLbl3);
        appointmentRequestPanel.add(appointmentRequestScrollPane);
        appointmentRequestPanel.add(checkBoxPanel);

        middleWrapper.add(middleContainer);
        middleContainer.add(leftGlueContainer);
        leftGlueContainer.add(graphMainContainer);
        leftGlueContainer.add(next);
        leftGlueContainer.add(calendarPanel);
        graphContainer1.add(graphLbl);
        graphContainer2.add(donutPanel);
        graphContainer3.add(d1Lbl);
        graphContainer3.add(dataLbl1);
        graphContainer4.add(d2Lbl);
        graphContainer4.add(dataLbl2);
        graphContainer5.add(d3Lbl);
        graphContainer5.add(dataLbl3);
        graphMainContainer.add(graphContainer1);
        graphMainContainer.add(graphContainer2);
        next.add(graphContainer3);
        next.add(graphContainer4);
        next.add(graphContainer5);

        textPanel1.add(totalLbl);
        textPanel1.add(totalCount);
        c1Container.add(circle1);
        c1Container.add(textPanel1);
        textPanel2.add(todayLbl);
        textPanel2.add(todayCount);
        c2Container.add(circle2);
        c2Container.add(textPanel2);
        textPanel3.add(tAppointmentLbl);
        textPanel3.add(tAppointmentCount);
        c3Container.add(circle3);
        c3Container.add(textPanel3);

        setFont("Arial", Font.BOLD, 20, totalLbl, todayLbl, tAppointmentLbl, dataLbl1, dataLbl2, dataLbl3);
        setFont("Arial", Font.ITALIC, 20, totalCount, todayCount, tAppointmentCount);
        setBorder(25, 50, 40, 15, circle1, circle2, circle3);
        setFont("Arial", Font.BOLD, 25, graphLbl);
        setBorder(0, 0, 10, 0, graphLbl);
        setBorder(50, 20, 5, 0, d1Lbl);
        setBorder(50, 10, 5, 70, dataLbl1);
        setBorder(5, 20, 5, 0, d2Lbl, d3Lbl);
        setBorder(5, 10, 5, 70, dataLbl2, dataLbl3);
        setFont("Arial", Font.BOLD, 25, bottomLbl1, bottomLbl2, bottomLbl3);
        setBorder(0, 0, 10, 0, bottomLbl1, bottomLbl2, bottomLbl3);
        setBorder(50, 0, 0, 0, mainAppointmentPanel, nextPatientPanel, appointmentRequestPanel);
        
        circleRow.add(c1Container);
        circleRow.add(c2Container);
        circleRow.add(c3Container);

        layeredPane.add(mainPanel, Integer.valueOf(0));
        //sidepanel
        layeredPane.add(sidePanel, Integer.valueOf(1));
        sidePanel.add(profileLabel, gbc(0, 0, GridBagConstraints.NORTH, 1, 0.05, GridBagConstraints.NONE));
        sideOptions.add(DIContainer);
        sideOptions.add(PIContainer);
        sideOptions.add(CIContainer);
        sideOptions.add(AIContainer);
        sidePanel.add(sideOptions, gbc(0, 1, GridBagConstraints.NORTHWEST, 1, 1, GridBagConstraints.HORIZONTAL));

        //centerpanel
        mainPanel.add(centerPanel);
        centerPanel.add(header);
        centerPanel.add(circleRow);
        centerPanel.add(middleWrapper);
        centerPanel.add(bottomWrapper);

        mainFrame.add(layeredPane);
        mainFrame.setVisible(true);
    }
}