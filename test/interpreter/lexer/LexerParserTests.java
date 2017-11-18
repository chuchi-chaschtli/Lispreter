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
	public void testIf() {
		Lexer l = new Lexer(
				"(if (eq 1 1) 5 18) (if (eq 1 0) 5 18) (if T 0 1) (if NIL 0 1) (if T (if NIL 0 (if T 1 2)) 3)");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "5\n18\n0\n1\n1");
	}

	@Test
	public void testCond() {
		Lexer l = new Lexer(
				"(defun fibonacci (n) (cond [(<= n 1) n] [t (+ (fibonacci (- n 1)) (fibonacci (- n 2)))])) "
						+ "(fibonacci 0) (fibonacci 1) (fibonacci 2) (fibonacci 4) (fibonacci 7)");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "fibonacci\n0\n1\n1\n3\n13");
	}

	@Test
	public void testQuote() {
		Lexer l = new Lexer("(quote (+ 1 2)) (' (* (+ 3 5) (- 2 2)))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "(+ 1 2)\n(* (+ 3 5) (- 2 2))");
		l = new Lexer("(' (cons \"This is a string\" (+ 3 3)))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "(cons \"This is a string\" (+ 3 3))");
	}

	@Test
	public void testList() {
		Lexer l = new Lexer(
				"(list 1 2 3 4) (list 1) (list NIL) (list \"a\" \"b\")");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "(1 2 3 4)\n(1)\n(NIL)\n(\"a\" \"b\")");
	}

	@Test
	public void testCarCdr() {
		Lexer l = new Lexer(
				"(car (list 1 2 3 4)) (cdr (list 1 2 3 4)) (car (cons 1 (cons 2 NIL))) (cdr (cons 1 (cons 2 (cons 3 NIL))))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "1\n(2 3 4)\n1\n((cons 2 (cons 3 NIL)))");
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

	@Test
	public void testNull() {
		Lexer l = new Lexer(
				"(null 3) (null NIL) (null T) (null (list 1 2 3)) (null (list NIL)) (null (cons 1 (cons 2 NIL)))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "NIL\nT\nNIL\nNIL\nT\nNIL");
	}

	@Test
	public void testIntegerp() {
		Lexer l = new Lexer(
				"(integerp 5) (integerp 0) (integerp \"a\") (integerp T) (integerp (first (list 3 3 4)))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "T\nT\nNIL\nNIL\nT");
	}

	@Test
	public void testMap() {
		Lexer l = new Lexer(
				"(defun andmap (func lst) (if (endp lst) T (and (func (car lst)) (andmap func (cdr lst))))) "
						+ "(andmap integerp (list 1 2 \"abc\" T 5 NIL)) (andmap integerp (list 4 5)) "
						+ "(defun filter (pred lst) (cond [(endp lst) NIL] "
						+ "[(pred (car lst)) (cons (car lst) (filter pred (cdr lst)))] "
						+ "[t (filter pred (cdr lst))])) "
						+ "(filter integerp (list 1 2 \"abc\" T 5 NIL)) (filter integerp (list 4 5))");
		Assert.assertEquals(new Parser(l.getTokens(), new StringBuilder())
				.eval().toString(), "andmap\nNIL\nT\nfilter\n(1 2 5)\n(4 5)");
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
