package Serveur;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.Scanner;

public class Service implements Runnable{
	private Socket socket;
	private DatabaseConnection conn;
	private Connection connection;
	
	public Service(Socket socket) {
		this.socket = socket;
		conn = new DatabaseConnection();
		connection = conn.getConnection();
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
				ab = new Abonne(connection, idAbonne);
				idDvd = socketIn.nextInt();
				dvd = new DVD(connection, idDvd);
				dvd.reservationPour(ab);
				socketOut.println("Bonjour " + idAbonne + ", vous avez réserver le DVD : " + idDvd);
				break;
			case 4000:
				idAbonne = socketIn.nextInt();
				ab = new Abonne(connection, idAbonne);
				idDvd = socketIn.nextInt();
				dvd = new DVD(connection, idDvd);
				dvd.empruntPar(ab);
				socketOut.println("Bonjour " + idAbonne + ", vous avez emprunter le DVD : " + idDvd);
				break;
			case 5000:
				idDvd = socketIn.nextInt();
				socketOut.println("Bonjour, vous avez rendu le DVD : " + idDvd);
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
}
