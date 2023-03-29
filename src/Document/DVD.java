package Document;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import Abonne.Abonne;

public class DVD implements Document{
	private int idDvd;
	private String titre;
	private boolean adulte;
	private Abonne emprunteur;
	private Abonne reserveur;
	private LocalTime heure = LocalTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private LocalTime heurereserve;

	
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
		assert(this.reserveur() == null) : "Le DVD est réservé jusqu'à " + this.getHeureReserve();
		if(this.getAdulte()) {
			assert(this.getAdulte() == ab.getAdulte()) : "Vous n'avez pas l'âge pour réserver ce DVD.";
		}
		this.reserveur = ab;
	}
	public void setHeureReserve() {
		this.heurereserve = this.heure.plusHours(2);
	}
	
	public LocalTime getHeureReserve(){
		return this.heurereserve;
	}
	
	public void annulerReservation() {
	    System.out.println("La réservation pour le DVD " + titre + " a été annulée.");
	    reserveur = null;
	}
	
	@Override
	public void empruntPar(Abonne ab) {
		assert(this.emprunteur() == null) : "Le DVD a déjà été emprunté.";
		assert(this.reserveur() == null || this.reserveur().getIdAbonne().equals(ab.getIdAbonne())) : "Le DVD est réservé jusqu'à " + this.heurereserve;
		if(this.getAdulte()) {
			assert(this.getAdulte() == ab.getAdulte()) : "Vous n'avez pas l'âge pour emprunter ce DVD.";
		}
		this.emprunteur = ab;
		this.reserveur = null;
		
	}

	@Override
	public void retour() {
		assert((this.emprunteur() == null && this.reserveur() != null) || (this.emprunteur() != null && this.reserveur() == null)) : "Le DVD que vous souhaitez rendre est déjà chez nous.";
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
