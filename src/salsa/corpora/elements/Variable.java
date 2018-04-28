package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'variable' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Variable {

	private String name;

	private Id id;

	private String text;

	static String xmltag = "variable";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the value of the 'name' attribute and the
	 * <code>Id</code> (value of the 'idref' attribute) as arguments.
	 * 
	 * @param name
	 * @param id
	 */
	public Variable(String name, Id id) {
		super();
		this.name = name;
		this.id = id;
	}

	/**
	 * Returns the value of the 'name' attribute.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the 'name' attribute.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the <code>Id</code> (value of the 'idref' attribute).
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code> (value of the 'idref' attribute).
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
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
	 * Returns the XML element name of <code>Variable</code>, i. e.
	 * 'variable'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Variable</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " name=\"" + name + "\" idref=\""
				+ id.getId() + "\">" + newline);
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
