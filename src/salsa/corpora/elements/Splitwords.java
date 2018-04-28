package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'splitwords' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Splitwords {

	private ArrayList<Splitword> splitwords;

	static String xmltag = "splitwords";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Splitwords() {
		super();
		splitwords = new ArrayList<Splitword>();
	}

	/**
	 * Returns the list of <code>Splitword</code> of this
	 * <code>Splitwords</code>.
	 * 
	 * @return the splitwords
	 */
	public ArrayList<Splitword> getSplitwords() {
		return splitwords;
	}

	/**
	 * Adds a new <code>Splitword</code> to this <code>Splitwords</code>.
	 */
	public void addSplitword(Splitword newSplitword) {
		this.splitwords.add(newSplitword);
	}

	/**
	 * Returns the XML element name of <code>Splitwords</code>, i. e.
	 * 'splitwords'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Splitwords</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);

		for (Splitword currentSplitword : splitwords) {
			buffer.append("\t\t\t\t\t" + currentSplitword.toString());
		}

		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
