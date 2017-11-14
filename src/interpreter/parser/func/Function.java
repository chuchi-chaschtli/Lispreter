/**
 * Function.java is a part of Lispreter. 
 */
package interpreter.parser.func;

import interpreter.exception.FuncDefException;
import interpreter.parser.Node;
import interpreter.parser.SExpression;
import interpreter.util.Pat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Data structure for Lisp function definitions. A function is instantiated with
 * at least a Node for its formal parameters and a Node for its body. The
 * formals are converted from string form to a List form.
 * <p>
 * Functions itself are abstract, and cannot be instanitated.
 * 
 * @author Anand
 *
 */
public class Function {

	private final String name;
	private List<String> params;
	private Node body;

	/**
	 * Constructs a function with formal parameters and a function body.
	 * 
	 * @param the
	 *            String tag
	 * @param params
	 *            a Node of parameters.
	 * @param body
	 *            a Node for the function body
	 */
	public Function(String name, Node params, Node body) {
		if (!(params.isList() || params.toString().equals("NIL"))) {
			throw new FuncDefException("Invalid function parameters");
		}
		if (!(body.isList() || body.toString().equals("NIL"))) {
			throw new FuncDefException("Invalid function body");
		}
		this.params = convertParams(params.toString());
		this.body = body;
		this.name = name;
	}

	/**
	 * Evaluates a function by invoking its body with passed arguments.
	 * 
	 * @param args
	 *            the list of actuals.
	 * @return the resultant Node after evaluation.
	 */
	public Node eval(Node args) {
		return body.eval(true, bind(args));
	}

	/**
	 * Create a list of parameters from a string containing the formals.
	 * Additionally validifies parameters.
	 * 
	 * @param formals
	 *            the String of parameters.
	 * @return a List of parameter names.
	 * @throws FuncDefException
	 *             if parameters are not distinct or not well-formed.
	 */
	private List<String> convertParams(String formals) {
		String[] words = formals.substring(1, formals.length() - 1)
				.split("\\s");
		List<String> result = new ArrayList<>();
		for (String word : words) {
			if (Pat.VALID_FUNC.matches(word)) {
				if (result.contains(word)) {
					throw new FuncDefException(
							"Formal param names cannot be duplicates.");
				}
				result.add(word);
			} else {
				throw new FuncDefException(
						"Parameter names must be alphanumeric literals : "
								+ word);
			}
		}
		return result;
	}

	/**
	 * Constructs a binding table of formals -> actuals.
	 * 
	 * @param actuals
	 *            the Node of actual parameters.
	 * @return a binding table.
	 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Function other = (Function) obj;
		if (body == null) {
			if (other.body != null) return false;
		} else if (!body.equals(other.body)) return false;
		if (params == null && other.params != null) {
			return false;
		}
		return params.equals(other.params);
	}
}
