package Serveur;

import java.sql.*; 
import java.util.ArrayList;
import java.util.List;

public class DVD implements Document{
	private int idDvd;
	private String titre;
	private boolean adulte;
	private Abonne emprunteur;
	private Abonne reserveur;
	Statement stmt = null;
    ResultSet rs = null;

	
	public DVD(int idDvd, String titre, boolean adulte, Abonne emprunteur, Abonne reserveur) {
		this.idDvd = idDvd;
		this.titre = titre;
		this.adulte = adulte;
		this.emprunteur = emprunteur;
		this.reserveur = reserveur;
	}
	
	@Override
	public int numero() {
		return this.idDvd;
	}

	@Override
	public Abonne emprunteur() {
		return this.emprunteur;
	}

	@Override
	public Abonne reserveur() {
		return this.reserveur;
	}

	@Override
	public void reservationPour(Abonne ab) {
		assert(this.emprunteur() == null) : "Le DVD a déjà été emprunté.";
		assert(this.reserveur() == null) : "Le DVD a déjà été réservé.";
		if(this.getAdulte()) {
			assert(this.getAdulte() == ab.getAdulte()) : "Vous n'avez pas l'âge pour réserver ce DVD.";
		}
		this.reserveur = ab;
		
	}

	@Override
	public void empruntPar(Abonne ab) {
		assert(this.emprunteur() == null) : "Le DVD a déjà été emprunté.";
		assert(this.reserveur() == null || this.reserveur().getIdAbonne() == ab.getIdAbonne()) : "Le DVD a déjà été réservé par quelqu'un d'autre.";
		if(this.getAdulte()) {
			assert(this.getAdulte() == ab.getAdulte()) : "Vous n'avez pas l'âge pour réserver ce DVD.";
		}
		this.emprunteur = ab;
		
	}

	@Override
	public void retour() {
		assert(this.emprunteur() != null || this.reserveur() != null) : "Le DVD que vous souhaitez rendre est déjà chez nous.";
		this.emprunteur = null;
		this.reserveur = null;
	}
	
	/*public List<DVD> listeDvdDisponible(){
		List<DVD> listeDVD = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM dvd WHERE emprunteur = null OR reserveur = null");
			while(rs.next()) {
				Integer a = rs.getInt("idDvd");
				DVD dvd = new DVD(conn, a);
				listeDVD.add(dvd);
				System.out.println(dvd.getTitre());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { rs.close(); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
        return listeDVD;
	}*/

	public boolean getAdulte() {
		return adulte;
	}

	public String getTitre() {
		return titre;
	}

}
