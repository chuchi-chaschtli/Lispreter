/**
 * PredFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.FuncDefException;
import interpreter.parser.Node;
import interpreter.parser.SExpression;

/**
 * @author Anand
 *
 */
public class PredFuncs implements PrimitiveMarker {

	@Primitive(aliases = "and")
	public static Node and(SExpression sexp) {
		if (sexp.getAddr().eval(true).toString().equals("NIL")) {
			return BoolFuncs.nil();
		}
		if (sexp.getDataTokens().isEmpty()) {
			return BoolFuncs.t();
		}
		return and(new SExpression(sexp.getData()));
	}

	@Primitive(aliases = "or")
	public static Node or(SExpression sexp) {
		if (sexp.getAddr().eval(true).toString().equals("T")) {
			return BoolFuncs.t();
		}
		if (sexp.getDataTokens().isEmpty()) {
			return BoolFuncs.nil();
		}
		return and(new SExpression(sexp.getData()));
	}

	@Primitive(aliases = "not")
	public static Node not(SExpression sexp) {
		String evaluation = sexp.getAddr().eval(true).toString();
		Node n = BoolFuncs.boolFuncFactory(evaluation);
		if (n == null) {
			throw new FuncDefException("Cannot negate a non-boolean function");
		}
		return n;
	}
}
