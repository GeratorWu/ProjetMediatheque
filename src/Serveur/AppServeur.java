package Serveur;
import java.io.IOException;

public class AppServeur {
	final static int port1 = 3000;
	final static int port2 = 4000;
	final static int port3 = 5000;
	public static void main(String[] args) {
		try {
			Serveur serveur1 = new Serveur(port1);
			Serveur serveur2 = new Serveur(port2);
			Serveur serveur3 = new Serveur(port3);
			Thread a = new Thread(serveur1);
			Thread b = new Thread(serveur2);
			Thread c = new Thread(serveur3);
			a.start();
			b.start();
			c.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
