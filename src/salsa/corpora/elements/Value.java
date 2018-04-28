package salsa.corpora.elements;

/**
 * Represents the 'value' section of a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Value {
	
	private String name;
	
	private String text;
	
	static String xmltag = "value";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the value of the 'name' attribute as 
	 * an argument.
	 * @param name
	 */
	public Value(String name) {
		super();
		this.name = name;
	}

	/**
	 * Returns the value of the 'name' attribute.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the 'name' attribute.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the text of this <code>Value</code> (PCDATA in XML).
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text of this <code>Value</code> (PCDATA in XML).
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the XML element name of <code>Value</code>, i. e. 'value'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	

	/**
	 * Returns a recursively created XML representation of this <code>Value</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " name=\"" + name + "\">");
		if (null != text){
			buffer.append(text.trim());
		}		
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}


	
}
