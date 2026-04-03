package dao;

import config.DBConnection;
import model.Order;
import model.OrderDetails;
import java.sql.*;
import java.util.*;

public class OrderDAO {

    // PLACE NEW ORDER - returns order id
    public int placeOrder(Order o) {
        String sql = "INSERT INTO orders (customer_id,total_amount,order_status)" +
                     " VALUES (?,?,'Pending') RETURNING id";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, o.getCustomerId());
            ps.setDouble(2, o.getTotalAmount());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return -1;
    }

    // ADD ORDER DETAILS
    public boolean addOrderDetails(OrderDetails d) {
        String sql = "INSERT INTO order_details " +
                     "(order_id,menu_id,amount,no_of_serving,total_amount)" +
                     " VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, d.getOrderId());
            ps.setInt(2, d.getMenuId());
            ps.setDouble(3, d.getAmount());
            ps.setInt(4, d.getNoOfServing());
            ps.setDouble(5, d.getTotalAmount());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    // GET ORDERS BY CUSTOMER
    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id=? " +
                     "ORDER BY order_date DESC";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setCustomerId(rs.getInt("customer_id"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setOrderStatus(rs.getString("order_status"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return list;
    }

    // GET ALL ORDERS (Admin)
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, " +
                     "CONCAT(c.first_name,' ',c.last_name) AS customer_name " +
                     "FROM orders o " +
                     "JOIN customer c ON o.customer_id=c.id " +
                     "ORDER BY o.order_date DESC";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setCustomerId(rs.getInt("customer_id"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setOrderStatus(rs.getString("order_status"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return list;
    }

    // GET ORDER DETAILS
    public List<OrderDetails> getOrderDetails(int orderId) {
        List<OrderDetails> list = new ArrayList<>();
        String sql = "SELECT od.*,m.name AS menu_name FROM order_details od " +
                     "JOIN menu m ON od.menu_id=m.id WHERE od.order_id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetails d = new OrderDetails();
                d.setId(rs.getInt("id"));
                d.setOrderId(rs.getInt("order_id"));
                d.setMenuId(rs.getInt("menu_id"));
                d.setMenuName(rs.getString("menu_name"));
                d.setAmount(rs.getDouble("amount"));
                d.setNoOfServing(rs.getInt("no_of_serving"));
                d.setTotalAmount(rs.getDouble("total_amount"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return list;
    }

    // UPDATE ORDER STATUS (Admin)
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status=? WHERE id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
