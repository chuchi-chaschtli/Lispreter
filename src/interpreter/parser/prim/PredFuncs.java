/**
 * PredFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.FuncDefException;
import interpreter.exception.NodeInitException;
import interpreter.parser.Node;
import interpreter.parser.SExpression;

/**
 * Basic predicate functions: Logical AND, NOT, OR.
 * 
 * @author Anand
 *
 */
public class PredFuncs implements PrimitiveMarker {

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
			return BoolFuncs.nil();
		}
		try {
			return and(new SExpression(sexp.getData()));
		}
		catch (NodeInitException e) {
			return BoolFuncs.t();
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
			return BoolFuncs.t();
		}
		try {
			return or(new SExpression(sexp.getData()));
		}
		catch (NodeInitException e) {
			return BoolFuncs.nil();
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
			evaluation = "NIL";
		} else if (evaluation.equals("NIL")) {
			evaluation = "T";
		} else {
			throw new FuncDefException("Cannot negate a non-boolean function");
		}
		return BoolFuncs.boolFuncFactory(evaluation);
	}
}
