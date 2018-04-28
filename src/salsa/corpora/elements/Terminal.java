package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents a 't' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Terminal {

	private Id id;

	private String lemma;

	private String morph;

	private String pos;

	private String word;

	private Secedge secedge;

	static String xmltag = "t";

	static String newline = System.getProperty("line.separator");

	/**
	 * This zero-argumented constructor allows you to initialize a
	 * <code>Terminal</code> without any attributes. This is because there are
	 * some corpora where not all attributes ('id', 'lemma', 'morph', 'pos',
	 * 'word') exist. But this does actually <strong>not conform</strong> to
	 * the SalsaXML.dtd.
	 * 
	 */
	public Terminal() {
		super();
	}

	/**
	 * Default constructor that takes the following arguments: The
	 * <code>Id</code>, the value of 'lemma', 'morph', 'pos' and 'word'.
	 * 
	 * @param id
	 * @param lemma
	 * @param morph
	 * @param pos
	 * @param word
	 */
	public Terminal(Id id, String lemma, String morph, String pos, String word) {
		super();
		this.id = id;
		lemma = lemma.replaceAll("<", "&lt;");
		lemma = lemma.replaceAll(">", "&gt;");
		lemma = lemma.replaceAll("\"", "&quot;");
		lemma = lemma.replaceAll("&", "&amp;");
		this.lemma = lemma;
		this.morph = morph;
		this.pos = pos;
		word = word.replaceAll("<", "&lt;");
		word = word.replaceAll(">", "&gt;");
		word = word.replaceAll("\"", "&quot;");
		word = word.replaceAll("&", "&amp;");
		this.word = word;
	}

	/**
	 * Returns the <code>Id</code>.
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code>.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the value of the 'lemma' attribute.
	 * 
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Sets the value of the 'lemma' attribute.
	 * 
	 * @param lemma
	 *            the lemma to set
	 */
	public void setLemma(String lemma) {
		
		lemma.replaceAll("<", "&lt;");
		lemma.replaceAll(">", "&gt;");
		
		this.lemma = lemma;
	}

	/**
	 * Returns the value of the 'morph' attribute.
	 * 
	 * @return the morph
	 */
	public String getMorph() {
		return morph;
	}

	/**
	 * Sets the value of the 'morph' attribute.
	 * 
	 * @param morph
	 *            the morph to set
	 */
	public void setMorph(String morph) {
		this.morph = morph;
	}

	/**
	 * Returns the value of the 'pos' attribute.
	 * 
	 * @return the pos
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * Sets the value of the 'pos' attribute.
	 * 
	 * @param pos
	 *            the pos to set
	 */
	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * Returns the value of the 'word' attribute.
	 * 
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Sets the value of the 'word' attribute.
	 * 
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * Returns the <code>Secedge</code>.
	 * 
	 * @return the secedge
	 */
	public Secedge getSecedge() {
		return secedge;
	}

	/**
	 * Sets the <code>Secedge</code>.
	 * 
	 * @param secedge
	 *            the secedge to set
	 */
	public void setSecedge(Secedge secedge) {
		this.secedge = secedge;
	}

	/**
	 * Returns the XML element name of <code>Terminal</code>, i. e. 't'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Terminal</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag);

		if (null != word) {
			buffer.append(" word=\"" + word + "\"");
		}
		if (null != id) {
			buffer.append(" id=\"" + id.getId() + "\"");
		}
		if (null != morph) {
			buffer.append(" morph=\"" + morph + "\"");
		}
		if (null != pos) {
			buffer.append(" pos=\"" + pos + "\"");
		}
		if (null != lemma) {
			buffer.append(" lemma=\"" + lemma + "\"");
		}

		if (null != secedge) {
			
			buffer.append(">" + newline);
			
			buffer.append("\t\t\t\t\t\t" + secedge.toString());

			buffer.append("\t\t\t\t\t</" + xmltag + ">" + newline);
		} else {
			buffer.append("/>" + newline);
		}

		return buffer.toString();
	}

}
