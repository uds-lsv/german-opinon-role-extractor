package salsa.corpora.elements;

public class Wordtags {

	private String xmlns;

	private Wordtag wordtag;

	static String xmltag = "wordtags";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Wordtags() {
		super();
	}

	/**
	 * Constructor that takes the value of the 'xmlns' attribute as an argument.
	 * 
	 * @param xmlns
	 */
	public Wordtags(String xmlns) {
		super();
		this.xmlns = xmlns;
	}

	/**
	 * Returns the value of the 'xmlns' attribute.
	 * 
	 * @return the xmlns
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * Sets the value of the 'xmlns' attribute.
	 * 
	 * @param xmlns
	 *            the xmlns to set
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * Returns the <code>Wordtag</code> of this <code>Wordtags</code>.
	 * 
	 * @return the wordtag
	 */
	public Wordtag getWordtag() {
		return wordtag;
	}

	/**
	 * Sets the <code>Wordtag</code> of this <code>Wordtags</code>.
	 * 
	 * @param wordtag
	 *            the wordtag to set
	 */
	public void setWordtag(Wordtag wordtag) {
		this.wordtag = wordtag;
	}

	/**
	 * Returns the XML element name of <code>Wordtags</code>, i. e.
	 * 'wordtags'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Wordtags</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag);
		
		if (null != xmlns){
			buffer.append(" xmlns=\"" + xmlns + "\"");
		}
		
		buffer.append(">" + newline);

		if (null != wordtag) {
			buffer.append("\t\t\t\t\t" + wordtag.toString());
		}

		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
