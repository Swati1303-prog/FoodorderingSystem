package ui;

import dao.CustomerDAO;
import model.Customer;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("🍕 FoodieHub - Register");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 107, 53));
        mainPanel.setLayout(new GridBagLayout());

        // Card Panel
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(420, 560));
        card.setLayout(null);

        // Title
        JLabel title = new JLabel("🍕 Join FoodieHub");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 15, 420, 35);
        card.add(title);

        // Fields
        int y = 60;
        int gap = 55;

        JTextField firstNameField = createField(card, "First Name", 30, y); y += gap;
        JTextField lastNameField = createField(card, "Last Name", 30, y); y += gap;
        JTextField emailField = createField(card, "Email", 30, y); y += gap;
        JTextField phoneField = createField(card, "Phone Number", 30, y); y += gap;
        JTextField usernameField = createField(card, "Username", 30, y); y += gap;

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setBounds(30, y, 360, 15);
        card.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(30, y + 18, 360, 35);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        card.add(passwordField);
        y += gap;

        // Confirm Password
        JLabel confLabel = new JLabel("Confirm Password");
        confLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confLabel.setBounds(30, y, 360, 15);
        card.add(confLabel);

        JPasswordField confirmField = new JPasswordField();
        confirmField.setBounds(30, y + 18, 360, 35);
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        card.add(confirmField);
        y += gap + 10;

        // Buttons
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setBounds(30, y, 170, 40);
        registerBtn.setBackground(new Color(255, 107, 53));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(registerBtn);

        JButton backBtn = new JButton("Back to Login");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBounds(220, y, 170, 40);
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(backBtn);

        mainPanel.add(card);
        add(mainPanel);

        // REGISTER ACTION
        registerBtn.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            // Validation
            if (firstName.isEmpty() || lastName.isEmpty() ||
                email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CustomerDAO dao = new CustomerDAO();

            if (dao.usernameExists(username)) {
                JOptionPane.showMessageDialog(this,
                    "Username already taken!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer c = new Customer();
            c.setFirstName(firstName);
            c.setLastName(lastName);
            c.setMiddleName("");
            c.setEmail(email);
            c.setPhoneNumber(phone);
            c.setLandline("");
            c.setUsername(username);
            c.setPassword(password);

            if (dao.registerCustomer(c)) {
                JOptionPane.showMessageDialog(this,
                    "Registration Successful! Please login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Registration Failed! Email may already exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // BACK BUTTON
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private JTextField createField(JPanel panel, String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setBounds(x, y, 360, 15);
        panel.add(label);

        JTextField field = new JTextField();
        field.setBounds(x, y + 18, 360, 35);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(field);

        return field;
    }
}
