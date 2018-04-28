package salsa.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * <code>MyFileWriter</code> writes a text to a file, using buffered writing
 * methods.
 * 
 * @author Fabian
 * 
 */
public class MyFileWriter {

	private String fileName;

	/**
	 * Constructor that takes the name of the file to be written to.
	 * @param fileName
	 */
	public MyFileWriter(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Writes the text into the file that has been set in the constructor. 
	 * @param text
	 * @throws IOException if the text could not be written to the given file
	 */
	public void writeToFile(String text) throws IOException {

		File outputFile = new File(fileName);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFile)));

		out.write(text);

		out.flush();

		out.close();

	}

}
