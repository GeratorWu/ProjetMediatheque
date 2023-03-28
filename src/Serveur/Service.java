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

public class Service implements Runnable{
	private Socket socket;
	private DatabaseConnection conn;
	private Connection connection;
	Statement stmt = null;
    ResultSet rs = null;
	private static List<DVD> listeDVD = new ArrayList<DVD>();
	private static List<Abonne> listeAbonne = new ArrayList<Abonne>();
	
	public Service(Socket socket) {
		this.socket = socket;
		conn = new DatabaseConnection();
		connection = conn.getConnection();
		listeDVD = this.creationObjetDvd();
		listeAbonne = this.creationObjetAbonne();
	}
	public void run() {
		Scanner socketIn;
		try {
			System.out.println("Serveur démarré sur le port : " + socket.getLocalPort());
			PrintWriter socketOut = new PrintWriter (socket.getOutputStream ( ), true);
			socketIn = new Scanner(new InputStreamReader(socket.getInputStream ( )));
			int idAbonne;
			int idDvd;
			Abonne ab;
			DVD dvd;

			switch(socket.getLocalPort()) {
			case 3000:
				idAbonne = socketIn.nextInt();
				ab = this.trouverAbonneParId(idAbonne);
				idDvd = socketIn.nextInt();
				dvd = this.trouverDVDParId(idDvd);
				dvd.reservationPour(ab);
				socketOut.println("Bonjour " + ab.getNom() + " " + ab.getPrenom() + ", vous avez réserver le DVD : " + dvd.getTitre());
				break;
			case 4000:
				idAbonne = socketIn.nextInt();
				ab = this.trouverAbonneParId(idAbonne);
				idDvd = socketIn.nextInt();
				dvd = this.trouverDVDParId(idDvd);
				dvd.empruntPar(ab);
				socketOut.println("Bonjour " + ab.getNom() + " " + ab.getPrenom() + ", vous avez emprunter le DVD : " + dvd.getTitre());
				break;
			case 5000:
				idDvd = socketIn.nextInt();
				dvd = this.trouverDVDParId(idDvd);
				if(dvd.emprunteur() != null) {
					dvd.retour();
					socketOut.println("Bonjour, merci d'avoir rendu le DVD : " + dvd.getTitre());
				}
				else{
					dvd.retour();
					socketOut.println("Bonjour, la réservation a bien été annulé pour le DVD : " + dvd.getTitre());
				}
				break;
			default:
				System.out.println("Port non pris en charge.");
			}
			conn.closeConnection();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	public List<DVD> creationObjetDvd() {
		List<DVD> listeDVD = new ArrayList<DVD>();
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
				listeDVD.add(new DVD(id, titre, adulte, emprunteur, reserveur));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { rs.close(); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
        return listeDVD;
	}
	
	public List<Abonne> creationObjetAbonne() {
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
	
	public DVD trouverDVDParId(int id) {
	    for (DVD dvd : listeDVD) {
	        if (dvd.numero() == id) {
	            return dvd;
	        }
	    }
	    return null;
	}
	
	public Abonne trouverAbonneParId(int id) {
	    for (Abonne abonne : listeAbonne) {
	        if (abonne.getIdAbonne() == id) {
	            return abonne;
	        }
	    }
	    return null;
	}
}
