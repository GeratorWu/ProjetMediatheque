package Abonne;

import java.time.LocalDate;
import java.time.Period;

public class Abonne {
	private Integer idAbonne;
	private String nom;
	private String prenom;
	private LocalDate dateNaissance;
	private boolean adulte;
	
	public Abonne(Integer idAbonne, String nom, String prenom, LocalDate dateNaissance) {
		this.idAbonne = idAbonne;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		adulte = this.estAdulte(dateNaissance);
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

	public LocalDate getDateNaissance() {
		return this.dateNaissance;
	}
	
	public boolean getAdulte() {
		return this.adulte;
	}

	public boolean estAdulte(LocalDate dateNaissance2) { // Savoir si l'abonné est adulte pour les réservations et emprunts.
	    LocalDate maintenant = LocalDate.now();
	    Period periode = Period.between(dateNaissance2, maintenant);
	    int age = periode.getYears();
	    if (periode.getMonths() > 0 || (periode.getMonths() == 0 && periode.getDays() > 0)) {
	        age--;
	    }
	    return age >= 16;
	}
}
