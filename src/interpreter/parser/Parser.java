/**
 * Parser.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.parser.func.ClosureState;
import interpreter.util.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation for the Parser of the Interpreter. While the Lexer tokenizes a
 * program string into words, the Parser analyzes the grammar of the tokens and
 * evaluates the tokens accordingly. The output location for evaluation is
 * immutable.
 * 
 * @author Anand
 *
 */
public class Parser {

	private List<Tree> statements;
	private final Appendable output;

	/**
	 * Default constructor requires a collection of tokens, and passes the
	 * System console as the print stream.
	 * 
	 * @param tokens
	 *            a List of program tokens.
	 * @see #Parser(List, Appendable)
	 */
	public Parser(List<String> tokens) {
		this(tokens, System.out);
	}

	/**
	 * Constructor requires a collection of tokens and an output evaluation
	 * location. The tokens passed are analyzed and used to construct a Tree of
	 * parse statements.
	 * 
	 * @param tokens
	 *            a List of program tokens.
	 * @param output
	 *            a valid Appendable instance.
	 */
	public Parser(List<String> tokens, Appendable output) {
		this.statements = new ArrayList<>();
		this.output = output;
		List<String> tmp;
		int i = 0, j = 0, k = 0;
		while (i < tokens.size() && i > -1) {
			j = ListUtils
					.findFirstOpenParen(ListUtils.subList(tokens, i, true)) + i;
			if (j == i) {
				k = ListUtils.clauseEnd(ListUtils.subList(tokens, i, true)) + i
						+ 1;
			} else if (j > i) {
				k = j;
			} else {
				k = tokens.size();
			}
			tmp = ListUtils
					.inDotNotation(ListUtils.subList(tokens, i, k, true));
			statements.add(new Tree(tmp));
			i = k;
		}
	}

	/**
	 * Evaluates the parsing tree and sends the output to {@code output}.
	 * 
	 * @param sep
	 *            the separator between individual components.
	 * @return the Appendable object.
	 * @throws IOException
	 *             if an I/O error occurs from the Appendable object.
	 */
	public Appendable eval(String sep) {
		try {
			for (int i = 0; i < statements.size(); i++) {
				output.append(statements.get(i).eval()).append(
						i == statements.size() - 1 ? "" : sep);
				ClosureState.getInstance().reset();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Convenience method to evaluate the parsing tree and each evaluation
	 * occurs on a separate line.
	 * 
	 * @return the Appendable object.
	 * @see #eval(String)
	 */
	public Appendable eval() {
		return eval("\n");
	}
}
