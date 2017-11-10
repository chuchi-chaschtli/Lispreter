/**
 * ArithmeticZeroError.java is a part of Lispreter. 
 */
package interpreter.exception;

/**
 * @author Anand
 *
 */
public class ArithmeticZeroError extends Error {

	private static final long serialVersionUID = -6296993911940996892L;

	public ArithmeticZeroError() {
		super("Cannot perform this operation on 0");
	}
}
