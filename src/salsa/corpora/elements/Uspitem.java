package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'uspitem' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Uspitem {

	private Id id;

	static String xmltag = "uspitem";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the <code>Id</code> (from the 'idref'
	 * attribute) as an argument.
	 * 
	 * @param id
	 */
	public Uspitem(Id id) {
		super();
		this.id = id;
	}

	/**
	 * Returns the <code>Id</code>.
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code>.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the XML element name of this <code>Uspitem</code>.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Uspitem</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag);
		
		if (null != id.getId()) {
			buffer.append(" idref=\"" + id.getId() + "\"");
		}
		buffer.append("/>" + newline);

		return buffer.toString();
	}

}
