/**
 * PredFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.FuncDefException;
import interpreter.exception.NodeInitException;
import interpreter.parser.Node;
import interpreter.parser.NodeFactory;
import interpreter.parser.SExpression;

/**
 * Basic predicate functions: Logical AND, NOT, OR.
 * 
 * @author Anand
 *
 */
public class PredFuncs {

	/**
	 * Computes the logical AND of all provided arguments.
	 * 
	 * @param sexp
	 *            the given S-Expression.
	 * @return T if all arguments evaluate to T, NIL otherwise.
	 */
	@Primitive(aliases = "and")
	public static Node and(SExpression sexp) {
		if (sexp.getAddr().eval(true).toString().equals("NIL")) {
			return NodeFactory.FALSE;
		}
		try {
			return and(new SExpression(sexp.getData()));
		}
		catch (NodeInitException e) {
			return NodeFactory.TRUE;
		}
	}

	/**
	 * Computes the logical OR of all provided arguments.
	 * 
	 * @param sexp
	 *            the given S-Expression.
	 * @return T if any argument evaluate to T, NIL otherwise.
	 */
	@Primitive(aliases = "or")
	public static Node or(SExpression sexp) {
		if (sexp.getAddr().eval(true).toString().equals("T")) {
			return NodeFactory.TRUE;
		}
		try {
			return or(new SExpression(sexp.getData()));
		}
		catch (NodeInitException e) {
			return NodeFactory.FALSE;
		}
	}

	/**
	 * Computes the logical NOT of the first argument.
	 * 
	 * @param sexp
	 *            the given S-Expression.
	 * @return T if the first argument evaluates to NIL, NIL if the first
	 *         argument evaluates to T.
	 * @throws FuncDefException
	 *             if the first argument is neither T nor NIL.
	 */
	@Primitive(aliases = "not")
	public static Node not(SExpression sexp) {
		String evaluation = sexp.getAddr().eval(true).toString();
		if (evaluation.equals("T")) {
			return NodeFactory.FALSE;
		} else if (evaluation.equals("NIL")) {
			return NodeFactory.TRUE;
		}
		throw new FuncDefException("Cannot negate a non-boolean function");

	}
}
