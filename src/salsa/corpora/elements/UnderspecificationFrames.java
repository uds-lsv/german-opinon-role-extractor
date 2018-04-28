package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'uspframes' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class UnderspecificationFrames {

	private ArrayList<Uspblock> uspblocks;

	static String xmltag = "uspframes";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public UnderspecificationFrames() {
		super();
		uspblocks = new ArrayList<Uspblock>();
	}

	/**
	 * Returns the list of <code>Uspblock</code> elements.
	 * 
	 * @return the uspblocks
	 */
	public ArrayList<Uspblock> getUspblocks() {
		return uspblocks;
	}

	/**
	 * Adds a new <code>Uspblock</code> to this
	 * <code>UnderspecificationFrames</code>.
	 */
	public void addUspblock(Uspblock newUspblock) {
		this.uspblocks.add(newUspblock);
	}

	/**
	 * Returns the XML element name of <code>UnderspecificationFrames</code>,
	 * i. e. 'uspframes'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>UnderspecificationFrames</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);

		for (Uspblock currentUspblock : uspblocks) {
			buffer.append("\t\t\t\t\t\t" + currentUspblock.toString());
		}

		buffer.append("\t\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
