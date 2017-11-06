/**
 * BoolFuncs.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.parser.Atom;
import interpreter.parser.Node;

/**
 * @author Anand
 *
 */
public class BoolFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "T", "true" }, sexpRequired = false)
	public static Node t() {
		return new Atom(true);
	}

	@Primitive(aliases = { "NIL", "false" }, sexpRequired = false)
	public static Node nil() {
		return new Atom(false);
	}

	public static Node boolFuncFactory(String alias) {
		if (alias.matches("NIL|false")) {
			return nil();
		} else if (alias.matches("T|true")) {
			return t();
		}
		return null;
	}
}
