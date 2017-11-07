/**
 * Analyzer.java is a part of Lispreter. 
 */
package interpreter.lexer;

import interpreter.util.Pat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lexer for the Lisp class. Deconstructs meaningful symbols and expression in a
 * Lisp program and places them into a List of tokens for parsing.
 * 
 * @author Anand
 *
 */
public final class Analyzer {

	private final String prog;
	private List<String> tokens = new ArrayList<>();

	/**
	 * Creates a lexical analyzer object with an input stream, which is then
	 * tokenized.
	 * 
	 * @param stream
	 *            a valid input stream.
	 * @throws IOException
	 *             if the input stream could not be read.
	 */
	public Analyzer(InputStream stream) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = stream.read(buffer)) != -1) {
			byteStream.write(buffer, 0, length);
		}
		prog = byteStream.toString("UTF-8");
		tokenize();
	}

	/**
	 * Builds a lexical analyzer object with a program string.
	 * 
	 * @param input
	 *            a String.
	 */
	public Analyzer(String input) {
		prog = input;
		tokenize();
	}

	/**
	 * Grabs a read-only copy of the token list.
	 * 
	 * @return a list of tokens.
	 */
	public List<String> getTokens() {
		return Collections.unmodifiableList(tokens);
	}

	/**
	 * Splits the program string into semantically defined parts, as defined by
	 * the Pattern matcher.
	 * 
	 * @see Pat#matches(String)
	 * @see Pat#matches(char)
	 */
	private void tokenize() {
		List<String> tokenList = new ArrayList<>();
		if (prog.length() == 1) {
			tokenList.add(prog);
		} else {
			int counter = 0;
			while (counter < prog.length()) {
				int next = counter + 1;
				char c = prog.charAt(counter);

				if (Pat.LETTER.matches(c) || Pat.ATOM_NUM.matches(c)) {
					while (Pat.LITERAL.matches(prog
							.substring(counter, next + 1))
							|| Pat.ATOM_NUM.matches(prog.substring(counter,
									next + 1))) {
						next++;
					}
					tokenList.add(prog.substring(counter, next));
				} else if (Pat.SYMBOL.matches(c)) {
					tokenList.add("" + c);
				}
				counter = next;
			}
		}
		tokens = tokenList;
	}
}
