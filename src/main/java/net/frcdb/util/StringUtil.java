package net.frcdb.util;

import java.security.MessageDigest;

/**
 *
 * @author tim
 */
public class StringUtil {

	public static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public static String capitalizeWords(String s) {
		String ret = "";

		for (String part : ListUtil.safeSplit(s, " ")) {
			ret += capitalize(part) + " ";
		}

		if (ret.length() > 0) {
			ret = ret.substring(0, ret.length() - 1);
		}

		return ret;
	}

	public static String SHA1(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.reset();
			md.update(str.getBytes());
			byte[] digest = md.digest();

			String hex = "";
			for (int i = 0; i < digest.length; i++) {
				hex += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
			}

			return hex.toLowerCase();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String escape(String input) {
		if (input == null) {
			return null;
		}
		
		input = input.replace("<", "&lt;");
		input = input.replace(">", "&gt;");
		input = input.replace("&", "&amp;");
		
		return input;
	}
	
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	/**
	 * Computes the Levenshtein distance for the given 2 strings. From
	 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
	 * @param str1
	 * @param str2
	 * @return 
	 */
	public static int levenshteinDistance(CharSequence str1,
			CharSequence str2) {
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++) {
			distance[i][0] = i;
		}
		for (int j = 0; j <= str2.length(); j++) {
			distance[0][j] = j;
		}

		for (int i = 1; i <= str1.length(); i++) {
			for (int j = 1; j <= str2.length(); j++) {
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]
						+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
						: 1));
			}
		}

		return distance[str1.length()][str2.length()];
	}
	
}
