package salsa.corpora.elements;

/**
 * Represents the 'name' section of a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Name {
	
	private String text;
	
	static String xmltag = "name";

	static String newline = System.getProperty("line.separator");
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Name() {
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
	 * Returns the XML element name of <code>Name</code>.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this
	 * <code>Name</code>.
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
