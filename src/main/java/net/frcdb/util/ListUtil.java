package net.frcdb.util;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various utilities for creating and manipulating lists.
 * @author timothyb89
 */
public class ListUtil {

	/**
	 * Creates a list of strings using the provided strings.
	 * @param strs The list of strings.
	 * @return A list containing the given strings.
	 */
	public static List<String> stringList(String... strs) {
		List<String> ret = new ArrayList<String>();

		for (String s : strs) {
			ret.add(s);
		}

		return ret;
	}

	/**
	 * "Safely" splits a string into several tokens. This returns a (possibly
	 * empty) list of tokens, split using the given regex.
	 * This accounts for exceptions you may normally get when working directly
	 * with String.split() (ex. the array being empty).
	 * @param text The text to split
	 * @param split The regex to split by (as used by String.split())
	 * @return A (possibly empty) list of Strings.
	 */
	public static List<String> safeSplit(String text, String split) {
		List<String> ret = new ArrayList<String>();
		if (!text.trim().isEmpty()) {
			try {
				for (String s : text.split(split)) {
					ret.add(s);
				}
			} catch (Exception ex) {
				ret.add(text);
			}
		}

		return ret;
	}

	public static List<String> safeSplit(String text, String split, int max) {
		List<String> start = safeSplit(text, split);

		if (start.size() > max) {
			List<String> ret = new ArrayList<String>();
			String buf = "";

			for (int i = 0; i < start.size(); i++) {
				if (i < max - 1) {
					ret.add(start.get(i));
				} else {
					buf += start.get(i) + split;
				}
			}

			buf = buf.substring(0, buf.length() - split.length());

			ret.add(buf);

			return ret;
		} else {
			return start;
		}
	}

	/**
	 * Concatenates each token in the list <code>strs</code>, separated by
	 * <code>separator</code>
	 * @param strs The list of Strings
	 * @param separator The token to separate the strings with
	 * @return A String a list of tokens, separated by the provided separator.
	 */
	public static String concat(List<String> strs, String separator) {
		String ret = "";

		for (int i = 0; i < strs.size(); i++) {
			ret = ret + strs.get(i);

			if (i < strs.size() - 1) {
				ret = ret + separator;
			}
		}

		return ret;
	}

	/**
	 * Creates a list from the given arguments.
	 * @param <T> The object type
	 * @param objs The list of objects to include in the list.
	 * @return The list of the given type.
	 */
	public static <T> List<?> createList(T... objs) {
		return Arrays.asList(objs);
	}

	/**
	 * Returns a randomly-choosen element from the given list
	 * @param <T> The list type
	 * @param l The list
	 * @return A random element from the given list.
	 */
	public static <T> T randomElement(List<T> l) {
		return l.get(new Random().nextInt(l.size()));
	}

	/**
	 * Gets the values of regex groups as a string list for the given text.
	 * The first value of the returned list the entire pattern.
	 * @param regex The regex to use, containing (groups)
	 * @param text The text to search in
	 * @return A list of strings containing the values in the groups, or null if
	 *     the regex does not occur in the text.
	 */
	public static List<String> extract(String regex, String text) {
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(text);

		if (mat.matches()) {
			List<String> ret = new ArrayList<String>();

			for (int i = 0; i < mat.groupCount() + 1; i++) {
				ret.add(mat.group(i));
			}

			return ret;
		} else {
			return null;
		}
	}

	/**
	 * Creates a copy of the specified collection
	 * @param <T> The type of collection
	 * @param collection the collection to copy
	 * @return A copy of the collection as a list
	 */
	public static <T> List<T> copy(Collection<T> collection) {
		List<T> ret = new ArrayList<T>();

		if (collection != null) {
			ret.addAll(collection);
		}

		return ret;
	}

	/**
	 * Converts a list of Objectify {@code Ref<?>}s into a list of only their
	 * values. This has the potential to be an extremely slow operation unless
	 * the list was declared with an {@code @Load} annotation.
	 * @param <T> the type of objects to load
	 * @param refs the list of {@code Refs} to resolve
	 * @return a list of the referenced values
	 */
	public static <T> List<T> resolveRefs(List<Ref<T>> refs) {
		List<T> ret = new ArrayList<T>(refs.size());
		
		for (Ref<T> ref : refs) {
			ret.add(ref.get());
		}
		
		return ret;
	}
	
	public static <T> List<Key<T>> refsToKeys(List<Ref<T>> refs) {
		List<Key<T>> ret = new ArrayList<Key<T>>(refs.size());
		
		for (Ref<T> ref : refs) {
			ret.add(ref.getKey());
		}
		
		return ret;
	}
	
}
