package Serveur;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Abonne.Abonne;
import Document.DVD;
import Exception.RestrictionException;
import Mediatheque.Document;
import bttp2.Codage;

public class Service implements Runnable{
	private Socket socket;
	private DatabaseConnection conn;
	private Connection connection;
	private Statement stmt = null;
    private ResultSet rs = null;
    private static List<Abonne> listeAbonne = new ArrayList<Abonne>();
	private static List<Document> listeDocument = new ArrayList<Document>();
	
	public Service(Socket socket) {
		this.socket = socket;
		conn = new DatabaseConnection();
		connection = conn.getConnection();
		listeAbonne = this.creationObjetAbonne();
		listeDocument = this.creationObjetDocument();
	}
	public void run() {
		Scanner socketIn;
		PrintWriter socketOut = null;
		try {
			System.out.println("Serveur démarré sur le port : " + socket.getLocalPort()); //Vérification que le serveur a bien démarré.
			socketOut = new PrintWriter (socket.getOutputStream ( ), true);
			socketIn = new Scanner(new InputStreamReader(socket.getInputStream ( )));
			int idAbonne;
			int idDvd = 0;
			Abonne ab;
			Document dvd;

			switch(socket.getLocalPort()) { // Afficher le service en fonction du port connecté.
			case 3000: // Réservation
				
				
				socketOut.println(Codage.coder(this.DVDDisponible()));  // Codage du message envoyer au client.
				idAbonne = socketIn.nextInt(); // On récupère l'idAbonné du client
				ab = this.trouverAbonneParId(idAbonne); // On stock l'abonné trouver via une requete vers le serveur.
				idDvd = socketIn.nextInt(); // On fait la même chose pour le DVD
				dvd = this.trouverDocumentParId(idDvd);
				dvd.reservationPour(ab); // On réserve le dvd pour l'abonné.
				socketOut.println("Bonjour " + ab.getNom() + " " + ab.getPrenom() + ", vous avez réserver le DVD : " + dvd.toString() + ", vous avez 2 heures pour l'emprunter");
				this.sauvegardeDVD(idDvd); // On met à jour la base de données avec le nouveau DVD.
				
				
				break;
			case 4000: // Emprunt
				idAbonne = socketIn.nextInt();
				ab = this.trouverAbonneParId(idAbonne);
				idDvd = socketIn.nextInt();
				dvd = this.trouverDocumentParId(idDvd);
				dvd.empruntPar(ab);
				socketOut.println("Bonjour " + ab.getNom() + " " + ab.getPrenom() + ", vous avez emprunter le DVD : " + dvd.toString());
				break;
			case 5000: // Retour
				idDvd = socketIn.nextInt();
				dvd = this.trouverDocumentParId(idDvd);
				if(dvd.emprunteur() != null) {
					dvd.retour();
					socketOut.println("Bonjour, merci d'avoir rendu le DVD : " + dvd.toString());
				}
				else{
					dvd.retour();
					socketOut.println("Bonjour, la réservation a bien été annulé pour le DVD : " + dvd.toString());
				}
				break;
			default:
				System.out.println("Port non pris en charge.");
			}
			this.sauvegardeDVD(idDvd);
			conn.closeConnection();
			try { rs.close(); } catch (Exception e) {}
	        try { stmt.close(); } catch (Exception e) {}
			this.socket.close();
		} catch (IOException | RestrictionException e) { // On récupère les erreurs si le client fait n'importe quoi.
			socketOut.println(e.getMessage());
		}	
		
	}
	
	public List<Document> creationObjetDocument() { // Création des objets DVD
		List<Document> listeDocument = new ArrayList<Document>();
		Integer emprunteurtmp;
		Integer reserveurtmp;
		try {
			stmt = connection.createStatement();
			String sql = "SELECT * FROM dvd";
			PreparedStatement req = connection.prepareStatement(sql);
			rs = req.executeQuery();
			while(rs.next()) {
				Integer id = rs.getInt("idDvd");
				String titre = rs.getString("titre");
				Boolean adulte = rs.getBoolean("adulte");
				emprunteurtmp = rs.getInt("emprunteur");
				Abonne emprunteur = this.trouverAbonneParId(emprunteurtmp);
				reserveurtmp = rs.getInt("reserveur");
				Abonne reserveur = this.trouverAbonneParId(reserveurtmp);
				listeDocument.add(new DVD(id, titre, adulte, emprunteur, reserveur));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        return listeDocument;
	}
	
	public List<Abonne> creationObjetAbonne() { // Création des objets abonnées.
		List<Abonne> listeAbonne = new ArrayList<Abonne>();
		Date dateNaissanceTmp;
		try {
			stmt = connection.createStatement();
			String sql = "SELECT * FROM abonne";
			PreparedStatement req = connection.prepareStatement(sql);
			rs = req.executeQuery();
			while(rs.next()) {
				Integer id = rs.getInt("idAbonne");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				dateNaissanceTmp = rs.getDate("dateNaissance");
				LocalDate dateNaissance = dateNaissanceTmp.toLocalDate();
				listeAbonne.add(new Abonne(id, nom, prenom, dateNaissance));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeAbonne;
	}
	
	public Document trouverDocumentParId(int id) { // Trouver un DVD via l'id
		assert(id <= listeDocument.size()) : "Ce document n'existe pas";
	    for (Document dvd : listeDocument) {
	        if (dvd.numero() == id) {
	            return dvd;
	        }
	    }
	    return null;
	}
	
	public Abonne trouverAbonneParId(int id) { // Trouver un abonne via l'id
		assert(id <= listeAbonne.size()) : "Cet abonné n'existe pas";
	    for (Abonne abonne : listeAbonne) {
	        if (abonne.getIdAbonne() == id) {
	            return abonne;
	        }
	    }
	    return null;
	}
	
	public void sauvegardeDVD(int idDvd) { // Sauvegarder tous les DVD
		Document dvd = this.trouverDocumentParId(idDvd);
		Integer a = 0;
		Integer b = 0;
		try {
			stmt = connection.createStatement();
			String sql = "UPDATE dvd set emprunteur = ?, reserveur = ? WHERE idDvd = ?";
			PreparedStatement req = connection.prepareStatement(sql);
			Abonne emprunteur = dvd.emprunteur();
			if(emprunteur != null) {
				a = emprunteur.getIdAbonne();
			}
			else {
				a = null;
			}
			req.setObject(1, a, java.sql.Types.INTEGER);
			
			Abonne reserveur = dvd.reserveur();
			if(reserveur != null) {
				b = reserveur.getIdAbonne();
			}
			else {
				b = null;
			}
			req.setObject(2, b, java.sql.Types.INTEGER);
			req.setInt(3, dvd.numero());
			int result = req.executeUpdate();
			if (result > 0) {
	            System.out.println("Sauvegarde réussie");
	        } else {
	            System.out.println("Sauvegarde écouché.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String DVDDisponible() { //Afficher les DVD au client grâce au codage
		StringBuilder sb = new StringBuilder("Voici la liste des DVD disponibles :##");
		for (Document dispodvd : listeDocument) {
			if (dispodvd.reserveur() == null && dispodvd.emprunteur() == null) {
					sb.append("Numéro du DVD : ").append(dispodvd.numero()).append(", il a pour titre : ").append(dispodvd.toString()).append("##");
			}
		}
		return sb.toString();
	}
}
