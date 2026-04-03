package ui;

import dao.MenuDAO;
import model.Customer;
import model.Menu;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private Customer loggedInCustomer;
    private JTable menuTable;
    private DefaultTableModel tableModel;

    // Cart storage
    private List<Object[]> cart = new ArrayList<>();

    public MenuFrame(Customer customer) {
        this.loggedInCustomer = customer;

        setTitle("🍽️ FoodieHub - Menu | Welcome " + customer.getFirstName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 107, 53));
        topPanel.setPreferredSize(new Dimension(900, 60));

        JLabel welcomeLabel = new JLabel("  🍕 Welcome, " +
            customer.getFirstName() + " " + customer.getLastName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        // Top Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        JButton cartBtn = createTopButton("🛒 Cart");
        JButton ordersBtn = createTopButton("📋 Orders");
        JButton logoutBtn = createTopButton("🚪 Logout");

        btnPanel.add(cartBtn);
        btnPanel.add(ordersBtn);
        btnPanel.add(logoutBtn);
        topPanel.add(btnPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== MENU TABLE =====
        String[] columns = {"ID", "Name", "Category", "Price (₹)","Calories",
                           "Ingredients", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        menuTable = new JTable(tableModel);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuTable.setRowHeight(35);
        menuTable.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14));
        menuTable.getTableHeader().setBackground(new Color(44, 62, 80));
        menuTable.getTableHeader().setForeground(Color.WHITE);
        menuTable.setSelectionBackground(new Color(255, 235, 220));

        loadMenuData();

        JScrollPane scrollPane = new JScrollPane(menuTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JSpinner qtySpinner = new JSpinner(
            new SpinnerNumberModel(1, 1, 20, 1));
        qtySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton addToCartBtn = new JButton("🛒 Add to Cart");
        addToCartBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addToCartBtn.setBackground(new Color(39, 174, 96));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.setBorderPainted(false);
        addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);

        bottomPanel.add(qtyLabel);
        bottomPanel.add(qtySpinner);
        bottomPanel.add(addToCartBtn);
        bottomPanel.add(refreshBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====

        // ADD TO CART
        addToCartBtn.addActionListener(e -> {
            int selectedRow = menuTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a menu item first!",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int menuId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            double price = (double) tableModel.getValueAt(selectedRow, 3);
            int qty = (int) qtySpinner.getValue();

            // Check if already in cart
            boolean found = false;
            for (Object[] item : cart) {
                if ((int) item[0] == menuId) {
                    item[3] = (int) item[3] + qty;
                    item[4] = price * (int) item[3];
                    found = true;
                    break;
                }
            }

            if (!found) {
                cart.add(new Object[]{
                    menuId, name, price, qty, price * qty
                });
            }

            JOptionPane.showMessageDialog(this,
                name + " x" + qty + " added to cart!",
                "Added", JOptionPane.INFORMATION_MESSAGE);

            qtySpinner.setValue(1);
        });

        // VIEW CART
        cartBtn.addActionListener(e -> {
            dispose();
            new CartFrame(loggedInCustomer, cart);
        });

        // VIEW ORDERS
        ordersBtn.addActionListener(e -> {
            dispose();
            new OrderFrame(loggedInCustomer);
        });

        // LOGOUT
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        // REFRESH
        refreshBtn.addActionListener(e -> loadMenuData());

        setVisible(true);
    }

    private void loadMenuData() {
        tableModel.setRowCount(0);
        MenuDAO menuDAO = new MenuDAO();
        List<model.Menu> menus = menuDAO.getAllMenus();

        for (model.Menu m :menus) {
            tableModel.addRow(new Object[]{
                m.getId(),
                m.getName(),
                m.getTypeName(),
                m.getPrice(),
                m.getCalories(),
                m.getIngredients(),
                m.getStatus()
            });
        }
    }

    private JButton createTopButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 107, 53));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
