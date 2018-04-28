package salsa.corpora.elements;

/**
 * Represents the 'element' section in SalsaXML corpora.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Element {

	private String name;

	private String optional;

	static String xmltag = "element";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes <code>name</code> (e. g. 'Agent') and
	 * <code>optional</code> (e. g. 'true') as attributes.
	 * 
	 * @param corpusname
	 * @param target
	 */
	public Element(String name, String optional) {
		super();
		this.name = name;
		this.optional = optional;
	}

	/**
	 * Returns the name of this <code>Element</code>.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this <code>Element</code>.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of 'optional'.
	 * 
	 * @return the optional
	 */
	public String getOptional() {
		return optional;
	}

	/**
	 * Sets the value of 'optional'.
	 * 
	 * @param optional
	 */
	public void setOptional(String optional) {
		this.optional = optional;
	}

	/**
	 * Returns the name of the XML element, i. e. 'element'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Element</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " name=\"" + name + "\" optional=\""
				+ optional + "\"/>" + newline);

		return buffer.toString();
	}

}
