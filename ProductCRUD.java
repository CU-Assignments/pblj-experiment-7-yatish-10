import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name"; // Change to your database URL
    private static final String USER = "your_username"; // Change to your database username
    private static final String PASS = "your_password"; // Change to your database password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Menu:");
            System.out.println("1. Create Product");
            System.out.println("2. Read Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createProduct(scanner);
                    break;
                case 2:
                    readProduct(scanner);
                    break;
                case 3:
                    updateProduct(scanner);
                    break;
                case 4:
                    deleteProduct(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void createProduct(Scanner scanner) {
        System.out.print("Enter Product Name: ");
        String name = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false); // Start transaction
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            conn.commit(); // Commit transaction
            System.out.println("Product created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating product: " + e.getMessage());
        }
    }

    private static void readProduct(Scanner scanner) {
        System.out.print("Enter Product ID to read: ");
        int id = scanner.nextInt();

        String query = "SELECT * FROM Product WHERE ProductID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Product ID: " + rs.getInt("ProductID"));
                System.out.println("Product Name: " + rs.getString("ProductName"));
                System.out.println("Price: " + rs.getDouble("Price"));
                System.out.println("Quantity: " + rs.getInt("Quantity"));
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error reading product: " + e.getMessage());
        }
    }

    private static void updateProduct(Scanner scanner) {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new Product Name: ");
        String name = scanner.next();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();

        String query = "UPDATE Product SET ProductName = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false); // Start transaction
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, id);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit(); // Commit transaction
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.print("Enter
