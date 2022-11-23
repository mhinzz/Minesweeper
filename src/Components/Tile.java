package Components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

public class Tile extends JLabel {
	
	private Content content;
	private byte number = 0;
	private boolean flag = false;
	private boolean revealed = false;
	
	public static enum Content {
		EMPTY, NUMBER, MINE;
	}

	/**
	 * Constructor 
	 * @param x location of the tile
	 * @param y location of the tile
	 * @param width of the tile
	 * @param height of the tile
	 */
	public Tile(int x, int y, int width, int height) {
		setSize(width, height);
		setLocation(x, y);
		setIcon(Images.TILE_NOT_PRESSED);
	}
	
	/**
	 * Sets the icon of the tile to the icon at the specified @param iconPath
	 */
	public void setIcon(String iconPath) {
		Image image = new ImageIcon(iconPath).getImage();
		Image newimg = image.getScaledInstance(getWidth(), getHeight(), java.awt.Image.SCALE_SMOOTH);
		super.setIcon(new ImageIcon(newimg));
	}
	
	/**
	 * Makes the icon specified by @param i and @param j in the @param map unselectable
	 * @return true the i or j are out of bounds or if the icon is already revealed
	 */
	public static boolean invalidateTile(Tile[][] map, int i, int j) {
		return i >= map.length || i < 0 || j >= map[i].length || j < 0 || map[i][j].isRevealed();
	}
	
	/**
	 * 
	 * @param map
	 * @param i
	 * @param j
	 * @return
	 */
	public static boolean reveal(Tile[][] map, int i, int j) {
		if (invalidateTile(map, i, j) || map[i][j].isFlagged()) return true;
		
		Tile tile = map[i][j].setRevealed(true);
		
		if (tile.flag && !tile.isEmpty()) return true;
		
		String icon = Images.getTileDigit(0);
		switch (tile.getContent()) {
			case MINE: 
			tile.setFlag(true);
			tile.setIcon(Images.MINE_BLOWN);
			return false;
			
			case NUMBER:
			icon = Images.getTileDigit(tile.getNumber());
			break;

			case EMPTY:
			reveal(map, i, j - 1);
			reveal(map, i, j + 1);
			reveal(map, i + 1, j);
			reveal(map, i - 1, j);
			reveal(map, i + 1, j + 1);
			reveal(map, i + 1, j - 1);
			reveal(map, i - 1, j + 1);
			reveal(map, i - 1, j - 1);
			break;

			default:
			break;
		}
		tile.setIcon(icon);
		
		return true;
	}
	
	public static void setNumeral(Tile[][] map, int w, int h) {
		try {
			if (map[w][h].getContent().equals(Tile.Content.MINE)) {
				return;
			}
			map[w][h].setContent(Tile.Content.NUMBER);
			map[w][h].number++;
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace();
		}
	}
	
	/**
	 * Setters and getters 
	 */
	public Content getContent() {
		return this.content;
	}
	
	public void setContent(Content content) {
		if (content.equals(Content.EMPTY)) number = 0;
		else if (content.equals(Content.MINE)) number = -1;
		this.content = content;
	}
	
	public boolean isEmpty() {
		return this.content.equals(Content.EMPTY);
	}
	
	public void setEmpty(boolean empty) {
		this.content = Content.EMPTY;
	}
	
	public boolean isFlagged() {
		return this.flag;
	}
	
	public void setFlag(boolean flag) {
		setIcon((flag) ? Images.FLAG : Images.TILE_NOT_PRESSED);
		this.flag = flag;
	}
	
	public byte getNumber() {
		return this.number;
	}
	
	public void setNumber(byte number) {
		this.number = number;
	}
	
	public boolean isRevealed() {
		return this.revealed;
	}
	
	public Tile setRevealed(boolean revealed) {
		this.revealed = revealed;
		return this;
	}
}