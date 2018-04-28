package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents a 'fenode' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Fenode {
	
	private Id idref;
	
	private String is_split;
	
	static String xmltag = "fenode";

	static String newline = System.getProperty("line.separator");
	
	/**
	 * Default constructor that takes the <code>Id</code> (from the 'idref' attribute)
	 * and is_split as arguments. 
	 */
	public Fenode(Id idref, String is_split) {
		super();
		this.idref = idref;
		this.is_split = is_split;
	}
	
	/**
	 * Constructor that takes only the <code>Id</code> (from the 'idref' attribute)
	 * as an argument. This actually does not conform to the SalsaXML.dtd, but 
	 * unfortunately there are corpora where such cases exist.
	 */
	public Fenode(Id idref) {
		super();
		this.idref = idref;
		
	}

	/**
	 * Returns the <code>Id</code> (from the 'idref' attribute).
	 * @return the idref
	 */
	public Id getIdref() {
		return idref;
	}

	/**
	 * Sets the <code>Id</code> (from the 'idref' attribute).
	 * @param idref the idref to set
	 */
	public void setIdref(Id idref) {
		this.idref = idref;
	}

	/**
	 * 
	 * Returns the value of the is_split attribute, e. g. 'yes'.
	 * @return the is_split
	 */
	public String getIs_split() {
		return is_split;
	}

	/**
	 * Sets the value of the 'is_split' attribute.
	 * @param is_split the is_split to set
	 */
	public void setIs_split(String is_split) {
		this.is_split = is_split;
	}

	/**
	 * Returns the name of the XML element of <code>Fenode</code>.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of <code>Fenode</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " idref=\"" + idref.getId()
				+ "\"");
		
		if (null != is_split){
			buffer.append(" is_split=\"" + is_split + "\"");
		}
		buffer.append("/>" + newline);

		return buffer.toString();
	}

	
	


}
