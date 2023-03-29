package Serveur;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DVD implements Document{
	private int idDvd;
	private String titre;
	private boolean adulte;
	private Abonne emprunteur;
	private Abonne reserveur;
	private LocalTime heure = LocalTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	
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
		assert(this.emprunteur() == null) : "Le DVD a d�j� �t� emprunt�.";
		assert(this.reserveur() == null) : "Le DVD a d�j� �t� r�serv�.";
		if(this.getAdulte()) {
			assert(this.getAdulte() == ab.getAdulte()) : "Vous n'avez pas l'�ge pour r�server ce DVD.";
		}
		this.reserveur = ab;
		

	}
	
	public void annulerReservation() {
	    System.out.println("La r�servation pour le DVD " + titre + " a �t� annul�e.");
	    reserveur = null;
	}
	
	@Override
	public void empruntPar(Abonne ab) {
		assert(this.emprunteur() == null) : "Le DVD a d�j� �t� emprunt�.";
		assert(this.reserveur() == null || this.reserveur().getIdAbonne().equals(ab.getIdAbonne())) : "Le DVD a d�j� �t� r�serv� par quelqu'un d'autre.";
		if(this.getAdulte()) {
			assert(this.getAdulte() == ab.getAdulte()) : "Vous n'avez pas l'�ge pour emprunter ce DVD.";
		}
		this.emprunteur = ab;
		this.reserveur = null;
		
	}

	@Override
	public void retour() {
		assert((this.emprunteur() == null && this.reserveur() != null) || (this.emprunteur() != null && this.reserveur() == null)) : "Le DVD que vous souhaitez rendre est d�j� chez nous.";
		this.emprunteur = null;
		this.reserveur = null;
	}
	
	public boolean disponible(){
		if (this.reserveur() == null && this.emprunteur() == null)
			return true;
		return false;
	}

	public boolean getAdulte() {
		return adulte;
	}

	public String getTitre() {
		return titre;
	}
	
	public String getHeure2h() {
		return this.heure.plusHours(2).format(formatter);
	}

}
