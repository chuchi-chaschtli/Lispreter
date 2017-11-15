/**
 * NodeTests.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.exception.NodeInitException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anand
 *
 */
public class NodeTests {

	@Test
	public void testAtomEval() {
		Atom t = new Atom(true);
		Node n = Node.makeNode(true);
		Assert.assertEquals(n, t.eval());
		Assert.assertEquals(n, t.eval(false));
		Assert.assertEquals(n, t.eval(true));
		Assert.assertEquals(n, t.eval(null));
		Assert.assertEquals(n, t);

		Atom nil = new Atom(false);
		Assert.assertNotEquals(n, nil);

		n = Node.makeNode(false);
		Assert.assertEquals(n, nil.eval());
		Assert.assertEquals(n, nil.eval(false));
		Assert.assertEquals(n, nil.eval(true));
		Assert.assertEquals(n, nil.eval(null));
		Assert.assertEquals(n, nil);
		Assert.assertNotEquals(n, t);

		n = Node.makeNode(12);
		Atom num = new Atom(12);
		Assert.assertEquals(n, num.eval());
		Assert.assertEquals(n, num.eval(false));
		Assert.assertEquals(n, num.eval(true));
		Assert.assertEquals(n, num.eval(null));
		Assert.assertEquals(n, num);
	}

	@Test(expected = NodeInitException.class)
	public void testMakeNodeErr() {
		Node.makeNode(new ArrayList<String>());
	}

	@Test
	public void testMakeNodeList() {
		Assert.assertEquals(new Atom("a"),
				Node.makeNode(Arrays.asList("a", "b")));

		List<String> op = Arrays.asList("(", "SUM", " ", "(", "5", " ", "6",
				")", ")");
		Assert.assertEquals(new SExpression(op), Node.makeNode(op));
	}

	@Test
	public void testIsList() {
		Assert.assertFalse(Node.makeNode(false).isList());
		Assert.assertFalse(Node.makeNode(30).isList());
		Assert.assertTrue(Node
				.makeNode(
						Arrays.asList("(", "CONS", " ", "(", "5", " ", "NIL",
								")", ")")).isList());
		Assert.assertFalse(Node.makeNode(
				Arrays.asList("q", "(", "CONS", " ", "(", "5", " ", "NIL", ")",
						")")).isList());
	}
}
