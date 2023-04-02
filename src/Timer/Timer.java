package Timer;

import java.util.concurrent.CountDownLatch;

public class Timer implements Runnable{
	
	private int temps;
    private CountDownLatch countDownLatch;

    public Timer(int temps, CountDownLatch countDownLatch) {
        this.temps = temps;
        this.countDownLatch = countDownLatch;
    }

	@Override
	public void run() {
		try {
			Thread.sleep(temps);
            countDownLatch.countDown();
		} catch (InterruptedException e) {}	
	}

}
