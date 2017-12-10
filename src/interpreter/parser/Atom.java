/**
 * Atom.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.exception.EnvironmentException;
import interpreter.exception.NodeInitException;
import interpreter.util.Pat;

import java.util.Map;

/**
 * Atoms are elements of a Lisp program which are strictly alphanumeric
 * literals. If a string is alphanumeric, it must be formed with a leading
 * alphabetic character. If the leading character is numeric, the entire literal
 * must be strictly numeric.
 * <p>
 * The Patterns class allows us to access common regular expressions to
 * appropriately assign well-formed atoms.
 * 
 * @author Anand
 *
 */
public class Atom extends Node {

	private final String lit;

	/**
	 * Creates an an atom from a well-formed string.
	 * 
	 * @param lit
	 *            the literal string to make the atom.
	 * @throws NodeInitException
	 *             if the string is not well formed.
	 */
	protected Atom(String lit) {
		if (!(Pat.LITERAL.matches(lit)
				|| Pat.ATOM_NUM.matches(lit)
				|| Pat.SYMBOL.matches(lit)
				|| (Pat.STRING_ID.matches(lit.charAt(0)) && Pat.STRING_ID
						.matches(lit.charAt(lit.length() - 1))) || Pat.RELATIONAL_OP
					.matches(lit))) {
			throw new NodeInitException("Invalid atom specified");
		}
		tokens.add(this.lit = lit);
	}

	/**
	 * Constructs a T or NIL atom from a predicate.
	 * 
	 * @param bool
	 *            the boolean flag.
	 */
	protected Atom(boolean bool) {
		tokens.add(lit = bool ? "T" : "NIL");
	}

	/**
	 * Constructs an atom from a primitive integer. The integer is just
	 * converted to a string for its literal representation.
	 * 
	 * @param i
	 */
	protected Atom(int i) {
		tokens.add(lit = Integer.toString(i));
	}

	@Override
	public boolean isList() {
		return NodeFactory.FALSE.equals(this);
	}

	@Override
	public Node eval() {
		Environment env = Environment.getInstance();
		if (env == null) {
			throw new EnvironmentException(
					"The node specified has no environment");
		}
		if (env.isDefinedV(lit)) {
			return env.getVariableValue(lit);
		}
		return this;
	}

	@Override
	public Node eval(boolean literal) {
		return eval();
	}

	@Override
	public Node eval(boolean literal, Map<String, Node> env) {
		return eval();
	}

	@Override
	public Node eval(Map<String, Node> env) {
		return eval();
	}

	/**
	 * Grabs the atomic literal. Removes positive redundance (arabic numbers can
	 * be expressed without preceding '+' signs).
	 */
	@Override
	public String toString() {
		if (Pat.ATOM_NUM.matches(lit)) {
			return lit.replaceAll("\\A\\+", "");
		}
		return lit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lit == null) ? 0 : lit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Atom other = (Atom) obj;
		if (lit == null && other.lit != null) return false;
		return lit.equals(other.lit);
	}
}
