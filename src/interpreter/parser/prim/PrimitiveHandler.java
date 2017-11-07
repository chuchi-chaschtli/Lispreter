/**
 * PrimitiveHandler.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.parser.Node;
import interpreter.parser.SExpression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper class for all Primitive Functions. The handler, upon instantiation,
 * registers all primitive functions. All primitive functions must be supplied
 * with a {@link Primitive} annotation, otherwise it cannot be registered.
 * Similarly, primitive functions must be contained in an implementation which
 * is marked appropriately, by implementing {@link PrimitiveMarker}.
 * <p>
 * All functions, upon registration, are stored in a mapping of String -> Method
 * representing the alias and the function call method. Aliases must be unique.
 * This is a surjective mapping, and many aliases may map to a single method
 * call.
 * </p>
 * <p>
 * A set of methods is also maintained, containing all methods which require an
 * S-Expression argument. This is a convenience collection to ensure proper
 * function calls are made.
 * </p>
 * 
 * @author Anand
 *
 */
public class PrimitiveHandler {

	private Map<String, Method> primitives;
	private Set<Method> methodsWithArgs;

	/**
	 * Default constructor initializes collections and registers functions.
	 */
	public PrimitiveHandler() {
		primitives = new HashMap<>();
		methodsWithArgs = new HashSet<>();

		registerFunctions();
	}

	/**
	 * Calls a function supplied in the alias map with the given Object
	 * arguments.
	 * <p>
	 * If arguments are required by the function, then this call will fail if
	 * there isn't exactly one argument, and this argument must be an
	 * S-Expression.
	 * 
	 * @param name
	 *            the function alias.
	 * @param arg
	 *            specified arguments.
	 * @return a Node, if the function call succeeded, null otherwise.
	 * @throws IllegalArgumentException
	 *             if arguments are required, and the argument specified is not
	 *             a single S-Expression.
	 */
	public Node callFunc(String name, Object... arg) {
		if (primitives.containsKey(name)) {
			Method m = primitives.get(name);
			try {
				if (!methodsWithArgs.contains(m)) {
					return (Node) m.invoke(null);
				}
				if (arg.length != 1 || (!(arg[0] instanceof SExpression))) {
					throw new IllegalArgumentException(
							"Must supply one S-Expression arg");
				}
				return (Node) m.invoke(null, arg);
			}
			catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Registers all function classes.
	 * 
	 * @see PrimitiveHandler#register(Class)
	 */
	private void registerFunctions() {
		register(BoolFuncs.class);
		register(ListFuncs.class);
		register(MathFuncs.class);
	}

	/**
	 * Registers a single Primitive function class. For a class to contain
	 * primitive functions, it <b>must</b> be a PrimitiveMarker. For each method
	 * which is a Primitive function, it must return a Node type to be a valid
	 * function call. Registration effectively adds the aliases to the working
	 * map, and adds all methods which require arguments to the working set.
	 * 
	 * @param clazz
	 *            a PrimitiveMarker class.
	 * @throws IllegalArgumentException
	 *             if the alias is already contained in the working map.
	 * @throws UnsupportedOperationException
	 *             if a marked method does not return a Node.
	 * @throws ClassNotFoundException
	 *             if the class loader failed.
	 * @throws SecurityException
	 *             if a security violation occurred.
	 */
	private void register(Class<? extends PrimitiveMarker> clazz) {
		try {
			for (Method m : PrimitiveHandler.class.getClassLoader()
					.loadClass(clazz.getName()).getMethods()) {
				if (m.isAnnotationPresent(Primitive.class)) {
					Primitive info = m.getAnnotation(Primitive.class);
					if (m.getReturnType() != Node.class) {
						throw new UnsupportedOperationException(
								"Cannot register a method which does not return a Node : "
										+ m.getName());
					}
					if (info.sexpRequired()) {
						methodsWithArgs.add(m);
					}
					for (String alias : info.aliases()) {
						if (primitives.containsKey(alias)) {
							throw new IllegalArgumentException(
									"Alias supplied for " + m.getName() + ", "
											+ alias + " is already assigned.");
						}
						primitives.put(alias, m);
					}
				}
			}
		}
		catch (SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a read-only map of the functions defined.
	 * 
	 * @return a map of String -> Method.
	 */
	public Map<String, Method> getRegisteredFunctions() {
		return Collections.unmodifiableMap(primitives);
	}
}
