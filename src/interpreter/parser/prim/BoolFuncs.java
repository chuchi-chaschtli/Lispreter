/**
 * BoolFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.parser.Atom;
import interpreter.parser.Node;
import interpreter.parser.SExpression;

/**
 * @author Anand
 *
 */
public class BoolFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "T", "true" }, sexpRequired = false)
	public static Node t() {
		return new Atom(true);
	}

	@Primitive(aliases = { "NIL", "false" }, sexpRequired = false)
	public static Node nil() {
		return new Atom(false);
	}

	/**
	 * Checks if the address of the given S-Expression is the same value as its
	 * data.
	 * <p>
	 * Note that while eq and equalp are slightly different in lisp, in Java
	 * more rigorous type-casting is required for differentiation.
	 * 
	 * @param sexp
	 *            the S-Expression to evaluate.
	 * @return T or NIL nodes.
	 */
	@Primitive(aliases = { "eq", "equalp" })
	public static Node eq(SExpression sexp) {
		return Node.makeNode(sexp.getAddr().eval(true).toString()
				.equals(sexp.getData().eval(true).toString()));
	}

	public static Node boolFuncFactory(String alias) {
		if (alias.matches("NIL|false")) {
			return nil();
		} else if (alias.matches("T|true")) {
			return t();
		}
		return null;
	}
}
