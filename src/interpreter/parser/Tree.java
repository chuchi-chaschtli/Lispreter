/**
 * Tree.java is a part of Lispreter. 
 */
package interpreter.parser;

import java.util.List;

/**
 * @author Anand
 *
 */
public final class Tree {

	private final Node root;

	public Tree(List<String> outer) {
		root = Node.makeNode(outer);
	}

	protected String eval() {
		return root.eval().toString();
	}

	@Override
	public String toString() {
		return root.toString();
	}
}
