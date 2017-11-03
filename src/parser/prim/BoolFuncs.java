/**
 * BoolFuncs.java is a part of Lispreter. 
 */
package parser.prim;

import parser.Atom;
import parser.Node;

/**
 * @author Anand
 *
 */
public class BoolFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "t" }, sexpRequired = false)
	public static Node t() {
		return new Atom(true);
	}

	@Primitive(aliases = { "nil" }, sexpRequired = false)
	public static Node nil() {
		return new Atom(false);
	}
}
