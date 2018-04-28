package salsa.corpora.elements;

/**
 * Represents a 'format' section of a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Format {
	

	private String format;
	
	static String xmltag = "format";
	
	static String newline = System.getProperty("line.separator");
	
	
	/**
	 * Zero-argumented default constructor.
	 * @param id
	 */
	public Format() {
		super();
		
	}
	

	/**
	 * Returns the format, e. g. 'NeGra format, version 3'.
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the format of this <code>Format</code> (written as PCDATA in XML).
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Returns the name of the XML element of <code>Format</code>.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}


	/**
	 * Returns a recursively created XML representation of this <code>Format</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">");
		buffer.append(format.toString().trim());
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}


}
