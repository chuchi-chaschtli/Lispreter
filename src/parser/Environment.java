/**
 * Environment.java is a part of Lispreter. 
 */
package parser;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import exception.EnvironmentException;

/**
 * Contains the working 'd-list' of the lisp program. Manages function/var
 * binding within the program context.
 * 
 * @author Anand
 *
 */
public class Environment {

	private Hashtable<String, UserFunc> functions = new Hashtable<>();
	private Hashtable<String, Node> variables = new Hashtable<>();

	/**
	 * Executes a given function with the given variable arguments.
	 * 
	 * @param name
	 *            the name of the function to lookup
	 * @param args
	 *            the Node parameter arguments
	 * @return the Node evaluation
	 * @throws EnvironmentException
	 *             if the given function name has not been defined.
	 */
	public Node exec(String name, Node args) {
		if (!isDefinedF(name)) {
			throw new EnvironmentException("The function " + name
					+ " is undefined.");
		}
		UserFunc func = functions.get(name);
		return func.eval(args);
	}

	/**
	 * Registers a function in the 'd-list' table.
	 * 
	 * @param name
	 *            the identifier tag of the function.
	 * @param args
	 *            the formal arguments of the function.
	 * @param body
	 *            the literal or sexp function body.
	 */
	public void register(String name, Node args, Node body) {
		functions.put(name, new UserFunc(name, args, body));
	}

	/**
	 * Merges in new variable bindings into the working environment.
	 * 
	 * @param table
	 *            a hashtable of var bindings (String -> Node).
	 */
	public void substitute(Hashtable<String, Node> table) {
		Iterator<Entry<String, Node>> iter = table.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Node> pair = iter.next();
			if (variables.contains(pair.getKey())) {
				continue;
			}
			variables.put(pair.getKey(), pair.getValue());
			iter.remove();
		}
	}

	/**
	 * Unbinds a given variable name, if it exists in the table.
	 * 
	 * @param name
	 *            the variable name to unbind.
	 * @throws EnvironmentException
	 *             if the variable is undefined.
	 */
	public void unbind(String name) {
		if (!isDefinedV(name)) {
			throw new EnvironmentException("The variable " + name
					+ " is undefined.");
		}
		variables.remove(name);
	}

	/**
	 * Unbinds a set of variables, if they exist in the table. Used to remove
	 * variables from the environment.
	 * 
	 * @param names
	 *            the variable names to unbind.
	 * @throws EnvironmentException
	 *             if any variable is undefined.
	 */
	public void unbindMulti(Set<String> names) {
		for (String name : names) {
			unbind(name);
		}
	}

	/**
	 * Is the given function name defined?
	 * 
	 * @param name
	 *            the name of the function.
	 * @return true if the function name is in the hashtable, false otherwise.
	 */
	public boolean isDefinedF(String name) {
		return functions.containsKey(name);
	}

	/**
	 * Is the given variable name defined?
	 * 
	 * @param name
	 *            the name of the variable.
	 * @return true if the variable name is in the hashtable, false otherwise.
	 */
	public boolean isDefinedV(String name) {
		return variables.containsKey(name);
	}

	/**
	 * Grabs the value of a given variable name.
	 * 
	 * @param name
	 *            the name of the variable.
	 * @return the Node value, if the variable was found.
	 * @throws EnvironmentException
	 *             if the variable was not defined.
	 * @see #isDefinedV(String)
	 */
	public Node getVariableValue(String name) {
		if (!isDefinedV(name)) {
			throw new EnvironmentException("The variable " + name
					+ " is undefined.");
		}
		return variables.get(name);
	}

	/**
	 * Grabs a copy table of the working variables, to avoid corruption
	 * 
	 * @return a table of String -> Node.
	 */
	public Hashtable<String, Node> getVariables() {
		return new Hashtable<String, Node>(variables);
	}

	/**
	 * Sets the variables to the given String -> Node Hashtable.
	 * 
	 * @param values
	 *            a valid Hashtable (String -> Node).
	 */
	public void setVariables(Hashtable<String, Node> values) {
		variables = new Hashtable<>(values);
	}
}
