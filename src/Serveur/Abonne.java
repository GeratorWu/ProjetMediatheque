package Serveur;

import java.sql.*;

public class Abonne {
	private Integer idAbonne;
	private String nom;
	private String prenom;
	private Date dateNaissance;
	private Connection conn;
	Statement stmt = null;
    ResultSet rs = null;
	
	public Abonne(Connection conn,Integer idAbonne) {
		this.conn = conn;
		this.idAbonne = idAbonne;
	}
	
	public Integer getIdAbonne() {
		return this.idAbonne;
	}
	
	public String getNom() {
		return this.nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public Date getDateNaissance() {
		return this.dateNaissance;
	}


	public String toString() {
		String s = "";
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM abonne WHERE idAbonne = ?";
			PreparedStatement req = conn.prepareStatement(sql);
			req.setInt(1, idAbonne);
			rs = req.executeQuery();
			while(rs.next()) {
				String prenom = rs.getString("prenom");
				s = s+("Bonjour " + prenom);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
}
