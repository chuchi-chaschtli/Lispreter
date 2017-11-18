/**
 * ClosureState.java is a part of Lispreter. 
 */
package interpreter.parser.func;

import interpreter.parser.Node;

/**
 * @author Anand
 *
 */
public class ClosureState {

	private static boolean lambda = false;
	private static Node nextNode = null;
	private static String nextValue = "";

	private ClosureState() {
		throw new AssertionError("Cannot instantiate Closure Evaluation class");
	}

	public static void changeState() {
		lambda = !lambda;
	}

	public static boolean isEvaluatingLambda() {
		return lambda;
	}

	public static Node getNextNode() {
		return nextNode;
	}

	public static void setNextNode(Node next) {
		nextNode = next;
	}

	public static String getNextValue() {
		return nextValue;
	}

	public static void setNextValue(String next) {
		nextValue = next;
	}

	public static void reset() {
		lambda = false;
		nextNode = null;
		nextValue = "";
	}
}
