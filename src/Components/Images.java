package Components;

import Errors.BadInputException;
import Util.Tools;

public interface Images {
	static final String ROOT 					= ".//images";
	static final String FORMAT 					= ".png";

	static final String GAME_ICON				= ROOT + "//game_icon"		+ FORMAT;
	
	public static final String WIN_SCREEN		= ROOT + "//win_icon"		+ FORMAT;
	public static final String LOSE_SCREEN		= ROOT + "//lose_icon"		+ FORMAT;
	
	public static final String MINE				= ROOT + "//tiles//mine1"	+ FORMAT;
	public static final String MINE_BLOWN		= ROOT + "//tiles//mine2"	+ FORMAT;
	public static final String MINE_NOT			= ROOT + "//tiles//mine3"	+ FORMAT;
	
	public static final String SMILE_COOL		= ROOT + "//smile//1"		+ FORMAT;
	public static final String SMILE_DEAD		= ROOT + "//smile//2"		+ FORMAT;
	public static final String SMILE_OOO		= ROOT + "//smile//3"		+ FORMAT;
	public static final String SMILE_HAPPY		= ROOT + "//smile//4"		+ FORMAT;
	public static final String SMILE_HAPPY_PRESS= ROOT + "//smile//5"		+ FORMAT;
	
	public static final String TILE_NOT_PRESSED	= ROOT + "//tiles//tile1"	+ FORMAT;
	public static final String TILE_PRESSED		= ROOT + "//tiles//tile2"	+ FORMAT;
	public static final String FLAG				= ROOT + "//tiles//flag"	+ FORMAT;
	
	/**
	 * Gets the Nine-Digit-Display path for the specified number
	 * @param num
	 * @return
	 */
	public static String getNSDDigit(int num) {
		if (!Tools.isDigit(num))
		throw new BadInputException("Entered number must be in range [0; 9]");
		return ROOT + "//nsd//" + num + FORMAT;
	}
	
	/**
	 * Gets the file path of the tile specified by the by num
	 * @param num
	 * @return
	 */
	public static String getTileDigit(int num) {
		if (!Tools.isDigit(num) && num != 9)
		throw new BadInputException("Entered number must be in range [0; 8]");
		return ROOT + "//tiles//" + num + FORMAT;
	}
}
