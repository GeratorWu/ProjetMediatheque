package Serveur;
import java.io.IOException;

public class AppServeur {
	final static int port = 1234;
	public static void main(String[] args) {
		try {
			Serveur serveur = new Serveur(port);
			Thread a = new Thread(serveur);
			a.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
