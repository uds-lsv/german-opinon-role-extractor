package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'graph' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Graph {

	private Id root;

	private Terminals terminals;

	private Nonterminals nonterminals;

	static String xmltag = "graph";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the value of 'root' as an argument.
	 * 
	 * @param root
	 */
	public Graph(Id root) {
		super();
		this.root = root;
	}

	/**
	 * Returns the value of 'root'.
	 * 
	 * @return the root
	 */
	public Id getRoot() {
		return root;
	}

	/**
	 * Sets the value of 'root'.
	 * 
	 * @param root
	 *            the root to set
	 */
	public void setRoot(Id root) {
		this.root = root;
	}

	/**
	 * Returns the <code>Terminals</code> of this <code>Graph</code>. If it
	 * has not been set, then <code>null</code> is returned.
	 * 
	 * @return the terminals
	 */
	public Terminals getTerminals() {
		return terminals;
	}

	/**
	 * Sets the <code>Terminals</code> of this <code>Graph</code>.
	 * 
	 * @param terminals
	 *            the terminals to set
	 */
	public void setTerminals(Terminals terminals) {
		this.terminals = terminals;
	}

	/**
	 * Returns the <code>Nonterminals</code> of this <code>Graph</code>.
	 * 
	 * @return the nonterminals
	 */
	public Nonterminals getNonterminals() {
		return nonterminals;
	}

	/**
	 * Sets the <code>Nonterminals</code> of this <code>Graph</code>.
	 * 
	 * @param nonterminals
	 *            the nonterminals to set
	 */
	public void setNonterminals(Nonterminals nonterminals) {
		this.nonterminals = nonterminals;
	}

	/**
	 * Returns the name of the XML element of <code>Graph</code>, i. e.
	 * 'graph'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Graph</code>.
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " root=\"" + root.getId() + "\">" + newline);

		if (null != terminals) {
			buffer.append("\t\t\t\t" + terminals.toString());
		}

		if (null != nonterminals) {
			buffer.append("\t\t\t\t" + nonterminals.toString());
		}
		buffer.append("\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
