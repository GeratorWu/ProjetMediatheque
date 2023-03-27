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
				switch (socket.getLocalPort()) {
                case 3000:
                    Service service3000 = new Service(socket);
                    Thread t1 = new Thread(service3000);
    				t1.start();
                    break;
                case 4000:
                    Service service4000 = new Service(socket);
                    Thread t2 = new Thread(service4000);
    				t2.start();
                    break;
                case 5000:
                    Service service5000 = new Service(socket);
                    Thread t3 = new Thread(service5000);
    				t3.start();
                    break;
                default:
                    System.out.println("Port non pris en charge");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
