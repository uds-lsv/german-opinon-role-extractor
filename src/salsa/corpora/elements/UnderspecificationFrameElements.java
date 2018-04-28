package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'uspfes' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class UnderspecificationFrameElements {

	private ArrayList<Uspblock> uspblocks;

	static String xmltag = "uspfes";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public UnderspecificationFrameElements() {
		super();
		this.uspblocks = new ArrayList<Uspblock>();
	}

	/**
	 * Returns the list of <code>Uspblock</code>.
	 * 
	 * @return the uspblocks
	 */
	public ArrayList<Uspblock> getUspblocks() {
		return uspblocks;
	}

	/**
	 * Adds a new <code>Uspblock</code> element to this
	 * <code>UnderspecificationFrameElements</code>.
	 */
	public void addUspblock(Uspblock newUspblock) {
		this.uspblocks.add(newUspblock);
	}

	/**
	 * Returns the XML element name of
	 * <code>UnderspecificationFrameElements</code>, i. e. 'uspfes'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Wordtags</code>.
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
