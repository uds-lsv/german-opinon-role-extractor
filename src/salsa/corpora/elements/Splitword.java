package salsa.corpora.elements;

import java.util.ArrayList;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'splitword' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Splitword {
	
	private Id id;
	
	private ArrayList<Part> parts;
	
	static String xmltag = "splitword";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the <code>Id</code> (from the 'idref' attribute)
	 * as an argument.
	 * @param id
	 */
	public Splitword(Id id) {
		super();
		this.id = id;
		parts = new ArrayList<Part>();
	}

	/**
	 * Returns the <code>Id</code> (from the 'idref' attribute).
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code> (from the 'idref' attribute).
	 * @param id the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the list of <code>Part</code> elements of this <code>Splitword</code>.
	 * @return the parts
	 */
	public ArrayList<Part> getParts() {
		return parts;
	}
	
	/**
	 * Adds a new <code>Part</code> to this <code>Splitword</code>.
	 */
	public void addPart(Part newPart){
		this.parts.add(newPart);
	}

	/**
	 * Returns the XML element name of <code>Splitword</code>, i. e. 'splitword'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	

	/**
	 * Returns a recursively created XML representation of this <code>Splitword</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " idref=\"" + id.getId() + "\">" + newline);
		
		for (Part currentPart : parts){
			buffer.append("\t\t\t\t\t\t" + currentPart);
		}
		
		buffer.append("\t\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	
	
	
	


}
