package ui;

import dao.OrderDAO;
import model.Customer;
import model.Order;
import model.OrderDetails;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartFrame extends JFrame {

    private Customer customer;
    private List<Object[]> cart;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public CartFrame(Customer customer, List<Object[]> cart) {
        this.customer = customer;
        this.cart = cart;

        setTitle("🛒 Cart - FoodieHub");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // TOP
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 107, 53));
        topPanel.setPreferredSize(new Dimension(700, 50));

        JLabel titleLabel = new JLabel("  🛒 Your Cart");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Menu ID", "Item Name", "Price", "Qty", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable cartTable = new JTable(tableModel);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartTable.setRowHeight(30);
        cartTable.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14));
        cartTable.getTableHeader().setBackground(new Color(44, 62, 80));
        cartTable.getTableHeader().setForeground(Color.WHITE);

        loadCartData();

        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // BOTTOM
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(new Color(245, 245, 245));

        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(255, 107, 53));

        JButton removeBtn = new JButton("❌ Remove Selected");
        removeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        removeBtn.setBackground(new Color(231, 76, 60));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFocusPainted(false);
        removeBtn.setBorderPainted(false);

        JButton placeOrderBtn = new JButton("✅ Place Order");
        placeOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        placeOrderBtn.setBackground(new Color(39, 174, 96));
        placeOrderBtn.setForeground(Color.WHITE);
        placeOrderBtn.setFocusPainted(false);
        placeOrderBtn.setBorderPainted(false);

        JButton backBtn = new JButton("⬅ Back to Menu");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);

        bottomPanel.add(totalLabel);
        bottomPanel.add(removeBtn);
        bottomPanel.add(placeOrderBtn);
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        updateTotal();

        // REMOVE
        removeBtn.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Select an item to remove!");
                return;
            }
            cart.remove(row);
            loadCartData();
            updateTotal();
        });

        // PLACE ORDER
        placeOrderBtn.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Cart is empty!", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Place this order?", "Confirm Order",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                OrderDAO orderDAO = new OrderDAO();

                // Calculate total
                double total = 0;
                for (Object[] item : cart) {
                    total += (double) item[4];
                }

                // Create order
                Order order = new Order();
                order.setCustomerId(customer.getId());
                order.setTotalAmount(total);

                int orderId = orderDAO.placeOrder(order);

                if (orderId > 0) {
                    // Add order details
                    for (Object[] item : cart) {
                        OrderDetails details = new OrderDetails();
                        details.setOrderId(orderId);
                        details.setMenuId((int) item[0]);
                        details.setAmount((double) item[2]);
                        details.setNoOfServing((int) item[3]);
                        details.setTotalAmount((double) item[4]);
                        orderDAO.addOrderDetails(details);
                    }

                    JOptionPane.showMessageDialog(this,
                        "Order placed successfully!\nOrder ID: #" + orderId,
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                    cart.clear();
                    loadCartData();
                    updateTotal();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Order failed!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // BACK
        backBtn.addActionListener(e -> {
            dispose();
            new MenuFrame(customer);
        });

        setVisible(true);
    }

    private void loadCartData() {
        tableModel.setRowCount(0);
        for (Object[] item : cart) {
            tableModel.addRow(item);
        }
    }

    private void updateTotal() {
        double total = 0;
        for (Object[] item : cart) {
            total += (double) item[4];
        }
        totalLabel.setText(String.format("Total: ₹%.2f", total));
    }
}