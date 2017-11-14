/**
 * ClosureState.java is a part of Lispreter. 
 */
package interpreter.parser.func;

/**
 * @author Anand
 *
 */
public class ClosureState {

	private static boolean lambda = false;

	private ClosureState() {
		throw new AssertionError("Cannot instantiate Closure Evaluation class");
	}

	public static void changeState() {
		lambda = !lambda;
	}

	public static boolean isEvaluatingLambda() {
		return lambda;
	}
}
