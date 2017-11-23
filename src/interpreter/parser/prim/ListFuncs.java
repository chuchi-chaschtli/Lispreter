/**
 * ListFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.FuncDefException;
import interpreter.parser.Environment;
import interpreter.parser.Node;
import interpreter.parser.NodeFactory;
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
			sexp = new SExpression(n);
		} else {
			sexp = new SExpression(n.eval());
		}
		return sexp.getAddr();
	}

	@Primitive(aliases = { "cdr", "rest" })
	public static Node cdr(Node n) {
		SExpression sexp;
		if (n.isList()) {
			sexp = new SExpression(n);
		} else {
			sexp = new SExpression(n.eval());
		}
		return NodeFactory.makeNode(sexp.getDataTokens());
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
		Node formals = NodeFactory.makeNode(dTokens.getAddrTokens());
		Node body = NodeFactory.makeNode(new SExpression(dTokens
				.getDataTokens()).getAddrTokens());

		Environment.getInstance().registerFunc(name, formals, body);

		return NodeFactory.makeNode(name);
	}

	@Primitive(aliases = { "lambda", "λ" })
	public static Node lambda(SExpression sexp) {
		Node addr = sexp.getAddr();
		Node data = sexp.getData();
		Environment env = Environment.getInstance();
		ClosureState cs = ClosureState.getInstance();

		cs.changeState();
		if (cs.getNextValue().isEmpty()) {
			cs.setNextValue(addr.toString());
		}

		if (cs.isEvaluatingLambda()
				&& addr.toString().equals(cs.getNextValue())) {
			SExpression nested = new SExpression(data);
			env.registerAnon(nested.getAddr(), nested.getData());
			cs.setNextValue(addr.toString());
			return NodeFactory.LAMBDA;
		}
		return env.execLamb(cs.getNextNode(), data);
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
		Node evalAddr = sexp.getAddr().eval(true);
		Node data = sexp.getData();
		if (data.isList()) {
			return new SExpression(evalAddr, list(new SExpression(data)));
		}
		return new SExpression(evalAddr, data);
	}

	@Primitive(aliases = "length")
	public static Node length(Node n) {
		if (!n.isList()) {
			return NodeFactory.makeNode(0);
		}
		SExpression sexp = new SExpression(n);
		return NodeFactory.makeNode(1 + Integer.valueOf(length(sexp.getData())
				.toString()));
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
		if (NodeFactory.makeNode(sexp.getAddrTokens()).eval().toString()
				.equals("T")) {
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
