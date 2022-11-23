package Components;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

import Errors.BadInputException;
import Util.Timer;
import Util.Tools;

public class Clock extends JPanel implements Runnable {
	private Timer timer;
	private JLabel[] digits;
	private int digitsSize = 3;
	private int margin = 4;

	/**
	 * Constructor
	 * @param timer 
	 * @param w width
	 * @param h height
	 * @param x location
	 * @param y location
	 */
	public Clock(Timer timer, int w, int h, int x, int y) {
		setSize(w, h);
		setLocation(x, y);
		this.timer = timer;
		setBorder(new CompoundBorder(
		new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null),
		new BevelBorder(BevelBorder.LOWERED, null, null, null, null)),
		new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null),
		new BevelBorder(BevelBorder.RAISED, null, null, null, null))));
		
		setLayout(null);
		initCounter();
	}

	@Override
	public void run() {
		while (timer.isActive()) {
			setClock(timer.getTime());
			updateClock();
			Tools.Wait(timer.getDelay());
			
			if (timer.isOn()) {
				timer.incrment();
				if (timer.getTime() != -1 && timer.getTime() >= timer.getStopTime()) {
					timer.off();
				}
			}
		}
	}

	/**
	 * Initializes the counter and sets it to 0
	 */
	private void initCounter() {
		digits = new JLabel[digitsSize];
		digits[0] = new JLabel();
		digits[0].setBounds(margin, margin, (getWidth() - margin * 2) / digitsSize, getHeight() - margin * 2);
		setNumber(digits[0], 0);
		add(digits[0]);
		
		for (int i = 1; i < digits.length; i++) {
			digits[i] = new JLabel();
			digits[i].setBounds(digits[i - 1].getX() + digits[i - 1].getWidth(), digits[i - 1].getY(),
			digits[i - 1].getWidth(), digits[i - 1].getHeight());
			setNumber(digits[i], 0);
			add(digits[i]);
		}
		revalidate();
		repaint();
		
		setClock(0);
	}

	/**
	 * Sets the passed label to the specified digit value
	 * @param label that is to be set
	 * @param digit to set the lable to
	 */
	private void setNumber(JLabel label, int digit) {
		if (!Tools.isDigit(digit)) throw new BadInputException("Input must be positive and digit: " + digit);
		label.setIcon(new ImageIcon());
		Image image = new ImageIcon(Images.getNSDDigit(digit)).getImage();
		Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
		label.setIcon(new ImageIcon(newimg));
	}

	/**
	 * Redraw the clock
	 */
	private void updateClock() {
		for (JLabel label : digits) {
			add(label);
		}
		revalidate();
		repaint();
	}

	/**
	 * Sets the Clock to the specified time
	 * @param time
	 */
	public void setClock(int time) {
		if (Tools.numOfDigits(time) > digitsSize) {
			throw new BadInputException("Number does not fit");
		}
		if (time < 0) {
			throw new BadInputException("Number can't be negative");
		}
		for (int i = 0; i < digitsSize; i++) {
			int digit = (int) (time / Math.pow(10, digitsSize - 1 - i)) % 10;
			setNumber(digits[i], digit);
		}
	}

	/**
	 * Setters and Getters
	 */
	public synchronized Timer getTime() {
		return this.timer;
	}

	public synchronized void setTimer(Timer timer) {
		this.timer = timer;
	}

	public synchronized int getValue() {
		return this.timer.getTime();
	}
}