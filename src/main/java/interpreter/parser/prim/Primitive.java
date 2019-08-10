/**
 * Primitive.java is a part of Lispreter. 
 */
package interpreter.parser.prim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Primitive annotation supplies required data about all primitive
 * functions. The annotatation can only be applied to methods, where each method
 * represents a specific primitive function. The aliases are the different names
 * which can be used to call a function. Each function can take in zero or one
 * argument (which must be an S-Expression), as marked by an appropriate flag.
 * 
 * @author Anand
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Primitive {

	/**
	 * The collection of aliases for a specified primitive function.
	 * 
	 * @return a String array.
	 */
	public String[] aliases();

	/**
	 * Whether or not an S-Expression argument is required to call a function.
	 * Defaults to true because most functions require an argument in some
	 * capacity.
	 * 
	 * @return true if an S-Expression is required, false otherwise.
	 */
	public boolean sexpRequired() default true;
}
