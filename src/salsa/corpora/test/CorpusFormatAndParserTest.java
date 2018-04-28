package salsa.corpora.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import salsa.corpora.xmlparser.CorpusParser;

/**
 * Testklasse, die die Datenstruktur und den Parser testen soll.
 * 
 * @author Fabian Shirokov
 * 
 */
public class CorpusFormatAndParserTest {

	/**
	 * Liest einen Korpus ein und soll ihn wieder so rausschreiben, wie er
	 * eingelesen wurde
	 * 
	 * @param args
	 */
	public static void main(String[] args) {


		CorpusFormatAndParserTest cfpt = new CorpusFormatAndParserTest();
		cfpt.run();

	}

	private void run() {
		try {
		
			CorpusParser parser = new CorpusParser();

			
			
			writeToFile(parser.parseCorpusFromFile("testcorpus.xml").toString());
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the text into the file that has been set in the constructor. 
	 * @param text
	 * @throws IOException if the text could not be written to the given file
	 */
	public void writeToFile(String text) throws IOException {

		File outputFile = new File("testcorpusNEW.xml");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFile)));

		out.write(text);

		out.flush();

		out.close();

	}

}
