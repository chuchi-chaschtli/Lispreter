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

	public static int clauseEnd(List<String> list) {
		if (!Pat.PAREN_OPEN.matches(list.get(0))) {
			throw new IllegalArgumentException(
					"Could not find clause end for an expression that isn't a clause");
		}
		int opens = 1;
		int endIndex = 1;

		while (opens > 0) {
			if (endIndex >= list.size()) {
				throw new IllegalArgumentException("Imbalanced parens");
			}

			String endVal = list.get(endIndex);
			if (Pat.PAREN_OPEN.matches(endVal)) {
				opens++;
			} else if (Pat.PAREN_CLOSE.matches(endVal)) {
				opens--;
			}

			if (opens > 0) {
				endIndex++;
			}
		}
		return endIndex;
	}

	public static <T> List<T> subList(List<T> list, int start, int end,
			boolean copy) {
		if (copy) {
			return new ArrayList<T>(list.subList(start, end));
		}
		return list.subList(start, end);
	}

	public static <T> List<T> subList(List<T> list, int start, int end) {
		return subList(list, start, end, true);
	}

	public static <T> List<T> subList(List<T> list, int start, boolean copy) {
		return subList(list, start, list.size(), copy);
	}

	public static List<String> inDotNotation(List<String> list) {
		List<String> result = new ArrayList<>();
		List<String> tmp = new ArrayList<>();
		int next = 0;

		if (Pat.PAREN_OPEN.matches(list.get(0))) {
			int close = clauseEnd(list);
			if (close > 1) {
				result.add("(");
				if (close > 2) {
					if (Pat.PAREN_OPEN.matches(list.get(1))) {
						next = clauseEnd(subList(list, 1, close));
					}
					next += 2;

					result.addAll(inDotNotation(subList(list, 1, next)));
					result.add(".");
					if (list.get(next).equals(".")) {
						result.addAll(inDotNotation(subList(list, next + 1,
								close)));
					} else {
						tmp.add("(");
						tmp.addAll(subList(list, next, close, false));
						tmp.add(")");
						result.addAll(inDotNotation(tmp));
					}
				} else {
					result.add(list.get(1));
					result.add(".");
					result.add("NIL");
				}
				result.add(")");
			} else {
				result.add("NIL");
			}
		} else {
			int openParen = list.indexOf("(");
			if (list.contains("[") && list.indexOf("[") < openParen) {
				openParen = list.indexOf("[");
			}
			if (list.contains("{") && list.indexOf("{") < openParen) {
				openParen = list.indexOf("{");
			}
			if (openParen > 0) {
				result.addAll(subList(list, 0, openParen));
				result.addAll(inDotNotation(subList(list, openParen, true)));
			} else {
				result = list;
			}
		}
		return result;
	}
}
