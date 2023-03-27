package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	  public static void main(String[] args) {
		  int port = Integer.parseInt(args[0]);

		    Socket socket;
	
		    try {
		    	socket = new Socket("localhost", port);
				System.out.println("Quel est votre idAbonne ?");
				Scanner socketIn = new Scanner(new InputStreamReader(socket.getInputStream ( )));
				PrintWriter socketOut = new PrintWriter (socket.getOutputStream ( ), true);
				Scanner sc = new Scanner(System.in);
				socketOut.println((sc.nextLine()));
				
				System.out.println("Quel est le DVD que vous voulez emprunter ?");
				socketOut.println((sc.nextLine()));
				System.out.println(socketIn.nextLine());
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }

}
