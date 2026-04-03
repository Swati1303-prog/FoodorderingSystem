import config.DBConnection;
import dao.CustomerDAO;
import dao.MenuDAO;
import java.sql.Connection;
import java.util.List;
import model.Customer;
import model.Menu;

public class TestDB {
    public static void main(String[] args) {

        // TEST 1: Connection
        System.out.println("=== TEST 1: Connection ===");
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Connected to PostgreSQL!");
            DBConnection.closeConnection(conn);
        } else {
            System.out.println("Connection FAILED!");
            return;
        }

        // TEST 2: Fetch all menus
        System.out.println("\n=== TEST 2: Fetch Menus ===");
        MenuDAO menuDAO = new MenuDAO();
        List<Menu> menus = menuDAO.getAllMenus();
        for (Menu m : menus) {
            System.out.println(m.getId() + " | " +
                             m.getName() + " | $" +
                             m.getPrice() + " | " +
                             m.getTypeName());
        }

        // TEST 3: Register a customer
        System.out.println("\n=== TEST 3: Register Customer ===");
        CustomerDAO customerDAO = new CustomerDAO();

        Customer newCustomer = new Customer();
        newCustomer.setFirstName("swati");
        newCustomer.setLastName("mishra");
        newCustomer.setMiddleName("");
        newCustomer.setEmail("swati@test.com");
        newCustomer.setPhoneNumber("1234567890");
        newCustomer.setLandline("");
        newCustomer.setUsername("swati123");
        newCustomer.setPassword("pass123");

        boolean registered = customerDAO.registerCustomer(newCustomer);
        System.out.println("Registration: " +
            (registered ? "SUCCESS" : "FAILED"));

        // TEST 4: Login
        System.out.println("\n=== TEST 4: Login ===");
        Customer loggedIn = customerDAO.loginCustomer("swati123", "pass123");
        if (loggedIn != null) {
            System.out.println("Login SUCCESS!");
            System.out.println("Welcome " + loggedIn.getFirstName() +
                             " " + loggedIn.getLastName());
        } else {
            System.out.println("Login FAILED!");
        }

        System.out.println("\n=== ALL TESTS DONE ===");
    }
}
