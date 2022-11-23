package Util;

public class Mode {
	private int mineAmount;
	private int mapWidth;
	private int mapHeight;
	private String name;
	
	/**
	 * Constructor
	 * @param name
	 * @param modes
	 * @return
	 */
	public static Mode getModeByName(String name, Mode[] modes) {
		for (Mode mode : modes) {
			if (Tools.equalsNoCase(name, mode.getName())) {
				return mode;
			}
		}
		return null;
	}

	/**
	 * Setters and Getters
	 */
	public String getName() {
		return name;
	}
	
	public Mode setName(String name) {
		if (name == null) {
			return null;
		}
		this.name = name;
		return this;
	}
	
	public int getMinesAmount() {
		return mineAmount;
	}
	
	public Mode setMineAmount(int mineAmount) {
		if (mineAmount <= 0) {
			return null;
		}
		this.mineAmount = mineAmount;
		return this;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public Mode setMapWidth(int mapWidth) {
		if (mapWidth <= 0) {
			return null;
		}
		this.mapWidth = mapWidth;
		return this;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	
	public Mode setMapHeight(int mapHeight) {
		if (mapHeight <= 0) {
			return null;
		}
		this.mapHeight = mapHeight;
		return this;
	}
}