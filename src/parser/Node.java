/**
 * Node.java is a part of Lispreter. 
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import parser.util.Pat;
import exception.EnvironmentException;
import exception.NodeInitException;

/**
 * Main data structure for handling atoms and S-Expressions. A node maintains a
 * list of tokens which create the object.
 * <p>
 * The factory pattern is employed to create new Nodes, which is dependent on if
 * the Node is a valid S-Expression.
 * 
 * @author Anand
 *
 */
public abstract class Node {

	protected List<String> tokens = new ArrayList<>();
	protected Environment env;

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
	 * Determines whether this node is part of a list of expressions to be
	 * evaluated.
	 * 
	 * @return true if part of a list, false otherwise.
	 */
	protected abstract boolean isList();

	/**
	 * Evaluates this node.
	 * 
	 * @return the Node after evaluation.
	 * @throws EnvironmentException
	 *             if the node has no environment
	 */
	public abstract Node eval();

	/**
	 * Evaluates this node.
	 * 
	 * @param literal
	 *            whether or not numericals should be evaluated literally.
	 * @return the evaluated Node.
	 */
	public abstract Node eval(boolean literal);

	/**
	 * Sets the environment for this node.
	 * 
	 * @param env
	 *            the new Environment.
	 */
	public void setEnvironment(Environment env) {
		this.env = env;
	}

	/**
	 * Grabs this node's environment.
	 * 
	 * @return
	 */
	public Environment getEnvironment() {
		return env;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tokens);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Node other = (Node) obj;
		if (tokens == null && other.tokens != null) {
			return false;
		}
		return tokens.equals(other.tokens);
	}
}
