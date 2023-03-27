package Serveur;

import java.sql.*;

public class DVD implements Document{
	private Abonne abonne;
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

	@SuppressWarnings("unused")
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
				abo = new Abonne(this.conn, a);
				if (a==null) {
					return null;
				}
				return abo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Abonne reserveur() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reservationPour(Abonne ab) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void empruntPar(Abonne ab) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retour() {
		// TODO Auto-generated method stub
		
	}

}
