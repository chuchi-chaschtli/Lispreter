/**
 * Lispreter.java is a part of Lispreter. 
 */
package interpreter;

import interpreter.lexer.Lexer;
import interpreter.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * @author Anand
 *
 */
public class Lispreter {

	public static void main(String[] args) {
		try {
			Lexer lexer;

			if (Flag.INPUT_FILE.containsFlag(args)) {
				lexer = new Lexer(new FileInputStream(
						Flag.INPUT_FILE.getParts(args)[0]));
			} else {
				lexer = new Lexer(System.in);
			}

			Parser parser;
			FileWriter writer = null;
			if (Flag.OUTPUT_FILE.containsFlag(args)) {
				writer = new FileWriter(getOutputFile(Flag.OUTPUT_FILE.getParts(args)[0]));
				parser = new Parser(lexer.getTokens(), writer);
			} else {
				parser = new Parser(lexer.getTokens());
			}
			parser.eval();
			if (writer != null) {
				writer.close();
			}
		}
		catch (Exception e) {
			System.out.println("Error occurred!");
			if (Flag.DEBUG.containsFlag(args)) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} else {
				System.out.println("Specify '-d' to debug the error!");
			}
		}
	}
	
	private static File getOutputFile(String path) {
		File file = new File(path);
		long counter = 0;
		while (file.exists()) {
			String[] parts = path.split("\\.");
			StringBuffer buffy = new StringBuffer();
			for (int i = 0; i < parts.length - 1; i++) {
				buffy.append(parts[i]).append(
						i == parts.length - 2 ? "" : ".");
			}
			file = new File(buffy.toString() + ++counter + "." + parts[parts.length - 1]);
		}
		return file;
	}
}
