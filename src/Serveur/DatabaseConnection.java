package Serveur;
import java.sql.*;

public class DatabaseConnection { // Connexion � la base de donn�es.
	private Connection connection;

	public DatabaseConnection() { 
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        String url = "jdbc:mysql://localhost:3306/mediatheque";
	        String username = "root";
	        String password = "root";

	        connection = DriverManager.getConnection(url, username, password);

	        System.out.println("Connexion � la base de donn�es r�ussie.");

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
	        System.out.println("Connexion ferm�e.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
