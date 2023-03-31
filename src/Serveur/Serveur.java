package Serveur;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur implements Runnable{
	private ServerSocket serveur;
	Serveur(int port) throws IOException{
		this.serveur = new ServerSocket(port);
	}
	@Override
	public void run() {
		try {
			while(true) {
				Socket socket = serveur.accept();
				Service service = new Service(socket);
                Thread t1 = new Thread(service);
				t1.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
