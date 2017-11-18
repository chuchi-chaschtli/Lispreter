/**
 * ListFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.FuncDefException;
import interpreter.parser.Atom;
import interpreter.parser.Environment;
import interpreter.parser.Node;
import interpreter.parser.SExpression;
import interpreter.parser.func.ClosureState;
import interpreter.util.Pat;

/**
 * @author Anand
 *
 */
public final class ListFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "car", "first" })
	public static Node car(Node n) {
		SExpression sexp;
		if (n.isList()) {
			sexp = (SExpression) n;
		} else {
			sexp = ((SExpression) n.eval());
		}
		return sexp.getAddr();
	}

	@Primitive(aliases = { "cdr", "rest" })
	public static Node cdr(Node n) {
		SExpression sexp;
		if (n.isList()) {
			sexp = (SExpression) n;
		} else {
			sexp = ((SExpression) n.eval());
		}
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
		Environment.getInstance().registerFunc(name, formals, body);

		return new Atom(name);
	}

	@Primitive(aliases = { "lambda", "λ" })
	public static Node lambda(SExpression sexp) {
		Node addr = sexp.getAddr();
		Node data = sexp.getData();
		Environment env = Environment.getInstance();

		// TODO : Cleanup and Bug-Fix
		ClosureState.changeState();
		if (ClosureState.getNextValue().isEmpty()) {
			ClosureState.setNextValue(addr.toString());
		}
		if (ClosureState.isEvaluatingLambda()
				&& addr.toString().equals(ClosureState.getNextValue())) {
			SExpression nested = new SExpression(data);
			ClosureState.setNextNode(env.registerAnon(nested.getAddr(),
					nested.getData()));
			ClosureState.setNextValue(addr.toString());
			return new Atom("lambda");
		}
		return env.execLamb(ClosureState.getNextNode(), data);
	}

	/**
	 * Carries out cons as according to the lisp function definition, which
	 * combines the CAR with the CADR of the arguments into a single flattened
	 * list.
	 * <p>
	 * If either argument fails, an exception is thrown during S-Expression
	 * construction.
	 * 
	 * @param sexp
	 *            S-Expression arguments.
	 * @return (cons (car sexp) (cadr sexp))
	 */
	@Primitive(aliases = "cons")
	public static Node cons(SExpression sexp) {
		Node cadr = new SExpression(sexp.getDataTokens()).getAddr();
		return new SExpression(sexp.getAddr().eval(), cadr.eval());
	}

	@Primitive(aliases = "list")
	public static Node list(SExpression sexp) {
		return new SExpression(sexp.getAddr(), sexp.getData());
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
	@Primitive(aliases = "cond")
	public static Node cond(SExpression sexp) {
		SExpression addr = new SExpression(sexp.getAddrTokens());
		if (addr.getAddr().eval().toString().equalsIgnoreCase("T")) {
			return new SExpression(addr.getDataTokens()).getAddr().eval(true);
		}
		return cond(new SExpression(sexp.getDataTokens()));
	}

	@Primitive(aliases = "if")
	public static Node ifelse(SExpression sexp) {
		SExpression dTokens = new SExpression(sexp.getDataTokens());
		if (Node.makeNode(sexp.getAddrTokens()).eval().toString().equals("T")) {
			return dTokens.getAddr().eval(true);
		}
		return new SExpression(dTokens.getData()).getAddr().eval(true);
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
