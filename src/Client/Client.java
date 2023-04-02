package Client;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import bttp2.Codage;

public class Client {

	  public static void main(String[] args) throws IOException { //Cr�ation du client avec le port utilis�
		  int port = Integer.parseInt(args[0]);

		    Socket socket = null;
	
		    try {
		    	socket = new Socket("localhost", port);
		    	
				Scanner socketIn = new Scanner(new InputStreamReader(socket.getInputStream ( )));
				PrintWriter socketOut = new PrintWriter (socket.getOutputStream ( ), true);
				Scanner sc = new Scanner(System.in);
				
				switch(port) {
				case 3000:
					System.out.println(Codage.decoder(socketIn.nextLine()));
					System.out.println("Quel est votre num�ro d'abonn� ?");
					socketOut.println(sc.nextLine());
					System.out.println("Quel est le num�ro du DVD que vous voulez r�server ?");
					socketOut.println(sc.nextLine());
					break;
				case 4000:
					System.out.println("Quel est votre num�ro d'abonn� ?");
					socketOut.println(sc.nextLine());
					System.out.println("Quel est le num�ro du DVD que vous voulez emprunter ?");
					socketOut.println(sc.nextLine());
					break;
				case 5000:
					System.out.println("Quel est le numero du DVD que vous voulez rendre ?");
					socketOut.println(sc.nextLine());
					break;
				default:
					System.out.println("Port non pris en charge.");
				}
				System.out.println(socketIn.nextLine());
				socket.close();
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	  }

}
