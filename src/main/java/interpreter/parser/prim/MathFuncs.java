/**
 * MathFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.exception.ArithmeticZeroError;
import interpreter.exception.NodeInitException;
import interpreter.parser.Node;
import interpreter.parser.NodeFactory;
import interpreter.parser.SExpression;
import interpreter.util.Pat;

/**
 * Math operation functions that are already pre-packaged.
 * 
 * @author Anand
 *
 */
public final class MathFuncs {

	/**
	 * Checks if the address of the given S-Expression is an integer.
	 * 
	 * @param n
	 *            the S-Expression to evaluate.
	 * @return T or NIL if the given S-Expression is an integer.
	 */
	@Primitive(aliases = "integerp")
	public static Node integerp(Node n) {
		Node toEval = n;
		try {
			SExpression sexp = (SExpression) n;
			toEval = sexp.getAddr();
		}
		catch (Exception e) {}
		return NodeFactory.makeNode(Pat.ATOM_NUM.matches(toEval.eval(true)
				.toString()));
	}

	/**
	 * The following function definitions are fairly straightforward. Simple
	 * integer evaluations for basic math operations.
	 */

	@Primitive(aliases = { "sum", "+" })
	public static Node plus(SExpression sexp) {
		Node evaluatedAddr = sexp.getAddr().eval();
		if (evaluatedAddr.isList()) {
			return plus(new SExpression(evaluatedAddr));
		}
		Node data = sexp.getData();

		int sum = toInteger(sexp.getAddr());
		if (!data.eval(true).toString().matches("NIL")) {
			try {
				sum += toInteger(plus(new SExpression(data)));
			}
			catch (NodeInitException e) {
				sum += toInteger(data);
			}
		}
		return NodeFactory.makeNode(sum);
	}

	@Primitive(aliases = { "difference", "-" })
	public static Node minus(SExpression sexp) {
		Node evaluatedAddr = sexp.getAddr().eval();
		if (evaluatedAddr.isList()) {
			return minus(new SExpression(evaluatedAddr));
		}
		Node data = sexp.getData();

		int diff = toInteger(sexp.getAddr());
		if (!data.eval(true).toString().matches("NIL")) {
			try {
				diff -= toInteger(minus(new SExpression(data)));
			}
			catch (NodeInitException e) {
				diff -= toInteger(data);
			}
		}
		return NodeFactory.makeNode(diff);
	}

	@Primitive(aliases = { "product", "*" })
	public static Node product(SExpression sexp) {
		Node evaluatedAddr = sexp.getAddr().eval();
		if (evaluatedAddr.isList()) {
			return product(new SExpression(evaluatedAddr));
		}
		Node data = sexp.getData();

		int prod = toInteger(sexp.getAddr());
		if (!data.eval(true).toString().matches("NIL")) {
			try {
				prod *= toInteger(product(new SExpression(data)));
			}
			catch (NodeInitException e) {
				prod *= toInteger(data);
			}
		}
		return NodeFactory.makeNode(prod);
	}

	@Primitive(aliases = { "quotient", "/" })
	public static Node quotient(SExpression sexp) {
		Node evaluatedAddr = sexp.getAddr().eval();
		if (evaluatedAddr.isList()) {
			return quotient(new SExpression(evaluatedAddr));
		}
		Node data = sexp.getData();

		int quot = toInteger(sexp.getAddr());
		if (!data.eval(true).toString().matches("NIL")) {
			int dividend;
			try {
				dividend = toInteger(quotient(new SExpression(data)));
			}
			catch (NodeInitException e) {
				dividend = toInteger(data);
			}
			checkZero(dividend);
			quot /= dividend;
		}
		return NodeFactory.makeNode(quot);
	}

	@Primitive(aliases = "rem")
	public static Node remainder(SExpression sexp) {
		return NodeFactory.makeNode(toInteger(sexp.getAddr())
				% checkZero(toInteger(sexp.getData())));
	}

	@Primitive(aliases = "mod")
	public static Node mod(SExpression sexp) {
		return NodeFactory.makeNode(Math.floorMod(toInteger(sexp.getAddr()),
				checkZero(toInteger(sexp.getData()))));
	}

	@Primitive(aliases = { "expt", "^" })
	public static Node expt(SExpression sexp) {
		return NodeFactory.makeNode((int) Math.pow(toInteger(sexp.getAddr()),
				toInteger(sexp.getData())));
	}

	@Primitive(aliases = { "less", "<" })
	public static Node less(SExpression sexp) {
		return NodeFactory.makeNode(toInteger(sexp.getAddr()) < toInteger(sexp
				.getData()));
	}

	@Primitive(aliases = { "greater", ">" })
	public static Node greater(SExpression sexp) {
		return NodeFactory.makeNode(toInteger(sexp.getAddr()) > toInteger(sexp
				.getData()));
	}

	@Primitive(aliases = { "leq", "<=" })
	public static Node leq(SExpression sexp) {
		return NodeFactory.makeNode(toInteger(sexp.getAddr()) <= toInteger(sexp
				.getData()));
	}

	@Primitive(aliases = { "geq", ">=" })
	public static Node geq(SExpression sexp) {
		return NodeFactory.makeNode(toInteger(sexp.getAddr()) >= toInteger(sexp
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

	private static int checkZero(int x) {
		if (x == 0) {
			throw new ArithmeticZeroError();
		}
		return x;
	}

}
