package Document;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import Abonne.Abonne;
import Exception.RestrictionException;
import Mediatheque.Document;

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
	public void reservationPour(Abonne ab) throws RestrictionException { // R�servation pour un abonn�
		synchronized (this) {
			if (this.emprunteur() != null) {
				throw new RestrictionException("Le DVD a d�j� �t� emprunt�.");
			}
			if (this.reserveur() != null) {
				throw new RestrictionException("Le DVD est r�serv� jusqu'� " + this.getHeureReserve());
			}
			if (this.getAdulte() && !ab.getAdulte()) {
				throw new RestrictionException("Vous n'avez pas l'�ge pour r�server ce DVD.");
			}
			this.reserveur = ab;
		}
	}
	public void setHeureReserve() { // Avoir l'heure limite de r�servation, c'est � dire 2 heures.
		this.heurereserve = this.heure.plusHours(2);
	}
	
	public LocalTime getHeureReserve(){
		return this.heurereserve;
	}
	
	public void annulerReservation() {
	    System.out.println("La r�servation pour le DVD " + titre + " a �t� annul�e.");
	    reserveur = null;
	}
	
	@Override
	public void empruntPar(Abonne ab) throws RestrictionException { // Emprunt par un abonn�.
		synchronized (this) {
			if (this.emprunteur() != null) {
				throw new RestrictionException("Le DVD a d�j� �t� emprunt�.");
			}
			if (this.reserveur() != null && !this.reserveur().getIdAbonne().equals(ab.getIdAbonne())) {
				throw new RestrictionException("Le DVD est r�serv� jusqu'� " + this.heurereserve);
			}
			if (this.getAdulte() && !ab.getAdulte()) {
				throw new RestrictionException("Vous n'avez pas l'�ge pour emprunter ce DVD.");
			}
			this.emprunteur = ab;
			this.reserveur = null;
		}
	}

	@Override
	public void retour() throws RestrictionException { // Retour du DVD.
		if ((this.emprunteur() == null && this.reserveur() != null) || (this.emprunteur() != null && this.reserveur() == null)) {
			this.emprunteur = null;
			this.reserveur = null;
		} else {
			throw new RestrictionException("Le DVD que vous souhaitez rendre est d�j� chez nous.");
		}
	}
	
	public boolean disponible(){ // Savoir si le DVD est disponible
		if (this.reserveur() == null && this.emprunteur() == null)
			return true;
		return false;
	}

	public boolean getAdulte() { // Savoir si le DVD est pour les adultes.
		return adulte;
	}

	public String getTitre() {
		return titre;
	}
	
	public String getHeure2h() { // Avoir l'heure dans deux heures.
		return this.heure.plusHours(2).format(formatter);
	}
	
	public String toString() {
		return this.titre;
	}

}
