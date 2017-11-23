/**
 * NodeFactory.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.exception.NodeInitException;
import interpreter.util.Pat;

import java.util.List;

/**
 * Factory delegation for Nodes.
 * 
 * @author Anand
 *
 */
public class NodeFactory {

	public static final Node FALSE = makeNode(false);
	public static final Node TRUE = makeNode(true);
	public static final Node LAMBDA = makeNode("lambda");

	/**
	 * Employs the factory pattern to create a new atom or s-expression.
	 * 
	 * @param list
	 *            the string list for the new node.
	 * @return a new Node object.
	 * @throws NodeInitException
	 *             if the node is empty, or the factory fails.
	 */
	public static Node makeNode(List<String> list) {
		if (list.size() > 0) {
			String first = list.get(0);
			if (Pat.PAREN_OPEN.matches(first)) {
				return new SExpression(list);
			} else {
				return new Atom(first);
			}
		}
		throw new NodeInitException("Attempted to create a dataless Node");
	}

	/**
	 * Adapts the factory pattern appropriately to create a boolean atom.
	 * 
	 * @param bool
	 *            the boolean literal.
	 * @return a new Atom
	 */
	public static Node makeNode(boolean bool) {
		return new Atom(bool);
	}

	/**
	 * Makes a boolean node from a string literal.
	 * 
	 * @param str
	 *            either T or NIL to be converted to a True or False atom,
	 *            respectively.
	 * @return a Boolean Node.
	 * @throws IllegalArgumentException
	 *             if anything other than T or NIL were specified.
	 */
	public static Node makeBooleanNode(String str) {
		if (str.equalsIgnoreCase("T")) {
			return NodeFactory.TRUE;
		} else if (str.equalsIgnoreCase("NIL")) {
			return NodeFactory.FALSE;
		}
		throw new IllegalArgumentException(
				"Boolean node invocation from string must be either T or NIL.");
	}

	/**
	 * Adapts the factory pattern appropriately to create an integer atom.
	 * 
	 * @param i
	 *            the integer literal.
	 * @return a new Atom
	 */
	public static Node makeNode(int i) {
		return new Atom(i);
	}

	/**
	 * Adapts the factory pattern appropriately to create a String atom.
	 * 
	 * @param i
	 *            the String literal.
	 * @return a new Atom
	 */
	public static Node makeNode(String str) {
		return new Atom(str);
	}
}
