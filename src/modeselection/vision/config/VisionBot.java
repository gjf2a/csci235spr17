package modeselection.vision.config;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.video.Video;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BitImage;
import modeselection.vision.Pos;

abstract public class VisionBot implements Runnable {
	public static final int WIDTH = 160, HEIGHT = 120;

	abstract public BitImage processImage(AdaptedYUYVImage img);
	
	public void displayFinalInfo() {
		displayFrameRate(2);
	}
	
	public long getCycles() {return cycles;}
	
	public long getDuration() {return duration;}
	
	public long getLastCycleTime() {return duration - lastDuration;}
	
	public void run() {
		try {
			Video wc = setupVideo();
			cycles = 0;
			long startTime = System.currentTimeMillis();
			while (Button.ESCAPE.isUp()) {
				wc.grabFrame(frame);
				
				AdaptedYUYVImage img = new AdaptedYUYVImage(frame, WIDTH, HEIGHT);
				BitImage proc = processImage(img);
				proc.draw();
				cycles += 1;
				lastDuration = duration;
				duration = System.currentTimeMillis() - startTime;
			}
			
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private byte[] frame;
	private long cycles, duration, lastDuration;
	
	private Video setupVideo() throws IOException {
		Video wc = BrickFinder.getDefault().getVideo();
		wc.open(WIDTH, HEIGHT);
		frame = wc.createFrame();
		return wc;
	}
	
	private void finish() throws InterruptedException {
		LCD.clear();
		displayFinalInfo();
		Thread.sleep(1500);
		while (Button.ESCAPE.isUp());
	}
	
	public long frameRate() {
		return 1000 * cycles / duration;
	}
	
	public void displayFrameRate(int row) {
		String rate = String.format("rate: %4.2f hz", 1000.0 * cycles / duration);
		LCD.drawString(rate, 0, row);
	}
	
	public static boolean inBounds(Pos p) {
		return 0 <= p.getX() && p.getX() < WIDTH && 0 <= p.getY() && p.getY() < HEIGHT;
	}
}
