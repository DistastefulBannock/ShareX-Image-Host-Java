/**
 * 
 */
package best.yiff.host.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DistastefulBannock
 *
 */
public class StringUtils {
	
	/**
	 * Generates a random english string
	 * @param size How long the reandom string should be
	 * @return A random string
	 */
	public static String randomEnglishString(int size) {
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		String output = "";
		for (int i = 0; i < size; i++)
			output += chars[ThreadLocalRandom.current().nextInt(0, chars.length)];
		return output;
	}
	
}
