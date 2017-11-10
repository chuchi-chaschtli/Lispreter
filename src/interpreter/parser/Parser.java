/**
 * Parser.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.util.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anand
 *
 */
public class Parser {

	private List<Tree> statements;

	public Parser(List<String> tokens) {
		statements = new ArrayList<>();
		List<String> tmp;
		int i = 0, j = 0, k = 0;
		while (i < tokens.size() && i > -1) {
			j = tokens.subList(i, tokens.size()).indexOf("(");
			if (j == i) {
				k = ListUtils.clauseEnd(ListUtils.subList(tokens, i, true)) + i;
			} else if (j > i) {
				k = j - 1;
			} else {
				k = tokens.size() - 1;
			}
			tmp = ListUtils.inDotNotation(ListUtils.subList(tokens, i, k + 1,
					true));
			statements.add(new Tree(tmp));
			i = k + 1;
		}
	}

	public void eval() {
		try {
			eval(System.out);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void eval(Appendable out) throws IOException {
		for (Tree t : statements) {
			out.append(t.eval()).append("\n");
		}
	}
}
