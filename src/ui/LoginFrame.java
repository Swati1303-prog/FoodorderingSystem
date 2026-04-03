package ui;

import dao.CustomerDAO;
import model.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> loginTypeBox;

    public LoginFrame() {
        setTitle("🍕 FoodieHub - Login");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 107, 53));
        mainPanel.setLayout(new GridBagLayout());

        // Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(350, 400));
        cardPanel.setLayout(null);

        // Title
        JLabel titleLabel = new JLabel("🍕 FoodieHub");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, 350, 40);
        cardPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Welcome back!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBounds(0, 55, 350, 25);
        cardPanel.add(subtitleLabel);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setBounds(40, 100, 270, 20);
        cardPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBounds(40, 125, 270, 40);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cardPanel.add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setBounds(40, 180, 270, 20);
        cardPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBounds(40, 205, 270, 40);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cardPanel.add(passwordField);

        // Login Type
        JLabel typeLabel = new JLabel("Login as:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLabel.setBounds(40, 260, 270, 20);
        cardPanel.add(typeLabel);

        loginTypeBox = new JComboBox<>(new String[]{"Customer", "Admin"});
        loginTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginTypeBox.setBounds(40, 285, 270, 35);
        cardPanel.add(loginTypeBox);

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBounds(40, 340, 130, 40);
        loginBtn.setBackground(new Color(255, 107, 53));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPanel.add(loginBtn);

        // Register Button
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerBtn.setBounds(180, 340, 130, 40);
        registerBtn.setBackground(new Color(39, 174, 96));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPanel.add(registerBtn);

        mainPanel.add(cardPanel);
        add(mainPanel);

        // LOGIN BUTTON ACTION
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String loginType = (String) loginTypeBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter username and password!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Admin".equals(loginType)) {
                if ("admin".equals(username) && "admin123".equals(password)) {
                    JOptionPane.showMessageDialog(this,
                        "Admin Login Successful!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new AdminDashboard();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Invalid admin credentials!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                CustomerDAO dao = new CustomerDAO();
                Customer customer = dao.loginCustomer(username, password);

                if (customer != null) {
                    JOptionPane.showMessageDialog(this,
                        "Welcome " + customer.getFirstName() + "!",
                        "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MenuFrame(customer);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Invalid username or password!",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // REGISTER BUTTON ACTION
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }

    // MAIN METHOD - START THE APP
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}