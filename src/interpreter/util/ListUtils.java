/**
 * ListUtils.java is a part of Lispreter. 
 */
package interpreter.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anand
 *
 */
public final class ListUtils {

	private ListUtils() {
		throw new AssertionError("Cannot init util constructor");
	}

	/**
	 * Joins a list of strings, using the given imploder as a glue. Internally
	 * uses a StringBuffer to improve runtime.
	 * 
	 * @param list
	 *            the list of string objects.
	 * @param imploder
	 *            the glue for the result string.
	 * @return a String containing each string in the given list, separated by
	 *         the given imploder.
	 */
	public static String concat(List<String> list, String imploder) {
		StringBuffer buffy = new StringBuffer();

		for (String str : list) {
			buffy.append(str).append(imploder);
		}
		buffy.setLength(buffy.length() - 1);
		return buffy.toString();
	}

	/**
	 * Converts a string into a list of characters, with each character being
	 * represented as a string.
	 * 
	 * @param input
	 *            the given string.
	 * @return a list of each character in the string.
	 */
	public static List<String> listify(String input) {
		List<String> result = new ArrayList<>(input.length());

		for (char c : input.toCharArray()) {
			result.add(String.valueOf(c));
		}
		return result;
	}
}
