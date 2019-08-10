/**
 * MalformedTextException.java is a part of Lispreter. 
 */
package interpreter.exception;

/**
 * Exception thrown during tokenization errors in the Lexer.
 * 
 * @author Anand
 *
 */
public class MalformedTextException extends RuntimeException {

	private static final long serialVersionUID = 667749991416751308L;

	public MalformedTextException(String s) {
		super(s);
	}
}
