package Serveur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Service implements Runnable{
	private Socket socket;
	
	public Service(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		Scanner socketIn;
		try {
			System.out.println("Serveur démarré sur le port : " + socket.getLocalPort());
			socketIn = new Scanner(new InputStreamReader(socket.getInputStream ( )));
			PrintWriter socketOut = new PrintWriter (socket.getOutputStream ( ), true);
			socketOut.println("test");
			int idAbonne = socketIn.nextInt();
			int m = socketIn.nextInt();
			/*Thread t = new Thread(new Inscription(cours.get(lc-1), m));
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			//System.out.println("J'ai bien lu");
			//socketOut.println (cours.get(lc-1).toString());
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
}
