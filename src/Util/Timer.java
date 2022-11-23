package Util;

public class Timer {

	private boolean isOn = false;
	private boolean isActive = true;
	private int seconds = 0;
	private int delay = 1000;
	private int stopTime = -1;

	/**
	 * Default constructor, time starts a 0 and ends at 999 with one tick per second
	 */
	public Timer() {
		setup(0, 1000, 999);
		off();
	}

	/**
	 * Reset time to 0
	 */
	public void reset() {
		setup(0, delay, -1);
	}

	/**
	 * Set parameters all at once
	 * @param start
	 * @param delay
	 * @param stopTime
	 */
	public void setup(int start, int delay, int stopTime) {
		set(start);
		setDelay(delay);
		setStopTime(stopTime);
	}

	/**
	 * Increment seconds by one
	 */
	public void incrment() {
		this.seconds += 1;
	}

	/**
	 * Setters and Getters
	 */
	public synchronized void kill() {
		this.isActive = false;
	}

	public synchronized void on() {
		this.isOn = true;
	}

	public synchronized void off() {
		this.isOn = false;
	}

	public synchronized boolean isOn() {
		return isOn;
	}

	public synchronized int getTime() {
		return seconds;
	}

	public synchronized int set(int seconds) {
		return this.seconds = seconds;
	}

	public synchronized int getDelay() {
		return delay;
	}

	public synchronized void setDelay(int delay) {
		this.delay = delay;
	}

	public synchronized int getStopTime() {
		return stopTime;
	}

	public synchronized void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}

	public synchronized boolean isActive() {
		return isActive;
	}
}