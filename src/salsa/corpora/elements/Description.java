package salsa.corpora.elements;

/**
 * Represents the 'description' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Description {

	private String text;

	static String xmltag = "description";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Description() {
		super();

	}

	/**
	 * Returns the text (PCDATA in XML).
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text (PCDATA in XML).
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the XML element name of <code>Description</code>, i. e.
	 * 'description'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Description</code>.
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
