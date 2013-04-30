package net.frcdb.util;

/**
 *
 * @author tim
 */
public class GeocodingException extends Exception {

	/**
	 * Creates a new instance of
	 * <code>GeocodingException</code> without detail message.
	 */
	public GeocodingException() {
	}

	/**
	 * Constructs an instance of
	 * <code>GeocodingException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public GeocodingException(String msg) {
		super(msg);
	}
}
