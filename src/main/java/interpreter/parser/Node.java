/**
 * Node.java is a part of Lispreter. 
 */
package interpreter.parser;

import interpreter.exception.EnvironmentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Main data structure for handling atoms and S-Expressions. A node maintains a
 * list of tokens which create the object.
 * <p>
 * The factory pattern is employed to create new Nodes, which is dependent on if
 * the Node is a valid S-Expression.
 * 
 * @author Anand
 *
 */
public abstract class Node {

    protected List<String> tokens = new ArrayList<>();

    /**
     * Determines whether this node is part of a list of expressions to be
     * evaluated.
     * 
     * @return true if part of a list, false otherwise.
     */
    public abstract boolean isList();

    /**
     * Evaluates this node.
     * 
     * @return the Node after evaluation.
     * @throws EnvironmentException
     *             if the node has no environment
     */
    public abstract Node eval();

    /**
     * Evaluates this node.
     * 
     * @param literal
     *            whether or not numericals should be evaluated literally.
     * @return the evaluated Node.
     */
    public abstract Node eval(boolean literal);

    /**
     * Evaluates this node.
     * 
     * @param literal
     *            whether or not numericals should be evaluated literally.
     * @param env
     *            a scoped variable environment.
     * @return the evaluation of this Node.
     */
    public abstract Node eval(boolean literal, Map<String, Node> env);

    /**
     * Evaluates this node.
     * 
     * @param env
     *            a scoped variable enivornment.
     * @return the evaluation of this Node.
     */
    public abstract Node eval(Map<String, Node> env);

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        if (tokens == null && other.tokens != null) {
            return false;
        }
        return tokens.equals(other.tokens);
    }
}
