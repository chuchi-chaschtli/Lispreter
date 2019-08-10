/**
 * FuncDefException.java is a part of Lispreter. 
 */
package interpreter.exception;

/**
 * Exception thrown when an invalid function definition is called.
 * 
 * @author Anand
 *
 */
public class FuncDefException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FuncDefException(String s) {
		super(s);
	}
}
