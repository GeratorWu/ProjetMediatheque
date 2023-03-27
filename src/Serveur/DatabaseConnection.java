package Serveur;
import java.sql.*;

public class DatabaseConnection {
	private Connection connection;

	public DatabaseConnection() {
	    try {
	        // Charge le driver MySQL
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        // Définit les paramètres de connexion
	        String url = "jdbc:mysql://localhost:3306/mediatheque";
	        String username = "root";
	        String password = "root";

	        // Crée une connexion
	        connection = DriverManager.getConnection(url, username, password);

	        System.out.println("Connexion à la base de données réussie.");

	    } catch (ClassNotFoundException ex) {
	        System.out.println("Driver not found");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	public Connection getConnection() {
	    return connection;
	}

	public void closeConnection() {
	    try {
	        connection.close();
	        System.out.println("Connexion fermée.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
