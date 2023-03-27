package Serveur;

import java.sql.*;

public class DVD implements Document{
	private int idDvd;
	private Connection conn;
	Statement stmt = null;
    ResultSet rs = null;
	
	public DVD(Connection conn, int idDvd) {
		this.conn = conn;
		this.idDvd = idDvd;
	}

	@Override
	public int numero() {
		return this.idDvd;
	}

	@Override
	public Abonne emprunteur() {
		Abonne abo = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM dvd WHERE idDvd = ?";
			PreparedStatement req = conn.prepareStatement(sql);
			req.setInt(1, idDvd);
			rs = req.executeQuery();
			while(rs.next()) {
				Integer a = rs.getInt("emprunteur");
				if (!rs.wasNull()) {
	                abo = new Abonne(this.conn, a);
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { rs.close(); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
		
		return abo;
	}

	@Override
	public Abonne reserveur() {
		Abonne abo = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM dvd WHERE idDvd = ?";
			PreparedStatement req = conn.prepareStatement(sql);
			req.setInt(1, idDvd);
			rs = req.executeQuery();
			while(rs.next()) {
				Integer a = rs.getInt("reserveur");
				if (!rs.wasNull()) {
	                abo = new Abonne(this.conn, a);
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { rs.close(); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
		
		return abo;
	}

	@Override
	public void reservationPour(Abonne ab) {
		assert(this.emprunteur() == null) : "Le DVD a déjà été emprunté.";
		assert(this.reserveur() == null) : "Le DVD a déjà été réservé.";
		try {
			stmt = conn.createStatement();
			String sql = "UPDATE dvd SET reserveur = ? WHERE idDvd = ?";
			PreparedStatement req = conn.prepareStatement(sql);
			req.setInt(1, ab.getIdAbonne());
			req.setInt(2, idDvd);
			int result = req.executeUpdate();
	        if (result > 0) {
	            System.out.println("La réservation a été effectuée avec succès.");
	        } else {
	            System.out.println("La réservation a échoué.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { rs.close(); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
		
	}

	@Override
	public void empruntPar(Abonne ab) {
		assert(this.emprunteur() == null) : "Le DVD a déjà été emprunté.";
		System.out.println(this.reserveur() + " et " + ab);
		assert(this.reserveur() == null || this.reserveur().toString().equals(ab.toString())) : "Le DVD a déjà été réservé par quelqu'un d'autre.";
		try {
			stmt = conn.createStatement();
			String sql1 = "UPDATE dvd SET reserveur = null WHERE idDvd = ?";
			PreparedStatement req1 = conn.prepareStatement(sql1);
			req1.setInt(1, idDvd);
			req1.executeUpdate();
			String sql2 = "UPDATE dvd SET emprunteur = ? WHERE idDvd = ?";
			PreparedStatement req2 = conn.prepareStatement(sql2);
			req2.setInt(1, ab.getIdAbonne());
			req2.setInt(2, idDvd);
			int result = req2.executeUpdate();
	        if (result > 0) {
	            System.out.println("L'emprunt a été effectuée avec succès.");
	        } else {
	            System.out.println("L'emprunt a échoué.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { rs.close(); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
		
	}

	@Override
	public void retour() {
		// TODO Auto-generated method stub
		
	}

}
