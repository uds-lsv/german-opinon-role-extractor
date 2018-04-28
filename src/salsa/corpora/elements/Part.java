package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'part' section of a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Part {

	private String word;

	private Id id;

	static String xmltag = "part";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the values of the 'word' and 'id'
	 * attributes as arguments.
	 * 
	 * @param word
	 * @param id
	 */
	public Part(String word, Id id) {
		super();
		this.word = word;
		this.id = id;
	}

	/**
	 * Returns the value of 'word'.
	 * 
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Sets the value of 'word'.
	 * 
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * Returns the <code>Id</code>.
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code>.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the XML element name of <code>Part</code>, i. e. 'part'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Part</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " word=\"" + word + "\" id=\"" + id.getId()
				+ "\"/>" + newline);

		return buffer.toString();
	}

}
