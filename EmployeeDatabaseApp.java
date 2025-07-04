import java.sql.*;
import java.util.Scanner;

public class EmployeeDatabaseApp {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/companydb";
    private static final String USER = "marco";
    private static final String PASSWORD = "your_password_here";

    private Connection conn;


    public EmployeeDatabaseApp() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public void addEmployee(String name, String department, double salary) throws SQLException {
        String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, department);
            stmt.setDouble(3, salary);
            stmt.executeUpdate();
            System.out.println("Employee added successfully.");
        }
    }


    public void listEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Department: %s | Salary: %.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        }
    }


    public void updateEmployee(int id, String name, String department, double salary) throws SQLException {
        String sql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, department);
            stmt.setDouble(3, salary);
            stmt.setInt(4, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        }
    }


    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }


    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            EmployeeDatabaseApp app = new EmployeeDatabaseApp();
            while (true) {
                System.out.println("\nEmployee Database Menu:");
                System.out.println("1. Add Employee");
                System.out.println("2. List Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Department: ");
                        String dept = scanner.nextLine();
                        System.out.print("Enter Salary: ");
                        double salary = scanner.nextDouble();
                        app.addEmployee(name, dept, salary);
                        break;
                    case 2:
                        app.listEmployees();
                        break;
                    case 3:
                        System.out.print("Enter Employee ID to Update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter New Name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter New Department: ");
                        String newDept = scanner.nextLine();
                        System.out.print("Enter New Salary: ");
                        double newSalary = scanner.nextDouble();
                        app.updateEmployee(updateId, newName, newDept, newSalary);
                        break;
                    case 4:
                        System.out.print("Enter Employee ID to Delete: ");
                        int deleteId = scanner.nextInt();
                        app.deleteEmployee(deleteId);
                        break;
                    case 5:
                        app.close();
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
