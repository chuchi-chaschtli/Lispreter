/**
 * PatternTests.java is a part of Lispreter. 
 */
package interpreter.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anand
 *
 */
public class PatternTests {

	private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMS = "0123456789";
	private static final String ALPHANUMERICS = LETTERS + NUMS;
	private static final String NON_ALPHANUMERICS = " !@#$%^&*()+-_=<>?,./;:'{}[]";
	private static final String NON_LETTERS = NON_ALPHANUMERICS + NUMS;

	@Test
	public void testAtomNumMatching() {
		Assert.assertTrue(Pat.ATOM_NUM.matches("+10"));
		Assert.assertTrue(Pat.ATOM_NUM.matches("-10"));
		Assert.assertTrue(Pat.ATOM_NUM.matches("10"));
		Assert.assertTrue(Pat.ATOM_NUM.matches("0"));
		Assert.assertTrue(Pat.ATOM_NUM.matches("00"));
		Assert.assertFalse(Pat.ATOM_NUM.matches("1.4"));
		Assert.assertFalse(Pat.ATOM_NUM.matches("aa"));
		Assert.assertFalse(Pat.ATOM_NUM.matches("-"));
		Assert.assertFalse(Pat.ATOM_NUM.matches("+"));
		Assert.assertFalse(Pat.ATOM_NUM.matches('a'));
		Assert.assertTrue(Pat.ATOM_NUM.matches('1'));
	}

	@Test
	public void testLetterMatching() {
		for (char c : LETTERS.toCharArray()) {
			Assert.assertTrue(Pat.LETTER.matches(c));
			Assert.assertTrue(Pat.LETTER.matches(String.valueOf(c)));
		}
		for (char c : NON_LETTERS.toCharArray()) {
			Assert.assertFalse(Pat.LETTER.matches(c));
			Assert.assertFalse(Pat.LETTER.matches(String.valueOf(c)));
		}
		Assert.assertFalse(Pat.LETTER.matches(LETTERS));
	}

	@Test
	public void testLiteralMatching() {
		for (char c : ALPHANUMERICS.toCharArray()) {
			Assert.assertTrue(Pat.LITERAL.matches(c));
			Assert.assertTrue(Pat.LITERAL.matches(String.valueOf(c)));
		}
		for (char c : NON_ALPHANUMERICS.toCharArray()) {
			Assert.assertFalse(Pat.LITERAL.matches(c));
			Assert.assertFalse(Pat.LITERAL.matches(String.valueOf(c)));
		}
		Assert.assertTrue(Pat.LITERAL.matches(LETTERS));
	}

	@Test
	public void testParenMatching() {
		Assert.assertTrue(Pat.PAREN_OPEN.matches("("));
		Assert.assertTrue(Pat.PAREN_OPEN.matches("["));
		Assert.assertTrue(Pat.PAREN_OPEN.matches("{"));
		Assert.assertTrue(Pat.PAREN_OPEN.matches('{'));
		Assert.assertFalse(Pat.PAREN_OPEN.matches("(("));

		Assert.assertTrue(Pat.PAREN_CLOSE.matches(")"));
		Assert.assertTrue(Pat.PAREN_CLOSE.matches("]"));
		Assert.assertTrue(Pat.PAREN_CLOSE.matches("}"));
		Assert.assertTrue(Pat.PAREN_CLOSE.matches(')'));
		Assert.assertFalse(Pat.PAREN_CLOSE.matches("))"));
	}

	@Test
	public void testSymbolMatching() {
		Assert.assertTrue(Pat.SYMBOL.matches("."));
		Assert.assertTrue(Pat.SYMBOL.matches("("));
		Assert.assertTrue(Pat.SYMBOL.matches(")"));
		for (char c : ALPHANUMERICS.toCharArray()) {
			Assert.assertFalse(Pat.SYMBOL.matches(c));
			Assert.assertFalse(Pat.SYMBOL.matches(String.valueOf(c)));
		}
	}

	@Test
	public void testValidFuncDef() {
		for (char c : NON_LETTERS.toCharArray()) {
			Assert.assertFalse(Pat.VALID_FUNC.matches(c));
			Assert.assertFalse(Pat.VALID_FUNC.matches(String.valueOf(c)));
		}
		for (char c : LETTERS.toCharArray()) {
			Assert.assertTrue(Pat.VALID_FUNC.matches(c));
			Assert.assertTrue(Pat.VALID_FUNC.matches(String.valueOf(c)));
		}
		Assert.assertTrue(Pat.VALID_FUNC.matches("a56"));
		Assert.assertTrue(Pat.VALID_FUNC.matches("exampleFunction1"));
		Assert.assertTrue(Pat.VALID_FUNC.matches("exampleFunction"));
		Assert.assertFalse(Pat.VALID_FUNC.matches(""));
	}

	@Test
	public void testWhitespaceMatching() {
		Assert.assertTrue(Pat.WHITESPACE.matches(" "));
		for (char c : NON_LETTERS.substring(1).toCharArray()) {
			Assert.assertFalse(Pat.WHITESPACE.matches(c));
			Assert.assertFalse(Pat.WHITESPACE.matches(String.valueOf(c)));
		}
		for (char c : LETTERS.toCharArray()) {
			Assert.assertFalse(Pat.WHITESPACE.matches(c));
			Assert.assertFalse(Pat.WHITESPACE.matches(String.valueOf(c)));
		}
	}
}
