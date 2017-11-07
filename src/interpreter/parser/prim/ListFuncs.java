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

	@Primitive(aliases = { "cond" })
	public static Node cond(SExpression sexp) {
		SExpression addr = new SExpression(sexp.getAddrTokens());
		if (addr.getAddr().eval().toString().equals("T")) {
			return new SExpression(addr.getDataTokens()).getAddr().eval(true);
		}
		return cond(new SExpression(sexp.getDataTokens()));
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
