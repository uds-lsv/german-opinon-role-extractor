package salsa.corpora.xmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import salsa.corpora.elements.Corpus;

public class CorpusParser {

	private SAXParser parser;


	private CorpusHandler handler;

	/**
	 * Zero-argumented default constructor.
	 * 
	 * @throws ParserConfigurationException
	 *             if there is a problem with the parser factory
	 * @throws SAXException
	 *             if there is a problem when creating the parser
	 */
	public CorpusParser() throws ParserConfigurationException, SAXException {

		// init parser factory
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();

		parserFactory.setValidating(true);

		parserFactory.setNamespaceAware(false);
		// create parser
		this.parser = parserFactory.newSAXParser();

		// create handler
		this.handler = new CorpusHandler();
	}

	/**
	 * Parses the XML document and returns the equivalent <code>Corpus</code>. 
	 * It reads only files in the 'utf-8' format.
	 * 
	 * @param aFileName
	 *            a <code>String</code> with the file name to read the XML
	 *            document from
	 * @throws IOException
	 *             if there is a problem when reading the file
	 * @throws SAXException
	 *             if there is a problem when parsing the XML document
	 */
	public Corpus parseCorpusFromFile(String aFileName) throws IOException,
			SAXException {

		FileInputStream is = new FileInputStream(new File(aFileName));
		
		this.parser.parse(new InputSource(new InputStreamReader(is, "UTF-8")),
				this.handler);
		
		return handler.getCorpus();

	}

}
