/**
 * Pat.java is a part of Lispreter. 
 */
package parser.util;

/**
 * Contains useful regular expression patterns for symbol and literal
 * validation.
 * 
 * @author Anand
 *
 */
public enum Pat {

	ATOM_NUM("[\\d\\+\\-]?[\\d]*"),
	LETTER("[a-zA-Z]"),
	LITERAL("[a-zA-Z0-9]+?"),
	SYMBOL("[().]"),
	VALID_FUNC("[a-zA-Z]{1}[a-zA-Z0-9]*"),
	WHITESPACE("[\\s]+?");

	private String regex;

	Pat(String regex) {
		this.regex = regex;
	}

	/**
	 * Does the given string match the current regex pattern?
	 * 
	 * @param input
	 *            the string to match.
	 * @return true if the input follows the specified regex pattern.
	 */
	public boolean matches(String input) {
		return input.matches(regex);
	}
}
