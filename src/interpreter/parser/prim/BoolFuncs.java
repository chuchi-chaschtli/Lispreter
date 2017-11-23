/**
 * BoolFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.parser.Node;
import interpreter.parser.NodeFactory;
import interpreter.parser.SExpression;
import interpreter.util.Pat;

/**
 * @author Anand
 *
 */
public final class BoolFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "T", "true" }, sexpRequired = false)
	public static Node t() {
		return NodeFactory.TRUE;
	}

	@Primitive(aliases = { "NIL", "false" }, sexpRequired = false)
	public static Node nil() {
		return NodeFactory.FALSE;
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
	@Primitive(aliases = { "eq", "equalp", "=" })
	public static Node eq(SExpression sexp) {
		return NodeFactory.makeNode(sexp.getAddr().eval(true).toString()
				.equals(sexp.getData().eval(true).toString()));
	}

	/**
	 * Checks if the data of the given S-Expression is NIL.
	 * 
	 * @param sexp
	 *            the S-Expression to evaluate.
	 * @return T or NIL if the S-Expression is NIL.
	 */
	@Primitive(aliases = { "null", "endp" })
	public static Node endp(SExpression sexp) {
		Node addr = sexp.getAddr();
		boolean bool = addr.eval().toString().equals("NIL");
		if (addr.isList()) {
			bool = new SExpression(addr).getData().eval(true).toString()
					.equals("NIL");
		}
		return NodeFactory.makeNode(bool);
	}

	@Primitive(aliases = { "atom" })
	public static Node atom(SExpression sexp) {
		return NodeFactory.makeNode(Pat.LITERAL.matches(sexp.getAddr().eval()
				.toString()));
	}
}
