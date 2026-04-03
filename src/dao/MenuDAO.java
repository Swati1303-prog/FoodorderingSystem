package dao;

import config.DBConnection;
import model.Menu;
import java.sql.*;
import java.util.*;

public class MenuDAO {

    // GET ALL AVAILABLE MENUS
    public List<Menu> getAllMenus() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT m.*,mt.type_name FROM menu m " +
                     "JOIN menu_type mt ON m.type_id=mt.id " +
                     "WHERE m.status='Available'";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Menu m = new Menu();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setPrice(rs.getDouble("price"));
                m.setTypeId(rs.getInt("type_id"));
                m.setTypeName(rs.getString("type_name"));
                m.setImage(rs.getString("image"));
                m.setIngredients(rs.getString("ingredients"));
                m.setStatus(rs.getString("status"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return list;
    }

    // GET MENU BY ID
    public Menu getMenuById(int id) {
        String sql = "SELECT m.*,mt.type_name FROM menu m " +
                     "JOIN menu_type mt ON m.type_id=mt.id WHERE m.id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Menu m = new Menu();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setPrice(rs.getDouble("price"));
                m.setTypeId(rs.getInt("type_id"));
                m.setTypeName(rs.getString("type_name"));
                m.setIngredients(rs.getString("ingredients"));
                m.setStatus(rs.getString("status"));
                m.setCalories(rs.getInt("calories"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    // ADD NEW MENU (Admin)
    public boolean addMenu(Menu m) {
        String sql = "INSERT INTO menu (name,price,type_id,image,ingredients,status)" +
                     " VALUES (?,?,?,?,?,'Available')";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, m.getName());
            ps.setDouble(2, m.getPrice());
            ps.setInt(3, m.getTypeId());
            ps.setString(4, m.getImage());
            ps.setString(5, m.getIngredients());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    // DELETE MENU (Admin - soft delete)
    public boolean deleteMenu(int id) {
        String sql = "UPDATE menu SET status='Unavailable' WHERE id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    // GET AVERAGE RATING
    public double getAverageRating(int menuId) {
        String sql = "SELECT COALESCE(AVG(score),0) FROM rating WHERE menu_id=?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, menuId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return 0;
    }
}
