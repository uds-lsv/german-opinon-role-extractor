package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'nonterminals' section of a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Nonterminals {
	
	ArrayList<Nonterminal> nonterminals;
	
	static String xmltag = "nonterminals";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Nonterminals() {
		super();
		nonterminals = new ArrayList<Nonterminal>();
	}

	/**
	 * Returns the list of <code>Nonterminal</code> elements.
	 * @return the nonterminals
	 */
	public ArrayList<Nonterminal> getNonterminals() {
		return nonterminals;
	}
	
	/**
	 * Adds a new <code>Nonterminal</code> to this <code>Nonterminals</code>.
	 * @param newNonterminal
	 */
	public void addNonterminal(Nonterminal newNonterminal){
		this.nonterminals.add(newNonterminal);
	}

	/**
	 * Returns the XML element name of <code>Nonterminals</code>, i. e. 'nonterminals'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this <code>Nonterminals</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);
		
		for (Nonterminal currentNonterminal : nonterminals){
			buffer.append("\t\t\t\t\t" + currentNonterminal.toString());
		}
		
		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}
}
