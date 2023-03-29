package Serveur;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Timer implements Runnable{
	
	private int temps;
    private Socket socket;
    private CountDownLatch countDownLatch;

    public Timer(int temps, Socket socket, CountDownLatch countDownLatch) {
        this.temps = temps;
        this.socket = socket;
        this.countDownLatch = countDownLatch;
    }

	@Override
	public void run() {
		try {
			Thread.sleep(temps);
            countDownLatch.countDown();
            this.socket.close();
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
		
	}

}
