/**
 * EnvironmentException.java is a part of Lispreter. 
 */
package interpreter.exception;

/**
 * This exception is thrown whenever the Environment is accessed or modified in
 * an illegal maner.
 * 
 * @author Anand
 *
 */
public class EnvironmentException extends RuntimeException {

	private static final long serialVersionUID = -123590125279082698L;

	public EnvironmentException(String s) {
		super(s);
	}
}
