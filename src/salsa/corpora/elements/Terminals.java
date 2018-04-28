package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'terminals' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Terminals {

	private ArrayList<Terminal> terminals;

	static String xmltag = "terminals";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Terminals() {
		super();
		this.terminals = new ArrayList<Terminal>();
	}

	/**
	 * Returns the list of <code>Terminal</code> elements.
	 * 
	 * @return the terminals
	 */
	public ArrayList<Terminal> getTerminals() {
		return terminals;
	}

	/**
	 * Adds a new <code>Terminal</code> to this <code>Terminals</code>.
	 */
	public void addTerminal(Terminal newTerminal) {
		this.terminals.add(newTerminal);
	}

	/**
	 * Returns the name of the XML element of <code>Terminals</code>, i. e.
	 * 'terminals'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Terminals</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);

		for (Terminal currentTerminal : terminals) {
			buffer.append("\t\t\t\t\t" + currentTerminal.toString());
		}

		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
