/**
 * NodeInitException.java is a part of Lispreter. 
 */
package exception;

/**
 * This exception is thrown whenever a Node is initialized in an invalid way.
 * For example, constructing a node with no data will throw this exception.
 * 
 * @author Anand
 *
 */
public class NodeInitException extends RuntimeException {

	private static final long serialVersionUID = -5533624622989990963L;

	public NodeInitException(String s) {
		super(s);
	}
}
