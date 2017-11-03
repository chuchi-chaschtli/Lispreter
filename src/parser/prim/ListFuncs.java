/**
 * ListFuncs.java is a part of Lispreter. 
 */
package parser.prim;

import parser.Node;
import parser.SExpression;

/**
 * @author Anand
 *
 */
public class ListFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "car", "first" })
	public static Node car(SExpression s) {
		return s.getAddr();
	}

	@Primitive(aliases = { "cdr", "rest" })
	public static Node cdr(SExpression s) {
		return s.getData();
	}
}
