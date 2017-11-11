/**
 * UserDef.java is a part of Lispreter. 
 */
package interpreter.parser.func;

import interpreter.exception.FuncDefException;
import interpreter.parser.Node;
import interpreter.parser.SExpression;

import java.util.Hashtable;

/**
 * Representation for user defined lisp functions.
 * 
 * @author Anand
 *
 */
public final class UserDef extends Function {

	private final String name;

	/**
	 * Constructor initializes a User Function based on a name, and a list of
	 * formals and body.
	 * 
	 * @param name
	 *            the String id of the function.
	 * @param formals
	 *            the list of formals.
	 * @param body
	 *            the func body.
	 */
	public UserDef(String name, Node formals, Node body) {
		super(formals, body);
		this.name = name;
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
		for (int i = 0; i < params.size(); i++) {
			String f = params.get(i);
			env.put(f, s.getAddr().eval());
			try {
				s = new SExpression(s.getDataTokens());
			}
			catch (Exception e) {
				if (i < params.size() - 1) {
					throw new FuncDefException("Too few args for function : "
							+ name);
				}
			}
		}

		if (s.getData().eval().toString().equals("NIL")) {
			return env;
		}
		throw new FuncDefException("Too many args for function : " + name);
	}

}
