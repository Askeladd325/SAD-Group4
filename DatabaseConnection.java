import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/SAD_project";
    private static final String user = "root";
    private static final String password = "";

    private static Connection connection;

    public static Connection getConnection(){
        if(connection == null){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database!");
            } catch(ClassNotFoundException e){
                System.out.println("JDBC Driver not found: " + e.getMessage());
            } catch(SQLException e){
                System.out.println("Database connection error: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Database connection closed.");
            } 
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}