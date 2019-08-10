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

	private boolean lambda = false;
	private Node nextNode = null;
	private String nextValue = "";

	private static ClosureState instance;

	private ClosureState() {
		reset();
	}

	public static ClosureState getInstance() {
		if (instance == null) {
			instance = new ClosureState();
		}
		return instance;
	}

	public void changeState() {
		lambda = !lambda;
	}

	public boolean isEvaluatingLambda() {
		return lambda;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node next) {
		nextNode = next;
	}

	public String getNextValue() {
		return nextValue;
	}

	public void setNextValue(String next) {
		nextValue = next;
	}

	public void reset() {
		lambda = false;
		nextNode = null;
		nextValue = "";
	}
}