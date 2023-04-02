package Document;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

import Abonne.Abonne;
import Exception.RestrictionException;
import Mediatheque.Document;
import Timer.Timer;

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
	public void reservationPour(Abonne ab) throws RestrictionException { // Réservation pour un abonné
		synchronized (this) {
			if (this.emprunteur() != null) {
				throw new RestrictionException("Le DVD a déjà été emprunté.");
			}
			if (this.reserveur() != null) {
				throw new RestrictionException("Le DVD est réservé jusqu'à " + this.getHeureReserve());
			}
			if (this.getAdulte() && !ab.getAdulte()) {
				throw new RestrictionException("Vous n'avez pas l'âge pour réserver ce DVD.");
			}
			this.reserveur = ab;
			this.heurereserve = this.heure.plusHours(2);		
		}
	}
	
	public LocalTime getHeureReserve(){
		return this.heurereserve;
	}
	
	public void annulerReservation() {
		CountDownLatch countDownLatch = new CountDownLatch(1); // Création du Timer
		Thread timer; // On lance un timer pour 2h, si le timer est écoulé, la réservation est annulée.
		while(true) {
			timer = new Thread(new Timer(1000 * 60 * 60 * 2, countDownLatch)); 
			timer.start();
			try {
			    countDownLatch.await();
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
			timer.interrupt();
			
			if(this.emprunteur() == null){
				this.annulerReservation();
			}
			break;
		}
	    System.out.println("La réservation pour le DVD " + titre + " a été annulée.");
	    reserveur = null;
	}
	
	@Override
	public void empruntPar(Abonne ab) throws RestrictionException { // Emprunt par un abonné.
		synchronized (this) {
			if (this.emprunteur() != null) {
				throw new RestrictionException("Le DVD a déjà été emprunté.");
			}
			if (this.reserveur() != null && !this.reserveur().getIdAbonne().equals(ab.getIdAbonne())) {
				throw new RestrictionException("Le DVD est réservé jusqu'à " + this.heurereserve);
			}
			if (this.getAdulte() && !ab.getAdulte()) {
				throw new RestrictionException("Vous n'avez pas l'âge pour emprunter ce DVD.");
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
			throw new RestrictionException("Le DVD que vous souhaitez rendre est déjà chez nous.");
		}
	}

	public boolean getAdulte() { // Savoir si le DVD est pour les adultes.
		return adulte;
	}

	public String getHeure2h() { // Avoir l'heure dans deux heures.
		return this.heure.plusHours(2).format(formatter);
	}
	
	public String toString() {
		return this.titre;
	}

}
