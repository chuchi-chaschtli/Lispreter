/**
 * SExpression.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.exception.NodeInitException;
import interpreter.parser.prim.BoolFuncs;
import interpreter.parser.prim.ListFuncs;
import interpreter.util.ListUtils;
import interpreter.util.Pat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Representation for S-Expressions. Construction and evaluation from various
 * input sources are also handled. Stipulation for list and dot notation is also
 * managed.
 * 
 * @author Anand
 *
 */
public class SExpression extends Node {

	// address and data tokens
	private Node data, addr;
	private List<String> dataTokens, addrTokens;

	/**
	 * Constructs an S-Expression from a token list. Uses our cons builder to
	 * validate input.
	 * 
	 * @param tokens
	 *            the list of tokens to build
	 */
	public SExpression(List<String> tokens) {
		makeCons(tokens);
	}

	/**
	 * Constructs an S-Expression from a given node. Essentially a casting
	 * operation.
	 * 
	 * @param n
	 *            the Node to try and force into an S-Expression
	 */
	public SExpression(Node n) {
		this(n.tokens);
	}

	/**
	 * Builds an S-Expression from two nodes, by making one an address and
	 * another a data object. Accordingly, the tokens are then updated.
	 * 
	 * @param addr
	 *            the new address Node.
	 * @param data
	 *            the new data Node.
	 */
	public SExpression(Node addr, Node data) {
		this.addr = addr;
		this.data = data;
		this.addrTokens = addr.tokens;
		this.dataTokens = data.tokens;
		buildTokens();

	}

	/**
	 * Deep-copy constructor for an S-Expression.
	 * 
	 * @param sexp
	 *            the to-copy S-Expression.
	 */
	public SExpression(SExpression sexp) {
		data = Node.makeNode(sexp.dataTokens);
		addr = Node.makeNode(sexp.addrTokens);
		dataTokens = new ArrayList<String>(sexp.dataTokens);
		addrTokens = new ArrayList<String>(sexp.addrTokens);
	}

	/**
	 * Appropriately updates tokens, determines address and data tokens, and
	 * validifies the representation of a given S-Expression, in the form of a
	 * string list.
	 * 
	 * @param tokens
	 */
	private void makeCons(List<String> tokens) {
		if (tokens.size() > 0 && Pat.PAREN_OPEN.matches(tokens.get(0))) {
			int index = 1;
			int dataBegin = 3;
			if (Pat.PAREN_OPEN.matches(tokens.get(index))) {
				int numClausesOpen = 1;
				while (numClausesOpen > 0 && index < tokens.size()) {
					index++;
					if (Pat.PAREN_OPEN.matches(tokens.get(index))) {
						numClausesOpen++;
					} else if (Pat.PAREN_CLOSE.matches(tokens.get(index))) {
						numClausesOpen--;
					}
				}
				dataBegin = index + 1;
			}
			index = dataBegin > 3 ? ListUtils.subList(tokens, dataBegin, true)
					.indexOf(".") + dataBegin : 2;

			addrTokens = ListUtils.subList(tokens, 1, index, true);
			dataTokens = ListUtils.subList(tokens, index + 1,
					tokens.size() - 1, true);

			data = Node.makeNode(dataTokens);
			addr = Node.makeNode(addrTokens);
			buildTokens();
			return;
		}
		throw new NodeInitException("Invalid S-Expression provided : "
				+ tokens.toString());
	}

	/**
	 * Formats the list of address and data tokens with surrounding parentheses,
	 * and separates the address and data with a period.
	 */
	private void buildTokens() {
		tokens.add("(");
		tokens.addAll(addrTokens);
		tokens.add(".");
		tokens.addAll(dataTokens);
		tokens.add(")");
	}

	@Override
	public boolean isList() {
		return data.toString().matches("NIL") || data.isList();
	}

	@Override
	public Node eval() {
		return eval(false);
	}

	@Override
	public Node eval(boolean literal) {
		String ad = addr.eval().toString();
		Node formals = null;
		Environment env = Environment.getInstance();

		if (literal && Pat.ATOM_NUM.matches(ad)) {
			return addr.eval();
		} else if (ad.matches("T|NIL")) {
			return BoolFuncs.boolFuncFactory(ad);
		} else if (env.isDefinedV(ad)) {
			return env.getVariableValue(ad);
		} else if (env.isDefinedF(ad)) {
			return env.exec(ad, Node.makeNode(dataTokens));
		} else if (ad.matches("CAR|CDR")) {
			SExpression sexp = new SExpression(dataTokens);
			if (data.isList()) {
				sexp = new SExpression(sexp.addr.eval().tokens);
			}
			formals = sexp;
		} else if (ad.matches("DEFUN")) {
			return ListFuncs.defun((SExpression) data);
		} else if (ad.matches("LAMBDA")) {
			return ListFuncs.lambda((SExpression) data);
		} else {
			formals = data;
		}

		return env.invokePrim(ad, formals);
	}

	@Override
	public Node eval(boolean literal, Hashtable<String, Node> env) {
		Environment environ = Environment.getInstance();
		Hashtable<String, Node> outdated = environ.getVariables();
		environ.substitute(env);
		environ.setVariables(outdated);
		return eval(literal);
	}

	@Override
	public Node eval(Hashtable<String, Node> env) {
		return eval(false, env);
	}

	/**
	 * Attempts to print this object in list notation, but defaults to dot
	 * notation.
	 */
	@Override
	public String toString() {
		if (isList()) {
			List<String> l = new ArrayList<String>();
			SExpression tmp = this;
			while (tmp.isList()) {
				try {
					l.add(tmp.addr.toString());
					tmp = new SExpression(tmp.dataTokens);
				}
				catch (Exception e) {
					break;
				}
			}
			return "(" + ListUtils.concat(l, " ") + ")";
		}
		return "(" + addr.toString() + "." + data.toString() + ")";
	}

	/**
	 * Grabs the address node.
	 * 
	 * @return
	 */
	public Node getAddr() {
		return addr;
	}

	/**
	 * Grabs the data node.
	 * 
	 * @return
	 */
	public Node getData() {
		return data;
	}

	/**
	 * Grabs the address tokens as a copy list.
	 * 
	 * @return
	 */
	public List<String> getAddrTokens() {
		return new ArrayList<String>(addrTokens);
	}

	/**
	 * Grabs the data tokens as a copy list.
	 * 
	 * @return
	 */
	public List<String> getDataTokens() {
		return new ArrayList<String>(dataTokens);
	}
}
