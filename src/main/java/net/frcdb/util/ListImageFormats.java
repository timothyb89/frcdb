package net.frcdb.util;

import javax.imageio.ImageIO;

/**
 *
 * @author tim
 */
public class ListImageFormats {
	
	public static void main(String[] args) {
		for (String s : ImageIO.getWriterFormatNames()) {
			System.out.println(s);
		}
	}
	
}
