package salsa.corpora.elements;

public class Author {

	private String text;
	
	static String xmltag = "author";

	static String newline = System.getProperty("line.separator");
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Author() {
		super();
	}

	/**
	 * Returns the text (PCDATA in XML).
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text (PCDATA in XML).
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the XML element name of <code>Author</code>.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this
	 * <code>Author</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">");
		if (null != text) {
			buffer.append(text.trim());
		}
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}
	

}
