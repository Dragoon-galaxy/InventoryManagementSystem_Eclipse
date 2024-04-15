import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class inventory {

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory?" +
                    "user=root&password=1234");

            stmt = conn.createStatement();

            while (true) {
                System.out.println("\n=== INVENTORY MANAGEMENT SYSTEM ===");
                System.out.println("1. Add Product");
                System.out.println("2. Remove Product");
                System.out.println("3. Update Product");
                System.out.println("4. Print All Products");
                System.out.println("5. Exit");

                System.out.print("Enter your choice (1-5): ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character

                switch (choice) {
                    case 1:
                        System.out.println("\n=== Add Product ===");
                        System.out.print("Enter product number: ");
                        int productNumber = scanner.nextInt();
                        scanner.nextLine();

                        // Check for duplicate product number
                        rs = stmt.executeQuery("SELECT * FROM inventoryproducts WHERE product_no = " + productNumber);
                        if (rs.next()) {
                            System.out.println("Error: Product with the given product number already exists.");
                            break;
                        }

                        System.out.print("Enter product name: ");
                        String productName = scanner.nextLine();

                        System.out.print("Enter product quantity: ");
                        int productQuantity = scanner.nextInt();
                        scanner.nextLine();

                        String insertQuery = "INSERT INTO inventoryproducts (product_no, product_name, product_quantity) " +
                                "VALUES (" + productNumber + ", '" + productName + "', " + productQuantity + ")";
                        stmt.executeUpdate(insertQuery);

                        System.out.println("Product added successfully.");
                        break;

                    case 2:
                        System.out.println("\n=== Remove Product ===");
                        System.out.print("Enter product number to remove: ");
                        int productNumberToRemove = scanner.nextInt();
                        scanner.nextLine();

                        String deleteQuery = "DELETE FROM inventoryproducts WHERE product_no = " + productNumberToRemove;
                        int rowsAffected = stmt.executeUpdate(deleteQuery);

                        if (rowsAffected > 0) {
                            System.out.println("Product removed successfully.");
                        } else {
                            System.out.println("Error: Product not found or couldn't be removed.");
                        }
                        break;

                    case 3:
                        System.out.println("\n=== Update Product ===");
                        System.out.print("Enter product number to update: ");
                        int productNumberToUpdate = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter new product name: ");
                        String newProductName = scanner.nextLine();

                        System.out.print("Enter new product quantity: ");
                        int newProductQuantity = scanner.nextInt();
                        scanner.nextLine();

                        String updateQuery = "UPDATE inventoryproducts SET product_name = '" + newProductName +
                                "', product_quantity = " + newProductQuantity +
                                " WHERE product_no = " + productNumberToUpdate;

                        int rowsUpdated = stmt.executeUpdate(updateQuery);

                        if (rowsUpdated > 0) {
                            System.out.println("Product updated successfully.");
                        } else {
                            System.out.println("Error: Product not found or couldn't be updated.");
                        }
                        break;

                    case 4:
                        System.out.println("\n=== Print All Products ===");
                        rs = stmt.executeQuery("SELECT * FROM inventoryproducts");
                        System.out.printf("%-15s%-30s%-15s\n", "Product Number", "Product Name", "Product Quantity");
                        while (rs.next()) {
                            int pro_no = rs.getInt("product_no");
                            String pro_name = rs.getString("product_name");
                            int pro_quantity = rs.getInt("product_quantity");

                            System.out.printf("%-15s%-30s%-15s\n", pro_no, pro_name, pro_quantity);
                        }
                        break;

                    case 5:
                        System.out.println("\nExiting Inventory Management System. Goodbye!");
                        System.exit(0);

                    default:
                        System.out.println("\nError: Invalid choice. Please try again.");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing database resources: " + e.getMessage());
            }
            scanner.close();
        }
    }
}
