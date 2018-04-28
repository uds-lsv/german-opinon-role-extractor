package salsa.corpora.elements;

public class Date {

	private String text;
	
	static String xmltag = "date";

	static String newline = System.getProperty("line.separator");
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Date() {
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
	 * Returns the XML element name of <code>Date</code>.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this
	 * <code>Date</code>.
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
