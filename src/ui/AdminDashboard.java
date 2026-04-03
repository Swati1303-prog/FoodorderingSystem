package ui;

import dao.CustomerDAO;
import dao.MenuDAO;
import dao.OrderDAO;

import model.Customer;
import model.Order;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard - FoodieHub");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // TOP
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.setPreferredSize(new Dimension(950, 55));

        JLabel title = new JLabel("  Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topPanel.add(logoutBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // STATS PANEL
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statsPanel.setBackground(new Color(245, 245, 245));

        OrderDAO orderDAO = new OrderDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        MenuDAO menuDAO = new MenuDAO();

        List<Order> allOrders = orderDAO.getAllOrders();
        List<Customer> allCustomers = customerDAO.getAllCustomers();
        // ✅ Use full name to be clear
        List<model.Menu> allMenus = menuDAO.getAllMenus();

        double totalRevenue = 0;
        int pendingCount = 0;
        for (Order o : allOrders) {
            totalRevenue += o.getTotalAmount();
            if ("Pending".equals(o.getOrderStatus())) pendingCount++;
        }

        statsPanel.add(createStatCard("Total Orders",
            String.valueOf(allOrders.size()), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Revenue",
            "₹" + String.format("%.0f", totalRevenue), new Color(39, 174, 96)));
        statsPanel.add(createStatCard("Customers",
            String.valueOf(allCustomers.size()), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Pending",
            String.valueOf(pendingCount), new Color(231, 76, 60)));

        // TABS
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.addTab("Orders", createOrdersPanel());
        tabbedPane.addTab("Customers", createCustomersPanel());
        tabbedPane.addTab("Menu", createMenuPanel());
        tabbedPane.addTab("Add Menu Item", createAddMenuPanel());
        tabbedPane.addTab("Ratings", createRatingsPanel());

        // Center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // LOGOUT
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(Color.GRAY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(nameLabel);

        return card;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"Order ID", "Customer", "Date", "Total", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        OrderDAO dao = new OrderDAO();
        List<Order> orders = dao.getAllOrders();

        for (Order o : orders) {
            model.addRow(new Object[]{
                "#" + o.getId(),
                o.getCustomerName(),
                o.getOrderDate(),
                "₹" + String.format("%.2f", o.getTotalAmount()),
                o.getOrderStatus()
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());

        JComboBox<String> statusBox = new JComboBox<>(
            new String[]{"Pending", "Processing", "Completed", "Cancelled"});

        JButton updateBtn = new JButton("Update Status");
        updateBtn.setBackground(new Color(243, 156, 18));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBorderPainted(false);
        updateBtn.setFocusPainted(false);

        bottom.add(new JLabel("Status:"));
        bottom.add(statusBox);
        bottom.add(updateBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an order!");
                return;
            }
            String idStr = (String) model.getValueAt(row, 0);
            int orderId = Integer.parseInt(idStr.replace("#", ""));
            String status = (String) statusBox.getSelectedItem();

            if (dao.updateOrderStatus(orderId, status)) {
                model.setValueAt(status, row, 4);
                JOptionPane.showMessageDialog(this, "Status updated!");
            }
        });

        return panel;
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"ID", "Name", "Email", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        CustomerDAO dao = new CustomerDAO();
        List<Customer> customers = dao.getAllCustomers();

        for (Customer c : customers) {
            model.addRow(new Object[]{
                c.getId(),
                c.getFirstName() + " " + c.getLastName(),
                c.getEmail(),
                c.getAccountStatus()
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"ID", "Name", "Category", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        MenuDAO dao = new MenuDAO();
        // ✅ Use full name model.Menu to be clear
        List<model.Menu> menus = dao.getAllMenus();

        // ✅ Use full name model.Menu to be clear
        for (model.Menu m : menus) {
            model.addRow(new Object[]{
                m.getId(),
                m.getName(),
                m.getTypeName(),
                "₹" + String.format("%.2f", m.getPrice()),
                m.getStatus()
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Delete button
        JPanel bottom = new JPanel(new FlowLayout());
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setFocusPainted(false);
        bottom.add(deleteBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a menu item!");
                return;
            }
            int menuId = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this item?", "Confirm",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteMenu(menuId)) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Menu item deleted!");
                }
            }
        });

        return panel;
    }

    private JPanel createAddMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Add New Menu Item");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(20);
        panel.add(priceField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{
            "1 - Appetizer", "2 - Main Course",
            "3 - Dessert", "4 - Beverage"
        });
        panel.add(categoryBox, gbc);

        // Ingredients
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Ingredients:"), gbc);
        gbc.gridx = 1;
        JTextField ingredientsField = new JTextField(20);
        panel.add(ingredientsField, gbc);

        // Image URL
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx = 1;
        JTextField imageField = new JTextField(20);
        panel.add(imageField, gbc);

        // Add Button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton addBtn = new JButton("Add Menu Item");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setBackground(new Color(39, 174, 96));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String ingredients = ingredientsField.getText().trim();
            String image = imageField.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Name and Price are required!");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                String categoryStr = (String) categoryBox.getSelectedItem();
                int typeId = Integer.parseInt(
                    categoryStr.substring(0, 1));

                // ✅ Use full name model.Menu to be clear
                model.Menu menu = new model.Menu();
                menu.setName(name);
                menu.setPrice(price);
                menu.setTypeId(typeId);
                menu.setImage(image);
                menu.setIngredients(ingredients);

                MenuDAO dao = new MenuDAO();
                if (dao.addMenu(menu)) {
                    JOptionPane.showMessageDialog(this,
                        "Menu item added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    nameField.setText("");
                    priceField.setText("");
                    ingredientsField.setText("");
                    imageField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to add menu item!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Price must be a number!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createRatingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"Rating ID", "Menu Item", "Customer",
                        "Score", "Remarks", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        // Load ratings
        try {
            java.sql.Connection conn = config.DBConnection.getConnection();
            String sql = "SELECT r.id, m.name AS menu_name, " +
                        "CONCAT(c.first_name,' ',c.last_name) AS customer_name, " +
                        "r.score, r.remarks, r.date_recorded " +
                        "FROM rating r " +
                        "JOIN menu m ON r.menu_id = m.id " +
                        "JOIN customer c ON r.customer_id = c.id " +
                        "ORDER BY r.date_recorded DESC";
            java.sql.Statement st = conn.createStatement();
            java.sql.ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("menu_name"),
                    rs.getString("customer_name"),
                    rs.getInt("score") + "/5",
                    rs.getString("remarks"),
                    rs.getTimestamp("date_recorded")
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}