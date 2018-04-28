package salsa.corpora.elements;

/**
 * Represents the 'global' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Global {
	
	private String param;
	
	private String type;
	
	private String text;
	
	static String xmltag = "global";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the type as an argument.
	 * @param type
	 */
	public Global(String type) {
		super();
		this.type = type;
	}

	/**
	 * Returns the value of the 'param' attribute. If it has not been set, then
	 * <code>null</code> is returned.
	 * @return the param
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Sets the value of the 'param' attribute.
	 * @param param the param to set
	 */
	public void setParam(String param) {
		this.param = param;
	}

	/**
	 * Returns the value of the 'type' attribute.
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the 'type' attribute.
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the text (PCDATA in XML).
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the name of the XML element of <code>Global</code>, i. e. 'global'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of <code>Global</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " type=\"" + type
				+ "\"");
		
		if (null != param) {
			buffer.append(" param=\"" + param + "\"");
		}
		
		buffer.append(">");
		
		if (null != text) {
			buffer.append(text);
		}
		
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}




}
