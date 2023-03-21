import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Appli {

	public static void main(String[] args) {
		Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Charge le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Définit les paramètres de connexion
            String url = "jdbc:mysql://localhost:3306/mediatheque";
            String username = "root";
            String password = "root";

            // Crée une connexion
            conn = DriverManager.getConnection(url, username, password);

            // Crée un objet Statement
            stmt = conn.createStatement();

            // Exécute une requête SELECT
            rs = stmt.executeQuery("SELECT * FROM abonne");

            // Traite les résultats
            while (rs.next()) {
                int Matricule = rs.getInt("idAbonne");
                String Nom = rs.getString("nom");
                Date Age = rs.getDate("date_naissance");
                System.out.println("Matricule : " + Matricule + " | Nom : " + Nom + " | Age : " + Age);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Ferme la connexion, l'objet Statement et l'objet ResultSet
            try { rs.close(); } catch (Exception e) {}
            try { stmt.close(); } catch (Exception e) {}
            try { conn.close(); } catch (Exception e) {}
        }

	}

}
