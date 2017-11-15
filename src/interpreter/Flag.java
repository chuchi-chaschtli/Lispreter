/**
 * Flag.java is a part of Lispreter. 
 */
package interpreter;

/**
 * Enumeration representing all possible command line arguments. All command
 * line args must have a '-' prefix, otherwise it is not registered as an
 * argument.
 * <p>
 * Currently, any input or output file may be specified using the flag
 * {@code -in} or {@code -out}, respectively, followed by a file path.
 * <p>
 * If an error occurs during program evaluation, {@code -d} can be specified to
 * debug the execution.
 * 
 * @author Anand
 *
 */
public enum Flag {
	DEBUG("-d"),
	INPUT_FILE("-i(n)?", 1),
	OUTPUT_FILE("-o(ut)?", 1);

	private String regex;
	private int argsNeeded;

	private Flag(String regex) {
		this(regex, 0);
	}

	private Flag(String regex, int argsNeeded) {
		if (!regex.startsWith("-")) {
			regex = "-" + regex;
		}
		this.regex = regex;
		this.argsNeeded = argsNeeded;
	}

	boolean matches(String input) {
		return input.matches(regex);
	}

	boolean containsFlag(String[] args) {
		for (String s : args) {
			if (matches(s)) {
				return true;
			}
		}
		return false;
	}

	String[] getParts(String[] args) {
		String[] result = new String[argsNeeded];
		int offset = -1;
		for (int i = 0; i < args.length; i++) {
			if (matches(args[i])) {
				offset = i;
				break;
			}
		}
		if (offset < 0) {
			throw new IllegalArgumentException(
					"Cannot find specifier for this flag.");
		} else if (offset + argsNeeded >= args.length) {
			throw new IllegalArgumentException(
					"Invalid number of arguments specified for this flag.");
		}
		for (int i = 0; i < argsNeeded; i++) {
			result[i] = args[i + offset + 1];
		}
		return result;
	}
}
