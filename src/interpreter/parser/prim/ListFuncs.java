/**
 * ListFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.FuncDefException;
import interpreter.parser.Atom;
import interpreter.parser.Environment;
import interpreter.parser.Node;
import interpreter.parser.SExpression;
import interpreter.parser.util.Pat;

/**
 * @author Anand
 *
 */
public class ListFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "car", "first" })
	public static Node car(SExpression sexp) {
		return sexp.getAddr();
	}

	@Primitive(aliases = { "cdr", "rest" })
	public static Node cdr(SExpression sexp) {
		return sexp.getData();
	}

	@Primitive(aliases = { "defun" })
	public static Node defun(SExpression sexp) {
		String name = sexp.getAddr().toString();
		if (!Pat.VALID_FUNC.matches(name)) {
			throw new FuncDefException(
					"Function names must be only char literals");
		}
		if (isPrimitiveRegistered(name)) {
			throw new FuncDefException("Cannot override a primitive function");
		}

		SExpression dTokens = new SExpression(sexp.getDataTokens());
		Node formals = Node.makeNode(dTokens.getAddrTokens());
		Node body = Node.makeNode(new SExpression(dTokens.getDataTokens())
				.getAddrTokens());
		Environment.getInstance().register(name, formals, body);

		return new Atom(name);
	}

	/**
	 * Evaluates a list of conditions until one's first evaluates to True, then
	 * returns the second item.
	 * 
	 * As per the lisp equivalent, an error will result if no 'arm' of the cond
	 * statement is a true expression.
	 * 
	 * @param sexp
	 *            an S-Expression describing a list of conditions.
	 * @return an evaluated expression in the list with the first true 'arm'.
	 */
	@Primitive(aliases = { "cond" })
	public static Node cond(SExpression sexp) {
		SExpression addr = new SExpression(sexp.getAddrTokens());
		if (addr.getAddr().eval().toString().equals("T")) {
			return new SExpression(addr.getDataTokens()).getAddr().eval(true);
		}
		return cond(new SExpression(sexp.getDataTokens()));
	}

	/**
	 * Quote returns the equivalent of CADR on the contained s-expression. In
	 * the context this function is operated in, the S-Expression data has been
	 * passed to other primitives, so we need only grab the address.
	 * 
	 * @param sexp
	 *            an S-Expression
	 * @return the address of {@code sexp}.
	 */
	@Primitive(aliases = { "quote", "'" })
	public static Node quote(SExpression sexp) {
		return sexp.getAddr();
	}

	private static boolean isPrimitiveRegistered(String alias) {
		for (String funcName : Environment.getInstance().getHandler()
				.getRegisteredFunctions().keySet()) {
			if (funcName.equals(alias)) {
				return true;
			}
		}
		return false;
	}
}
