/**
 * SExpression.java is a part of Lispreter. 
 */
package parser;

import java.util.ArrayList;
import java.util.List;

import util.StringUtils;
import exception.NodeInitException;

/**
 * @author Anand
 *
 */
public class SExpression extends Node {

	protected Node data, addr;

	protected List<String> dataTokens, addrTokens;

	public SExpression(List<String> tokens) {
		makeCons(tokens);
	}

	public SExpression(Node n) {
		this(n.tokens);
	}

	public SExpression(Node addr, Node data) {
		this.addr = addr;
		this.data = data;
		this.addrTokens = addr.tokens;
		this.dataTokens = data.tokens;
		buildTokens(addr.tokens, data.tokens);

	}

	public SExpression(SExpression sexp) {
		data = Node.makeNode(sexp.dataTokens);
		addr = Node.makeNode(sexp.addrTokens);
		dataTokens = new ArrayList<String>(sexp.dataTokens);
		addrTokens = new ArrayList<String>(sexp.addrTokens);
	}

	private void makeCons(List<String> tokens) {
		if (tokens.size() > 0 && tokens.get(0).matches("[(]")) {
			int index = 1;
			int dataBegin = 3;
			if (tokens.get(index) == "(") {
				int numClausesOpen = 1;
				while (numClausesOpen > 0 && index < tokens.size()) {
					index++;
					if (tokens.get(index).equals("(")) {
						numClausesOpen++;
					} else if (tokens.get(index).equals(")")) {
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
			buildTokens(addrTokens, dataTokens);
			return;
		}
		throw new NodeInitException("Invalid S-Expression provided : "
				+ tokens.toString());
	}

	private void buildTokens(List<String> addrTokens, List<String> dataTokens) {
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
	
	@Override
	public String toString() {
		if (isList()) {
			List<String> l = new ArrayList<String>();
			SExpression tmp = this;
			while (tmp.isList()) {
				l.add(tmp.addr.toString());
				try {
					tmp = new SExpression(tmp.dataTokens);
				} catch (Exception e) {
					break;
				}
			}
			String str = "(" + StringUtils.concat(l, " ") + ")";
			return str;
		}
		return "(" + addr.toString() + "." + data.toString() + ")";
	}
}
