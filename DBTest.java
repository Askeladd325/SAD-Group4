import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection SUCCESS!");
            } else {
                System.out.println("❌ Database connection FAILED.");
            }

        } catch (Exception e) {
            System.out.println("❌ Connection error:");
            e.printStackTrace();
        }
    }
}