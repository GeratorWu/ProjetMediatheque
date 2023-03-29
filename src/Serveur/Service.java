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
import java.util.concurrent.CountDownLatch;

import Abonne.Abonne;
import Document.DVD;
import Timer.Timer;
import bttp2.Codage;

public class Service implements Runnable{
	private Socket socket;
	private DatabaseConnection conn;
	private Connection connection;
	private Statement stmt = null;
    private ResultSet rs = null;
    private static List<Abonne> listeAbonne = new ArrayList<Abonne>();
	private static List<DVD> listeDVD = new ArrayList<DVD>();
	
	public Service(Socket socket) {
		this.socket = socket;
		conn = new DatabaseConnection();
		connection = conn.getConnection();
		listeAbonne = this.creationObjetAbonne();
		listeDVD = this.creationObjetDvd();
	}
	public void run() {
		Scanner socketIn;
		try {
			System.out.println("Serveur démarré sur le port : " + socket.getLocalPort());
			PrintWriter socketOut = new PrintWriter (socket.getOutputStream ( ), true);
			socketIn = new Scanner(new InputStreamReader(socket.getInputStream ( )));
			int idAbonne;
			int idDvd = 0;
			Abonne ab;
			DVD dvd;

			switch(socket.getLocalPort()) {
			case 3000:
				CountDownLatch countDownLatch = new CountDownLatch(1);
				
				socketOut.println(Codage.coder(this.DVDDisponible()));
				idAbonne = socketIn.nextInt();
				ab = this.trouverAbonneParId(idAbonne);
				idDvd = socketIn.nextInt();
				dvd = this.trouverDVDParId(idDvd);
				dvd.reservationPour(ab);
				dvd.setHeureReserve();
				socketOut.println("Bonjour " + ab.getNom() + " " + ab.getPrenom() + ", vous avez réserver le DVD : " + dvd.getTitre() + ", vous avez jusqu'à " + dvd.getHeure2h() + " pour l'emprunter");
				this.sauvegardeDVD(idDvd);
				
				Thread timer;
				while(true) {
					timer = new Thread(new Timer(1000 * 60 * 60 * 2, this.socket, countDownLatch));
					timer.start();
					try {
					    countDownLatch.await();
					} catch (InterruptedException e) {
					    e.printStackTrace();
					}
					timer.interrupt();
					
					if(dvd.emprunteur() == null){
						dvd.annulerReservation();
					    this.sauvegardeDVD(idDvd);
					}
					break;
				}
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
			this.sauvegardeDVD(idDvd);
			conn.closeConnection();
			try { rs.close(); } catch (Exception e) {}
	        try { stmt.close(); } catch (Exception e) {}
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
		assert(id <= listeDVD.size()) : "Ce DVD n'existe pas";
	    for (DVD dvd : listeDVD) {
	        if (dvd.numero() == id) {
	            return dvd;
	        }
	    }
	    return null;
	}
	
	public Abonne trouverAbonneParId(int id) {
		assert(id <= listeAbonne.size()) : "Cet abonné n'existe pas";
	    for (Abonne abonne : listeAbonne) {
	        if (abonne.getIdAbonne() == id) {
	            return abonne;
	        }
	    }
	    return null;
	}
	
	public void sauvegardeDVD(int idDvd) {
		DVD dvd = this.trouverDVDParId(idDvd);
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
	
	public String DVDDisponible() {
		StringBuilder sb = new StringBuilder("Voici la liste des DVD disponibles :##");
		for (DVD dispodvd : listeDVD) {
			if (dispodvd.disponible()) {
					sb.append("Numéro du DVD : ").append(dispodvd.numero()).append(", il a pour titre : ").append(dispodvd.getTitre()).append("##");
			}
		}
		return sb.toString();
	}
}
