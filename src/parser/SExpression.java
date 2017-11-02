/**
 * SExpression.java is a part of Lispreter. 
 */
package parser;

import java.util.ArrayList;
import java.util.List;

import parser.util.Pat;
import util.StringUtils;
import exception.NodeInitException;

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
			if (tokens.get(index) == "(") {
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
			index = dataBegin > 3 ? tokens.subList(dataBegin, tokens.size())
					.indexOf(".") : 2;

			data = Node.makeNode(dataTokens);
			addr = Node.makeNode(addrTokens);
			dataTokens = new ArrayList<String>(tokens.subList(index + 1,
					tokens.size() - 1));
			addrTokens = new ArrayList<String>(tokens.subList(1, index));
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
	protected boolean isList() {
		return data.toString().matches("NIL") || data.isList();
	}

	@Override
	Node eval() {
		// TODO IMPLEMENT
		return null;
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
			return "(" + StringUtils.concat(l, " ") + ")";
		}
		return "(" + addr.toString() + "." + data.toString() + ")";
	}

	/**
	 * Grabs the address node.
	 * 
	 * @return
	 */
	protected Node getAddr() {
		return addr;
	}

	/**
	 * Grabs the data node.
	 * 
	 * @return
	 */
	protected Node getData() {
		return data;
	}

	/**
	 * Grabs the address tokens as a copy list.
	 * 
	 * @return
	 */
	protected List<String> getAddrTokens() {
		return new ArrayList<String>(addrTokens);
	}

	/**
	 * Grabs the data tokens as a copy list.
	 * 
	 * @return
	 */
	protected List<String> getDataTokens() {
		return new ArrayList<String>(dataTokens);
	}
}
