package Views;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

import Components.Images;
import Components.Tile;
import Util.Mode;
import Util.Timer;

public class GameView extends JPanel {
	
	private Component parent;
	public static final int TILE_SIZE = 10;
	public static volatile int FLAG_COUNTER = 0;
	private Mode gameMode;
	private int tileSize;
	private Tile[][] map;
	private Timer mines;
	private Timer timer;
	private JLabel smileFace;
	private boolean GAMEOVER = false;
	private int prevI = -1, prevJ = -1;
	private boolean firstClick = true;
	
	/**
	* Contructor
	* @param parent
	* @param smileFace
	* @param gameMode
	* @param mines
	* @param timer
	*/
	public GameView(Component parent, JLabel smileFace, Mode gameMode, Timer mines, Timer timer) {
		this.parent = parent;
		this.mines = mines;
		this.timer = timer;
		this.smileFace = smileFace;
		this.gameMode = (gameMode == null) ? MainMenu.EASY : gameMode;
		initView();
		initMap();
		// initMines();
		
		setLocation(parent.getWidth() / 2 - getWidth() / 2, parent.getHeight() / 2 - getHeight() / 2);
		repaint();
	}
	
	/**
	* Initializes the view
	*/
	private void initView() {
		setBorder(new CompoundBorder(
		new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null),
		new BevelBorder(BevelBorder.LOWERED, null, null, null, null)),
		new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null),
		new BevelBorder(BevelBorder.RAISED, null, null, null, null))));
		
		setLayout(null);
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int i = e.getX() / tileSize;
				int j = e.getY() / tileSize;
				
				if (prevI != -1) map[prevI][prevJ].setIcon(Images.TILE_NOT_PRESSED);
				if (Tile.invalidateTile(map, i, j) || map[i][j].isFlagged() || GAMEOVER) return;
				
				map[i][j].setIcon(Images.getTileDigit(0));
				prevI = i;
				prevJ = j;
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int i = e.getX() / tileSize;
				int j = e.getY() / tileSize;
				prevI = -1;
				prevJ = -1;
				if (Tile.invalidateTile(map, i, j) || GAMEOVER) return;
				
				if (e.getButton() == MouseEvent.BUTTON1) { // Left mouse button
					if(firstClick){
						// initMines();
						initMinesNotSelected(i, j);
						firstClick = false;
					}
					boolean isOK = Tile.reveal(map, i, j);
					if (!isOK) gameLOST();
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right mouse button
					if (map[i][j].isFlagged()) {
						map[i][j].setFlag(false);
						mines.set(mines.getTime() + 1);
					} else {
						map[i][j].setFlag(true);
						mines.set(mines.getTime() - 1);
					}
				} 
				// else if (e.getButton() == MouseEvent.BUTTON2) { // Middle mouse button
				// 	System.out.println("something");
				// 	boolean isOK = true;
				// 	if ((map[i][j].getContent() == Tile.Content.NUMBER) && map[i][j].isRevealed()) {
				// 		System.out.println("something else");
				// 		isOK &= Tile.reveal(map, i-1, j-1);
				// 		isOK &= Tile.reveal(map, i-1, j);
				// 		isOK &= Tile.reveal(map, i-1, j+1);

				// 		isOK &= Tile.reveal(map, i, j-1);
				// 		isOK &= Tile.reveal(map, i, j+1);

				// 		isOK &= Tile.reveal(map, i+1, j-1);
				// 		isOK &= Tile.reveal(map, i+1, j);
				// 		isOK &= Tile.reveal(map, i+1, j+1);
				// 	}
				// 	if (!isOK) gameLOST();
				// }
				checkGameWon();
			}
		});
	}
	
	/**
	 * The game is won iff there are only tiles on the map that are:
	 *     ((not revealedand and is a bomb) or (is flagged and is a bomb)) and all other tiles are revealed
	 */
	private void checkGameWon() {
		for (int k = 0; k < map.length; k++) {
			for (int l = 0; l < map[k].length; l++) {
				if (
					(!map[k][l].isRevealed() && (map[k][l].getContent() != Tile.Content.MINE)) || 
					( map[k][l].isFlagged()  && (map[k][l].getContent() != Tile.Content.MINE))
				) {
					return;
				}
			}
		}
		gameWON();
	}
	
	/**
	* Initializes the map of empty tiles
	*/
	private void initMap() {
		map = new Tile[gameMode.getMapWidth()][gameMode.getMapHeight()];
		
		if (gameMode.equals(MainMenu.EASY)) {
			tileSize = 30;
		} else if (gameMode.equals(MainMenu.MEDIUM)) {
			tileSize = 25;
		} else if (gameMode.equals(MainMenu.HARD)) {
			tileSize = 20;
		} else if (gameMode.equals(MainMenu.CUSTOM)) {
			int w = parent.getWidth() / gameMode.getMapWidth();
			int h = parent.getHeight() / gameMode.getMapHeight();
			tileSize = (h < w) ? h : w;
		}
		
		setSize(tileSize * gameMode.getMapWidth() + 12, tileSize * gameMode.getMapHeight() + 12);
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				int tileX = 5 + i * tileSize;
				int tileY = 5 + j * tileSize;
				
				map[i][j] = new Tile(tileX, tileY, tileSize, tileSize);
				map[i][j].setContent(Tile.Content.EMPTY);
				add(map[i][j]);
			}
		}
	}
	
	/**
	* Initializes the mines
	*/
	private void initMines() {
		int count = 0;
		
		Random rd = new Random();
		while (count++ < gameMode.getMinesAmount()) {
			int w = rd.nextInt(gameMode.getMapWidth());
			int h = rd.nextInt(gameMode.getMapHeight());
			if (map[w][h].getContent().equals(Tile.Content.MINE)) {
				count--;
				continue;
			}
			map[w][h].setContent(Tile.Content.MINE);
			Tile.setNumeral(map, w - 1, h + 1);
			Tile.setNumeral(map, w - 1, h);
			Tile.setNumeral(map, w - 1, h - 1);
			Tile.setNumeral(map, w + 1, h + 1);
			Tile.setNumeral(map, w + 1, h);
			Tile.setNumeral(map, w + 1, h - 1);
			Tile.setNumeral(map, w, h - 1);
			Tile.setNumeral(map, w, h + 1);
		}
	}

	/**
	 * Fills the map with mines excluding the tile maked as the first one by i and j
	 * @param i
	 * @param j
	 */
	private void initMinesNotSelected(int i, int j) {
		do {
			for (int k = 0; k < map.length; k++) {
				for (int l = 0; l < map[k].length; l++) {
					map[k][l].setContent(Tile.Content.EMPTY);
				}
			}
			initMines();
		} while (map[i][j].getContent() == Tile.Content.MINE);
	}
	
	/**
	* Game lost handler
	*/
	public void gameLOST() {
		GAMEOVER = true;
		timer.off();
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Tile tile = map[i][j];
				if (tile.getContent() == Tile.Content.MINE && !tile.isFlagged()) {
					tile.setIcon(Images.MINE);
				}
				if (tile.isFlagged() && tile.getContent() != Tile.Content.MINE) {
					tile.setIcon(Images.MINE_NOT);
				}
			}
		}
		
		Image image1 = new ImageIcon(Images.SMILE_DEAD).getImage();
		Image newimg1 = image1.getScaledInstance(smileFace.getWidth(), smileFace.getHeight(), java.awt.Image.SCALE_SMOOTH);
		smileFace.setIcon(new ImageIcon(newimg1));
		
		Image image2 = new ImageIcon(Images.LOSE_SCREEN).getImage();
		Image newimg2 = image2.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		JOptionPane.showMessageDialog(this, "You have lost.", "Game Over", JOptionPane.OK_OPTION, new ImageIcon(newimg2));
	}
	
	/**
	* Game won handler
	*/
	public void gameWON() {
		GAMEOVER = true;
		timer.off();
		
		Image image1 = new ImageIcon(Images.SMILE_COOL).getImage();
		Image newimg1 = image1.getScaledInstance(smileFace.getWidth(), smileFace.getHeight(), java.awt.Image.SCALE_SMOOTH);
		smileFace.setIcon(new ImageIcon(newimg1));
		
		Image image2 = new ImageIcon(Images.WIN_SCREEN).getImage();
		Image newimg2 = image2.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		JOptionPane.showMessageDialog(this, "You have won.", "Game Over", JOptionPane.OK_OPTION, new ImageIcon(newimg2));
	}
	
	/**
	* Game difficulty setter
	*/
	public void setGameMode(Mode gameMode) {
		this.gameMode = gameMode;
	}
}