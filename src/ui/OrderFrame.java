package ui;

import dao.OrderDAO;
import model.Customer;
import model.Order;
import model.OrderDetails;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderFrame extends JFrame {

    public OrderFrame(Customer customer) {
        setTitle("My Orders - FoodieHub");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load orders first (we need this data for summary)
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getOrdersByCustomer(customer.getId());

        // ====================================================
        // STEP 7: Calculate total lifetime expense and calories
        // ====================================================
        double totalSpent = 0;
        double totalCaloriesAll = 0;

        for (Order o : orders) {
            totalSpent += o.getTotalAmount();

            List<OrderDetails> details =
                orderDAO.getOrderDetails(o.getId());

            for (OrderDetails d : details) {
                int calories = 0;
                try {
                    java.sql.Connection conn =
                        config.DBConnection.getConnection();
                    String sql = "SELECT calories FROM menu WHERE id=?";
                    java.sql.PreparedStatement ps =
                        conn.prepareStatement(sql);
                    ps.setInt(1, d.getMenuId());
                    java.sql.ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        calories = rs.getInt("calories");
                    }
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                totalCaloriesAll += calories * d.getNoOfServing();
            }
        }

        // ===== TOP PANEL (Title + Summary) =====
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        // Title bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 107, 53));
        topPanel.setPreferredSize(new Dimension(850, 50));
        topPanel.setMaximumSize(new Dimension(2000, 50));

        JLabel title = new JLabel("  My Orders");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        topContainer.add(topPanel);

        // ====================================================
        // STEP 7: Summary Panel showing total expense + calories
        // ====================================================
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        summaryPanel.setMaximumSize(new Dimension(2000, 80));

        // Card 1: Total Orders
        JPanel ordersCard = createSummaryCard(
            "Total Orders",
            String.valueOf(orders.size()),
            new Color(52, 152, 219)
        );

        // Card 2: Total Money Spent
        JPanel spentCard = createSummaryCard(
            "Total Spent",
            "₹" + String.format("%.2f", totalSpent),
            new Color(39, 174, 96)
        );

        // Card 3: Total Calories
        String calWarning = "";
        if (totalCaloriesAll > 5000) {
            calWarning = " ⚠️";
        }
        JPanel caloriesCard = createSummaryCard(
            "Total Calories",
            (int) totalCaloriesAll + " cal" + calWarning,
            new Color(231, 76, 60)
        );

        summaryPanel.add(ordersCard);
        summaryPanel.add(spentCard);
        summaryPanel.add(caloriesCard);

        topContainer.add(summaryPanel);

        add(topContainer, BorderLayout.NORTH);

        // ===== ORDERS TABLE =====
        String[] columns = {"Order ID", "Date", "Total (₹)", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable orderTable = new JTable(model);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orderTable.setRowHeight(30);
        orderTable.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14));
        orderTable.getTableHeader().setBackground(new Color(44, 62, 80));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.setSelectionBackground(new Color(255, 235, 220));
        orderTable.setGridColor(new Color(230, 230, 230));

        // Fill table with orders
        for (Order o : orders) {
            model.addRow(new Object[]{
                "#" + o.getId(),
                o.getOrderDate(),
                "₹" + String.format("%.2f", o.getTotalAmount()),
                o.getOrderStatus()
            });
        }

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewDetailsBtn.setBackground(new Color(52, 152, 219));
        viewDetailsBtn.setForeground(Color.WHITE);
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.setBorderPainted(false);
        viewDetailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton backBtn = new JButton("Back to Menu");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(viewDetailsBtn);
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== VIEW DETAILS ACTION =====
        viewDetailsBtn.addActionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select an order first!",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String orderIdStr = (String) model.getValueAt(row, 0);
            int orderId = Integer.parseInt(orderIdStr.replace("#", ""));

            List<OrderDetails> details = orderDAO.getOrderDetails(orderId);

            if (details.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No details found!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Order #").append(orderId).append("\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

            double orderCalories = 0;
            double orderTotal = 0;

            for (OrderDetails d : details) {

                // Get calories from menu table
                int calories = 0;
                try {
                    java.sql.Connection conn =
                        config.DBConnection.getConnection();
                    String calSql =
                        "SELECT calories FROM menu WHERE id=?";
                    java.sql.PreparedStatement ps =
                        conn.prepareStatement(calSql);
                    ps.setInt(1, d.getMenuId());
                    java.sql.ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        calories = rs.getInt("calories");
                    }
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                int itemCalories = calories * d.getNoOfServing();
                orderCalories += itemCalories;
                orderTotal += d.getTotalAmount();

                sb.append(d.getMenuName())
                  .append("\n")
                  .append("  Qty: ").append(d.getNoOfServing())
                  .append("  |  Price: ₹")
                  .append(String.format("%.2f", d.getAmount()))
                  .append("  |  Subtotal: ₹")
                  .append(String.format("%.2f", d.getTotalAmount()))
                  .append("\n")
                  .append("  Calories: ")
                  .append(itemCalories)
                  .append(" cal")
                  .append("\n\n");
            }

            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("Order Total:    ₹")
              .append(String.format("%.2f", orderTotal))
              .append("\n");
            sb.append("Order Calories: ")
              .append((int) orderCalories)
              .append(" cal\n");

            // Health tip
            if (orderCalories > 1000) {
                sb.append("\n⚠️ High calorie order! Consider lighter options next time.");
            } else {
                sb.append("\n✅ Good calorie balance!");
            }

            JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Order #" + orderId + " Details",
                JOptionPane.INFORMATION_MESSAGE);
        });

        // ===== BACK ACTION =====
        backBtn.addActionListener(e -> {
            dispose();
            new MenuFrame(customer);
        });

        setVisible(true);
    }

    // ====================================================
    // Helper method to create summary cards
    // ====================================================
    private JPanel createSummaryCard(String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(Color.GRAY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(nameLabel);

        return card;
    }
}