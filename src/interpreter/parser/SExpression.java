/**
 * SExpression.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.exception.NodeInitException;
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
		data = NodeFactory.makeNode(sexp.dataTokens);
		addr = NodeFactory.makeNode(sexp.addrTokens);
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

			data = NodeFactory.makeNode(dataTokens);
			addr = NodeFactory.makeNode(addrTokens);
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
		String ad = addr.eval(true).toString();
		Node formals = data;
		Environment env = Environment.getInstance();

		if (literal && Pat.ATOM_NUM.matches(ad)) {
			return addr.eval();
		} else if (ad.toUpperCase().matches("T|NIL")) {
			return NodeFactory.makeBooleanNode(ad);
		} else if (env.isDefinedV(ad)) {
			return env.getVariableValue(ad);
		} else if (env.isDefinedF(ad)) {
			return env.execFunc(ad, data);
		} else if (ad.toUpperCase().matches("LENGTH")) {
			try {
				formals = new SExpression(new SExpression(data).getAddr())
						.getData();
			}
			catch (Exception e) {}

		} else if (ad.toUpperCase().matches("Λ|LAMBDA")) {
			formals = this;
		} else if (ad.toUpperCase().matches("CAR|CDR|FIRST|REST")) {
			SExpression sexp = new SExpression(dataTokens);
			if (data.isList()) {
				Node address = sexp.addr;
				if (address.isList()) {
					formals = new SExpression(new SExpression(address).data);
				} else {
					formals = address;
				}
			}
		}

		return env.invokePrim(ad, formals);
	}

	@Override
	public Node eval(boolean literal, Hashtable<String, Node> env) {
		Environment environ = Environment.getInstance();
		Hashtable<String, Node> outdated = environ.getVariables();
		environ.substitute(env);
		Node retVal = eval(literal);
		environ.setVariables(outdated);
		return retVal;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((addr == null) ? 0 : addr.hashCode());
		result = prime * result
				+ ((addrTokens == null) ? 0 : addrTokens.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((dataTokens == null) ? 0 : dataTokens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		SExpression other = (SExpression) obj;
		if (addr == null) {
			if (other.addr != null) return false;
		} else if (!addr.equals(other.addr)) return false;
		if (addrTokens == null) {
			if (other.addrTokens != null) return false;
		} else if (!addrTokens.equals(other.addrTokens)) return false;
		if (data == null) {
			if (other.data != null) return false;
		} else if (!data.equals(other.data)) return false;
		if (dataTokens == null) {
			if (other.dataTokens != null) return false;
		}
		return dataTokens.equals(other.dataTokens);
	}
}
