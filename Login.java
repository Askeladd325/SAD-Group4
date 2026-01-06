import java.awt.*;
import javax.swing.*;

public class Login {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#56C7D1"));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#AEDCEB"), 2, true));

        JPanel insidePanel = new JPanel();
        insidePanel.setLayout(new BoxLayout(insidePanel, BoxLayout.Y_AXIS));
        insidePanel.setOpaque(false);

        JLabel header = new JLabel("LOGIN");
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        JPanel inputPanel1 = new JPanel();
        JPanel inputPanel2 = new JPanel();
        inputPanel1.setLayout(new BoxLayout(inputPanel1, BoxLayout.X_AXIS));
        inputPanel2.setLayout(new BoxLayout(inputPanel2, BoxLayout.X_AXIS));
        inputPanel1.setOpaque(false);
        inputPanel2.setOpaque(false);
        inputPanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JLabel lbl1 = new JLabel("Username: ");
        JLabel lbl2 = new JLabel("Password: ");

        JTextField usernameTxt = new JTextField(15);
        JTextField passwordTxt = new JPasswordField(15);
        usernameTxt.setBackground(Color.decode("#CBE9F4"));
        passwordTxt.setBackground(Color.decode("#CBE9F4"));

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton loginBtn = new JButton("login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 22));

        loginBtn.addActionListener(e -> {
            String username = usernameTxt.getText();
            String password = passwordTxt.getText();
            if (Queries.login(username, password)) {
                try {
                    dashboard.main(new String[] {});
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Wrong username or password", "Wrong Credential",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        insidePanel.add(header);
        inputPanel1.add(lbl1);
        inputPanel2.add(lbl2);
        inputPanel1.add(usernameTxt);
        inputPanel2.add(passwordTxt);
        btnPanel.add(loginBtn);
        insidePanel.add(inputPanel1);
        insidePanel.add(inputPanel2);
        insidePanel.add(btnPanel);
        mainPanel.add(insidePanel);
        frame.setContentPane(mainPanel);

        frame.setVisible(true);
    }
}