package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'edge' section in the SalsaXML corpora.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Edge {

	private Id idref;

	private String label;

	static String xmltag = "edge";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes an <code>Id</code> (from the 'idref' XML
	 * attribute) and the label as arguments.
	 * 
	 * @param idref
	 *            e. g. 's539_13'
	 * @param label
	 *            e. g. 'HD'
	 */
	public Edge(Id idref, String label) {
		this.idref = idref;
		this.label = label;
	}

	/**
	 * Returns the <code>Id</code> (from the 'idref' XML attribute) of this
	 * <code>Edge</code>.
	 * 
	 * @return
	 */
	public Id getId() {
		return idref;
	}

	/**
	 * Returns the label of this <code>Edge</code>.
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the name of the XML element of <code>Edge</code>, i. e.
	 * 'edge'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Edge</code>.
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " idref=\"" + idref.getId()
				+ "\" label=\"" + label + "\"/>" + newline);

		return buffer.toString();
	}

}
