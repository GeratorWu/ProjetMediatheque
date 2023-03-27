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
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM DVD WHERE idDvd = ?";
			PreparedStatement req = conn.prepareStatement(sql);
			req.setInt(1, idDvd);
			rs = req.executeQuery();
			while(rs.next()) {
				Integer a = rs.getInt("emprunteur");
				Integer b = rs.getInt("reserveur");
				if (a==null && b == null) {
					return null;
				}
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
