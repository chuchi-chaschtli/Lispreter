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
		Node n = NodeFactory.TRUE;
		Assert.assertEquals(n, t.eval());
		Assert.assertEquals(n, t.eval(false));
		Assert.assertEquals(n, t.eval(true));
		Assert.assertEquals(n, t.eval(null));
		Assert.assertEquals(n, t);

		Atom nil = new Atom(false);
		Assert.assertNotEquals(n, nil);

		n = NodeFactory.FALSE;
		Assert.assertEquals(n, nil.eval());
		Assert.assertEquals(n, nil.eval(false));
		Assert.assertEquals(n, nil.eval(true));
		Assert.assertEquals(n, nil.eval(null));
		Assert.assertEquals(n, nil);
		Assert.assertNotEquals(n, t);

		n = NodeFactory.makeNode(12);
		Atom num = new Atom(12);
		Assert.assertEquals(n, num.eval());
		Assert.assertEquals(n, num.eval(false));
		Assert.assertEquals(n, num.eval(true));
		Assert.assertEquals(n, num.eval(null));
		Assert.assertEquals(n, num);
	}

	@Test(expected = NodeInitException.class)
	public void testMakeNodeErr() {
		NodeFactory.makeNode(new ArrayList<String>());
	}

	@Test
	public void testMakeNodeList() {
		Assert.assertEquals(new Atom("a"),
				NodeFactory.makeNode(Arrays.asList("a", "b")));

		List<String> op = Arrays.asList("(", "SUM", " ", "(", "5", " ", "6",
				")", ")");
		Assert.assertEquals(new SExpression(op), NodeFactory.makeNode(op));
	}

	@Test
	public void testIsList() {
		Assert.assertTrue(NodeFactory.FALSE.isList());
		Assert.assertFalse(NodeFactory.makeNode(30).isList());
		Assert.assertTrue(NodeFactory
				.makeNode(
						Arrays.asList("(", "CONS", " ", "(", "5", " ", "NIL",
								")", ")")).isList());
		Assert.assertFalse(NodeFactory.makeNode(
				Arrays.asList("q", "(", "CONS", " ", "(", "5", " ", "NIL", ")",
						")")).isList());
	}
}
