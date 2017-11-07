/**
 * Closure.java is a part of Lispreter. 
 */
package interpreter.parser.func;

import interpreter.exception.FuncDefException;
import interpreter.exception.NodeInitException;
import interpreter.parser.Node;
import interpreter.parser.SExpression;

import java.util.Hashtable;

/**
 * Lambda operations as defined in lisp.
 * 
 * @author Anand
 *
 */
public final class Closure extends Function {

	public Closure(Node params, Node body) {
		super(params, body);
	}

	@Override
	protected Hashtable<String, Node> bind(Node actuals) {
		Hashtable<String, Node> env = new Hashtable<>();
		if (!actuals.isList()) {
			if (!actuals.toString().equals("NIL")) {
				throw new FuncDefException(
						"Invalid parameters passed in bind operation.");
			}
			return env;
		}
		SExpression s = new SExpression(actuals);
		for (String f : params) {
			env.put(f, s.getAddr().eval());
			try {
				s = new SExpression(s.getDataTokens());
			}
			catch (NodeInitException e) {
				break;
			}
		}
		return env;
	}
}
