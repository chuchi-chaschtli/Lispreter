/**
 * ListFuncs.java is a part of Lispreter. 
 */
package parser.prim;

import parser.Node;
import parser.SExpression;
import parser.util.Pat;

/**
 * @author Anand
 *
 */
public class ListFuncs implements PrimitiveMarker {

	@Primitive(aliases = { "car", "first" })
	public static Node car(SExpression sexp) {
		return sexp.getAddr();
	}

	@Primitive(aliases = { "cdr", "rest" })
	public static Node cdr(SExpression sexp) {
		return sexp.getData();
	}
	
	public static Node atom(SExpression sexp) {
		return Node.makeNode(Pat.LITERAL.matches(sexp.getAddr().eval().toString()));
	}
}
