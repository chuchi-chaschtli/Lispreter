/**
 * Tree.java is a part of Lispreter. 
 */
package interpreter.parser;

import java.util.List;

/**
 * Parsing tree for statement evaluation. A parse tree is given a root node to
 * evaluate, and a cascading effect subsequently occurs.
 * 
 * @author Anand
 *
 */
public final class Tree {

	private final Node root;

	/**
	 * Constructs a Tree from a list of tokens, by initializing the root node.
	 * 
	 * @param outer
	 *            a List of Strings.
	 */
	protected Tree(List<String> outer) {
		root = NodeFactory.makeNode(outer);
	}

	protected String eval() {
		return root.eval().toString();
	}

	@Override
	public String toString() {
		return root.toString();
	}
}
