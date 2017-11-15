/**
 * LexerTests.java is a part of Lispreter. 
 */
package interpreter.lexer;

import interpreter.parser.Parser;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anand
 *
 */
public class LexerParserTests {

	@Test
	public void testTokenize() {
		Lexer l = new Lexer("(sum 5 6)");
		Assert.assertEquals(l.getTokens(),
				Arrays.asList("(", "sum", "5", "6", ")"));
		l = new Lexer("(cons 6 (cons 8 (cons 20)))");
		Assert.assertEquals(l.getTokens(), Arrays.asList("(", "cons", "6", "(",
				"cons", "8", "(", "cons", "20", ")", ")", ")"));
		l = new Lexer("(cons (product 12 3) (cons (integerp 12) NIL))");
		Assert.assertEquals(l.getTokens().toString(),
				"[(, cons, (, product, 12, 3, ), (, cons, (, integerp, 12, ), NIL, ), )]");
	}

	@Test
	public void testMath() {
		Lexer l = new Lexer("(sum 5 6 7) " + "(sum 5 6) "
				+ "(sum 5 6 (product 3 3) 12) "
				+ "(sum (cons 4 (cons 4 NIL))) "
				+ "(sum (cons 4 (cons (product 17 2) NIL)))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "18\n11\n32\n8\n38");

	}

	@Test
	public void testDefun() {
		Lexer l = new Lexer(
				"(defun sumdouble (x y) (sum (product x 2) (product y 2))) "
						+ "(defun triplesumdouble (x y) (product (sumdouble x y) 3)) "
						+ "(sumdouble 6 (sumdouble 2 1)) "
						+ "(triplesumdouble 6 3)");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "sumdouble\ntriplesumdouble\n24\n54");
	}

	@Test
	public void testLambda() {
		Lexer l = new Lexer(
				"((lambda (x y) (product x y)) 15 7) "
						+ "((lambda (a b x y) (sum (product a x) (product b y))) 5 5 4 3)");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "105\n35");
	}

	@Test
	public void testParenTypes() throws IOException {
		String result = "sumdouble\ntriplesumdouble\n24\n54";
		Lexer l = new Lexer(
				"(defun sumdouble (x y) (sum (product x 2) (product y 2))) "
						+ "(defun triplesumdouble (x y) (product (sumdouble x y) 3)) "
						+ "(sumdouble 6 (sumdouble 2 1)) "
						+ "(triplesumdouble 6 3)");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), result);

		l = new Lexer(
				"{defun sumdouble (x y} (sum {product x 2} (product y 2))) "
						+ "{defun triplesumdouble (x y) (product (sumdouble x y) 3)) "
						+ "(sumdouble 6 (sumdouble 2 1)) "
						+ "(triplesumdouble 6 3}");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), result);

		l = new Lexer(
				"{defun sumdouble [x y} (sum {product x 2] [product y 2))) "
						+ "{defun triplesumdouble [x y] (product (sumdouble x y) 3)) "
						+ "(sumdouble 6 (sumdouble 2 1)) "
						+ "(triplesumdouble 6 3}");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), result);
	}
	// @Test
	// public void testRelationalOps() {
	// Lexer l;
	// try {
	// StringBuilder builder = new StringBuilder();
	// l = new Lexer("(less 5 6 4)");
	// new Parser(l.getTokens()).eval(builder);
	// l = new Lexer("(less 5 6)");
	// new Parser(l.getTokens()).eval(builder);
	// l = new Lexer("(less 6 5)");
	// new Parser(l.getTokens()).eval(builder);
	// l = new Lexer("(less 5 6 4 7)");
	// new Parser(l.getTokens()).eval(builder);
	// l = new Lexer("(less 4)");
	// new Parser(l.getTokens()).eval();
	// Assert.assertEquals(builder.toString(), "NIL\nT\nNIL\nNIL\n");
	// }
	// catch (Exception e) {
	// System.out.println("ERROR during test occurred");
	// }
	// }
}
