package salsa.corpora.elements;

/**
 * Represents a 'flag' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Flag {

	private String forWhat;

	private String name;

	private String source;

	private String text;

	static String xmltag = "flag";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the value of the 'name' attribute as an
	 * argument.
	 * 
	 * @param name
	 */
	public Flag(String name) {
		super();
		this.name = name;
	}

	/**
	 * Constructor that takes the values of the 'for' and the 'name' attribute
	 * as arguments.
	 * 
	 * @param name
	 * @param forWhat
	 *            value of the 'for' attribute
	 * 
	 */
	public Flag(String name, String forWhat) {
		super();
		this.name = name;
		this.forWhat = forWhat;
	}

	/**
	 * Returns the value of the 'for' attribute, e. g. 'frame'.
	 * 
	 * @return
	 */
	public String getForWhat() {
		return forWhat;
	}

	/**
	 * Sets the value of the 'for' attribute.
	 * 
	 * @param forWhat
	 */
	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	/**
	 * Returns the name of the flag, e. g. 'Reexamine'.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this <code>Flag</code>.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the source, e. g. '1'
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the name of the XML element of <code>Flag</code>.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of a <code>Flag</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag);
		if (null != forWhat) {
			buffer.append(" for=\"" + forWhat + "\"");
		}
		buffer.append(" name=\"" + name + "\"");
		if (null != source) {
			buffer.append(" source=\"" + source + "\"");
		}

		if (null != text) {
			buffer.append(">" + newline);
			buffer.append("\t\t\t\t" + text.trim());
			buffer.append("\t\t\t</" + xmltag + ">" + newline);

		} else {
			buffer.append("/>" + newline);
		}


		return buffer.toString();
	}

}
