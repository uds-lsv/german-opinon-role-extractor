package salsa.corpora.elements;

import java.util.ArrayList;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'target' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Target {

	private Id id;

	private String lemma;

	private String headlemma;

	private ArrayList<Fenode> fenodes;

	static String xmltag = "target";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented constructor. According to the SalsaXML.dtd each 'target'
	 * element is required to have a 'lemma' attribute, but there are corpora
	 * that do not conform to this rule.
	 */
	public Target() {
		super();
		fenodes = new ArrayList<Fenode>();
	}

	/**
	 * Default constructor that takes the value of the 'lemma' attribute as an
	 * argument.
	 * 
	 * @param lemma
	 */
	public Target(String lemma) {
		super();
		this.lemma = lemma;
		fenodes = new ArrayList<Fenode>();
	}

	/**
	 * Returns the <code>Id</code> of this <code>Target</code>.
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code> of this target.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the lemma.
	 * 
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Sets the lemma.
	 * 
	 * @param lemma
	 *            the lemma to set
	 */
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	/**
	 * Returns the headlemma.
	 * 
	 * @return the headlemma
	 */
	public String getHeadlemma() {
		return headlemma;
	}

	/**
	 * Sets the headlemma.
	 * 
	 * @param headlemma
	 *            the headlemma to set
	 */
	public void setHeadlemma(String headlemma) {
		this.headlemma = headlemma;
	}

	/**
	 * Returns the list of <code>Fenode</code> elements of this
	 * <code>Target</code>.
	 * 
	 * @return the fenodes
	 */
	public ArrayList<Fenode> getFenodes() {
		return fenodes;
	}

	/**
	 * Adds a new <code>Fenode</code> to this <code>Target</code>.
	 */
	public void addFenode(Fenode newFenode) {
		this.fenodes.add(newFenode);
	}

	/**
	 * Returns the XML element name of <code>Target</code>, i. e. 'target'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Target</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag);

		if (null != id) {
			buffer.append(" id=\"" + id.getId() + "\"");
		}
		if (null != lemma) {
			buffer.append(" lemma=\"" + lemma + "\"");
		}
		if (null != headlemma) {
			buffer.append(" headlemma=\"" + headlemma + "\"");
		}
		buffer.append(">" + newline);

		for (Fenode currentFenode : fenodes) {
			buffer.append("\t\t\t\t\t\t\t" + currentFenode.toString());
		}

		buffer.append("\t\t\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
