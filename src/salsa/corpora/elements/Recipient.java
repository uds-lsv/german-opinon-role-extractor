package salsa.corpora.elements;


/**
 * Represents the 'recipient' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Recipient {
	
	private String id;
	
	private String text;
	
	static String xmltag = "recipient";

	static String newline = System.getProperty("line.separator");

	public Recipient(String id){
		super();
		this.id = id;
	}

	/**
	 * Returns the user id (from the 'id' attribute).
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the user id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the recipient's name (PCDATA in XML), e. g. 'fabians'.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the recipient's name.
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the XML element name of <code>Recipient</code>, i. e. 'recipient'
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this <code>Recipient</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " id=\"" + id + "\">");
		buffer.append(text.trim());
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	
	
}
