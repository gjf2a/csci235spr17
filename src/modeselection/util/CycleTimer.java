package modeselection.util;

public class CycleTimer {
	private long duration, lastDuration, cycles, cycleStart;
	
	public CycleTimer() {
		duration = lastDuration = cycles = cycleStart = 0;
	}
	
	public void start() {
		cycleStart = System.currentTimeMillis();
	}
	
	public void bumpCycle() {
		long current = System.currentTimeMillis();
		lastDuration = current - cycleStart;
		duration += lastDuration;
		cycleStart = current;
		cycles += 1;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public long getLastDuration() {
		return lastDuration;
	}
	
	public long getCycles() {
		return cycles;
	}
	
	public double cyclesPerSecond() {
		return 1000.0 * cycles / duration;
	}
}
