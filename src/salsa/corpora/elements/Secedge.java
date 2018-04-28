package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'secedge' section of a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Secedge {

	private Id id;

	private String label;

	static String xmltag = "secedge";

	static String newline = System.getProperty("line.separator");

	/**
	 * Constructor that takes the <code>Id</code> (from the 'idref' attribute)
	 * and the label as arguments.
	 * 
	 * @param id
	 *            from the 'idref' attribute
	 * @param label
	 */
	public Secedge(Id id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	/**
	 * Returns the <code>Id</code> (from the 'idref' attribute).
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code> (from the 'idref' attribute).
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the value of the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value of the label.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns the XML element name of <code>Secedge</code>.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Secedge</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " idref=\"" + id.getId() + "\" label=\""
				+ label + "\"/>" + newline);
		

		return buffer.toString();
	}

}
