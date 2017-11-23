/**
 * PrimitiveTests.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import interpreter.lexer.Lexer;
import interpreter.parser.Node;
import interpreter.parser.NodeFactory;
import interpreter.parser.Parser;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anand
 *
 */
public class PrimitiveTests {

	@Test
	public void testPredFuncEval() {
		Lexer l = new Lexer(
				"(and T NIL) (and NIL NIL) (and NIL T) (and T T) (and NIL NIL NIL) (and T T T) (and NIL NIL T) (and NIL T NIL)");
		StringBuilder builder = new StringBuilder();
		new Parser(l.getTokens(), builder).eval();
		Assert.assertEquals(builder.toString(),
				"NIL\nNIL\nNIL\nT\nNIL\nT\nNIL\nNIL");

		l = new Lexer(
				"(or T NIL) (or NIL NIL) (or NIL T) (or T T) (or NIL NIL NIL) (or T T T) (or NIL NIL T) (or NIL T NIL)");
		builder = new StringBuilder();
		new Parser(l.getTokens(), builder).eval();
		Assert.assertEquals(builder.toString(), "T\nNIL\nT\nT\nNIL\nT\nT\nT");

		l = new Lexer("(not T) (not NIL) (not T NIL) (not T T) (not NIL T)");
		builder = new StringBuilder();
		new Parser(l.getTokens(), builder).eval();
		Assert.assertEquals(builder.toString(), "NIL\nT\nNIL\nNIL\nT");
		
		l = new Lexer("(and T) (and NIL) (or T) (or NIL)");
		builder = new StringBuilder();
		new Parser(l.getTokens(), builder).eval();
		Assert.assertEquals(builder.toString(), "T\nNIL\nT\nNIL");
	}

	@Test
	public void testMathFuncEval() {
		Assert.assertEquals(
				eval("(", "SUM", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(11));
		Assert.assertEquals(
				eval("(", "DIFFERENCE", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(-1));
		Assert.assertEquals(
				eval("(", "PRODUCT", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(30));
		Assert.assertEquals(
				eval("(", "QUOTIENT", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(0));
		Assert.assertEquals(
				eval("(", "LESS", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(true));
		Assert.assertEquals(
				eval("(", "LEQ", " ", "(", "6", " ", "6", ")", ")"),
				NodeFactory.makeNode(true));
		Assert.assertEquals(
				eval("(", "GREATER", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(false));
		Assert.assertEquals(
				eval("(", "GEQ", " ", "(", "5", " ", "6", ")", ")"),
				NodeFactory.makeNode(false));
		Assert.assertEquals(
				eval("(", "MOD", " ", "(", "9", " ", "-4", ")", ")"),
				NodeFactory.makeNode(-3));
		Assert.assertEquals(
				eval("(", "REM", " ", "(", "9", " ", "-4", ")", ")"),
				NodeFactory.makeNode(1));
		Assert.assertEquals(eval("(", "INTEGERP", " ", "x", ")"),
				NodeFactory.makeNode(false));
		Assert.assertEquals(eval("(", "INTEGERP", " ", "9", ")"),
				NodeFactory.makeNode(true));
	}

	private static Node eval(String... op) {
		return NodeFactory.makeNode(Arrays.asList(op)).eval();
	}
}
