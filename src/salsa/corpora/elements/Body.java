package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'body' section of a SalsaXML corpus.
 */
public class Body {
	
	ArrayList<Sentence> sentences;

	static String xmltag = "body";

	static String newline = System.getProperty("line.separator");

	
	/**
	 * Zero-argumented default constructor.
	 */
	public Body() {
		super();
		sentences = new ArrayList<Sentence>();
	}


	/**
	 * Returns a list of <code>Sentence</code> elements.
	 * @return the sentences
	 */
	public ArrayList<Sentence> getSentences() {
		return sentences;
	}

	/**
	 * Returns the name of the XML element of <code>Body</code>, i. e. 'body'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Adds a new <code>Sentence</code> to this <code>Body</code>.
	 * @param newSentence
	 */
	public void addSentence(Sentence newSentence) {
		this.sentences.add(newSentence);
	}
	

	/**
	 * Returns a recursively created XML representation of this <code>Body</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);

		for (Sentence currentSentence : sentences) {
			buffer.append("\t\t" + currentSentence.toString());
		}
		
		buffer.append("\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}


	

}
