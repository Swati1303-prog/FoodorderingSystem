package dao;

import config.DBConnection;
import model.Customer;
import java.sql.*;
import java.util.*;

public class CustomerDAO {

    // REGISTER NEW CUSTOMER
    public boolean registerCustomer(Customer c) {
        String sql = "INSERT INTO customer " +
            "(first_name,last_name,middle_name,email," +
            "phone_number,landline,username,password,account_status)" +
            " VALUES (?,?,?,?,?,?,?,?,'Active')";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getMiddleName());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhoneNumber());
            ps.setString(6, c.getLandline());
            ps.setString(7, c.getUsername());
            ps.setString(8, c.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    // LOGIN CUSTOMER
    public Customer loginCustomer(String username, String password) {
        String sql = "SELECT * FROM customer WHERE username=? " +
                     "AND password=? AND account_status='Active'";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFirstName(rs.getString("first_name"));
                c.setLastName(rs.getString("last_name"));
                c.setMiddleName(rs.getString("middle_name"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setLandline(rs.getString("landline"));
                c.setUsername(rs.getString("username"));
                c.setPassword(rs.getString("password"));
                c.setAccountStatus(rs.getString("account_status"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    // CHECK IF USERNAME EXISTS
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM customer WHERE username=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    // UPDATE CUSTOMER PROFILE
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customer SET first_name=?,last_name=?," +
                     "middle_name=?,email=?,phone_number=?,landline=? WHERE id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getMiddleName());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhoneNumber());
            ps.setString(6, c.getLandline());
            ps.setInt(7, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    // GET ALL CUSTOMERS (Admin)
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFirstName(rs.getString("first_name"));
                c.setLastName(rs.getString("last_name"));
                c.setEmail(rs.getString("email"));
                c.setAccountStatus(rs.getString("account_status"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return list;
    }
}
