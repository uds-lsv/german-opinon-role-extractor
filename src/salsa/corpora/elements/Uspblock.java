package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'uspblock' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Uspblock {
	
	private ArrayList<Uspitem> uspitems;

	static String xmltag = "uspblock";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Uspblock() {
		super();
		uspitems = new ArrayList<Uspitem>();
	}

	/**
	 * Returns the list of <code>Uspitem</code>.
	 * @return the uspitems
	 */
	public ArrayList<Uspitem> getUspitems() {
		return uspitems;
	}
	
	/**
	 * Adds a new <code>Uspitem</code> to this <code>Uspblock</code>.
	 */
	public void addUspitem(Uspitem newUspitem){
		this.uspitems.add(newUspitem);
	}

	/**
	 * Returns the XML element name of <code>Uspblock</code>, i. e. 'uspblock'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	

	/**
	 * Returns a recursively created XML representation of this <code>Uspblock</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);
		
		for (Uspitem currentUspitem : uspitems){
			buffer.append("\t\t\t\t\t\t\t" + currentUspitem.toString());
		}
		
		buffer.append("\t\t\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	
}
