/**
 * Analyzer.java is a part of Lispreter. 
 */
package interpreter.lexer;

import interpreter.exception.MalformedTextException;
import interpreter.util.Pat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Lexer for the Lisp class. Deconstructs meaningful symbols and expression in a
 * Lisp program and places them into a List of tokens for parsing.
 * 
 * @author Anand
 *
 */
public final class Lexer {

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
	public Lexer(InputStream stream) throws IOException {
		StringBuffer buffy = new StringBuffer();
		Scanner scan = new Scanner(stream);
		boolean blockCommentMode = false;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.contains("#|")) {
				if (!blockCommentMode) {
					blockCommentMode = true;
				}
				continue;
			} else if (line.contains("|#")) {
				if (!blockCommentMode) {
					scan.close();
					throw new MalformedTextException(
							"There is no beginning block comment");
				}
				blockCommentMode = false;
				buffy.append(line.substring(line.indexOf("|#") + 2,
						line.length()));
				continue;
			}
			if (blockCommentMode) {
				continue;
			}
			int commentIndex = line.indexOf(';');
			if (commentIndex < 0) {
				commentIndex = line.length();
			}
			buffy.append(line.substring(0, commentIndex));
		}
		prog = buffy.toString();
		scan.close();
		// ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		// byte[] buffer = new byte[1024];
		// int length = 0;
		// while ((length = stream.read(buffer)) != -1) {
		// byteStream.write(buffer, 0, length);
		// }
		// prog = byteStream.toString("UTF-8");
		tokenize();
	}

	/**
	 * Builds a lexical analyzer object with a program string.
	 * 
	 * @param input
	 *            a String.
	 */
	public Lexer(String input) {
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
		if (prog.length() == 1) {
			tokens.add(prog);
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
					tokens.add(prog.substring(counter, next));
				} else if (Pat.SYMBOL.matches(c)) {
					if (counter + 1 < prog.length()
							&& Pat.RELATIONAL_OP.matches(prog.substring(
									counter, counter + 2))) {
						tokens.add(prog.substring(counter, counter + 2));
						next++;
					} else {
						tokens.add("" + c);
					}
				} else if (Pat.STRING_ID.matches(c)) {
					next = prog.indexOf("\"", counter + 1) + 1;
					tokens.add(prog.substring(counter, next));
				}
				counter = next;
			}
		}
	}
}
