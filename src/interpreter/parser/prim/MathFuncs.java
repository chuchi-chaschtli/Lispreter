/**
 * MathFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.parser.Node;
import interpreter.parser.SExpression;
import interpreter.parser.util.Pat;

/**
 * Math operation functions that are already pre-packaged.
 * 
 * @author Anand
 *
 */
public class MathFuncs implements PrimitiveMarker {

	/**
	 * Checks if the address of the given S-Expression is an integer.
	 * 
	 * @param sexp
	 *            the S-Expression to evaluate.
	 * @return T or NIL if the given S-Expression is an integer.
	 */
	@Primitive(aliases = "integerp")
	public static Node integerp(SExpression sexp) {
		return Node.makeNode(Pat.ATOM_NUM.matches(sexp.getAddr().eval(true)
				.toString()));
	}

	/**
	 * The following function definitions are fairly straightforward. Simple
	 * integer evaluations for basic math operations.
	 */

	@Primitive(aliases = "+")
	public static Node plus(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr())
				+ toInteger(sexp.getData()));
	}

	@Primitive(aliases = "-")
	public static Node minus(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr())
				- toInteger(sexp.getData()));
	}

	@Primitive(aliases = "*")
	public static Node product(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr())
				* toInteger(sexp.getData()));
	}

	@Primitive(aliases = "/")
	public static Node quotient(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr())
				/ toInteger(sexp.getData()));
	}

	@Primitive(aliases = "rem")
	public static Node remainder(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr())
				% toInteger(sexp.getData()));
	}

	@Primitive(aliases = "mod")
	public static Node mod(SExpression sexp) {
		return Node.makeNode(Math.floorMod(toInteger(sexp.getAddr()),
				toInteger(sexp.getData())));
	}

	@Primitive(aliases = "expt")
	public static Node expt(SExpression sexp) {
		return Node.makeNode((int) Math.pow(toInteger(sexp.getAddr()),
				toInteger(sexp.getData())));
	}

	@Primitive(aliases = "<")
	public static Node less(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr()) < toInteger(sexp
				.getData()));
	}

	@Primitive(aliases = ">")
	public static Node greater(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr()) > toInteger(sexp
				.getData()));
	}

	@Primitive(aliases = "<=")
	public static Node leq(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr()) <= toInteger(sexp
				.getData()));
	}

	@Primitive(aliases = ">=")
	public static Node geq(SExpression sexp) {
		return Node.makeNode(toInteger(sexp.getAddr()) >= toInteger(sexp
				.getData()));
	}

	/**
	 * Convenience method to parse a Node into Integer format.
	 * 
	 * @param n
	 *            a Node to parse.
	 * @return an int.
	 * @throws NumberFormtException
	 *             if the Node was not parsed properly.
	 */
	private static int toInteger(Node n) {
		return Integer.parseInt(n.eval(true).toString());
	}
}
