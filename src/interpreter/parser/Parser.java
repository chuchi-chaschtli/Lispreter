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
			j = ListUtils.findFirstOpenParen(ListUtils.subList(tokens, i , true)) + i;
			if (j == i) {
				k = ListUtils.clauseEnd(ListUtils.subList(tokens, i, true)) + i + 1;
			} else if (j > i) {
				k = j;
			} else {
				k = tokens.size();
			}
			tmp = ListUtils.inDotNotation(ListUtils.subList(tokens, i, k,
					true));
			statements.add(new Tree(tmp));
			i = k;
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
