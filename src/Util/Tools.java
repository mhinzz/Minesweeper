package Util;

public class Tools {
	
	/**
	 * @param latency the amount of time to sleep the thread in milliseconds
	 */
	public static void Wait(int latency) {
		try {
			Thread.sleep(latency);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param number
	 * @return the number of digits in number
	 */
	public static int numOfDigits(int number) {
		return (int) (Math.log10(number) + 1);
	}
	
	/**
	 * @param number
	 * @param fromLeft flag used for determining whether user wants to get n^th digit from the right of from the left.
	 *                 Digit is from the left if True, right otherwise.
	 * @param n th digit
	 * @return the n^th digit of the given number starting from 1
	 */
	private static int getDigit(int number, int n, boolean fromLeft) {
		if (number < 0) number *= -1;
		int len = numOfDigits(number);
		if (n > len) return -1;
		if (fromLeft) return (int) (number / Math.pow(10, len - n)) % 10;
		else return (int) (number / Math.pow(10, n - 1)) % 10;
	}
	
	/**
	 * @param number the given number
	 * @param n th digit
	 * @return the n^th digit of the given number from the left starting from 1
	 */
	public static int getDigit(int number, int n) {
		return getDigit(number, n, true);
	}
	
	/**
	 * @param n
	 * @return whether the giving number is a number 'n' in this range: [0, 9]
	 */
	public static boolean isDigit(int n) {
		return n >= 0 && n <= 9;
	}
	
	/**
	 * @param str1
	 * @param str2
	 * @return True if the strings are equivalent
	 */
	public static boolean equals(String str1, String str2) {
		return str1.equals(str2);
	}
	
	/**
	 * @param str1
	 * @param str2
	 * @return True if the equality of 2 string while ignoring letter case
	 */
	public static boolean equalsNoCase(String str1, String str2) {
		return str1.toLowerCase().equals(str2.toLowerCase());
	}
	
	/**
	 * Method for testing whether the given number is within the given range
	 * 
	 * @param number			the given number
	 * @param includedFromLeft	if true include the left boundary, otherwise exclude
	 * @param leftBound			left boundary
	 * @param rightBound		right boundary
	 * @param includedFromRight	if true include the right boundary, otherwise exclude
	 * 
	 * @return true if the number is in range
	 * 
	 * @Possible_Cases:
	 * 		(n, true, a, b, true) -> test( n in [a, b] )
	 * 		(n, true, a, b, false) -> test( n in [a, b) )
	 * 		(n, false, a, b, true) -> test( n in (a, b] )
	 * 		(n, false, a, b, false) -> test( n in (a, b) )
	 */
	public static boolean isInRange(int number, boolean includedFromLeft, int leftBound, int rightBound, boolean includedFromRight) {
		if (number > leftBound && number < rightBound) return true;			// strictly in range
		else if (includedFromLeft && number == leftBound) return true;		// left boundary is the number
		else if (includedFromRight && number == rightBound) return true;	// right boundary is the number
		else return false;													// not in range
	}
}
