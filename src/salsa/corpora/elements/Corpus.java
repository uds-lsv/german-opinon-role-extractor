package salsa.corpora.elements;

/**
 * Represents a corpus in the SalsaXML format.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Corpus {

	private String corpusname;

	private String target;

	private Head head;

	private Body body;

	static String xmltag = "corpus";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the name of the corpus and its target as
	 * arguments.
	 * 
	 * @param corpusname
	 * @param target
	 */
	public Corpus(String corpusname, String target) {
		super();
		this.corpusname = corpusname;
		this.target = target;
	}

	/**
	 * Returns the name of this <code>Corpus</code>, e. g.
	 * 'TIGER-Gesamt-Juli03'
	 * 
	 * @return
	 */
	public String getCorpusname() {
		return this.corpusname;
	}

	/**
	 * Returns the target of this <code>Corpus</code>, e. g. 'gehen'.
	 * 
	 * @return
	 */
	public String getTarget() {
		return this.target;
	}

	/**
	 * Returns the <code>Head</code> of this <code>Corpus</code>. <code>null</code>
	 * is returned if no <code>Head</code> has been set.
	 * 
	 * @return the head
	 */
	public Head getHead() {
		return head;
	}

	/**
	 * Sets the <code>Head</code> of this <code>Corpus</code>.
	 * 
	 * @param head
	 *            the head to set
	 */
	public void setHead(Head head) {
		this.head = head;
	}

	/**
	 * Returns the <code>Body</code> of this <code>Corpus</code>. <code>null</code>
	 * is returned if no <code>Body</code> has been set.
	 * 
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Sets the <code>Body</code> of this <code>Corpus</code>.
	 * 
	 * @param body
	 *            the body to set
	 */
	public void setBody(Body body) {
		this.body = body;
	}

	/**
	 * Returns the name of the XML element of the <code>Corpus</code> class,
	 * i. e. 'corpus'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Sets the name of the <code>Corpus</code>.
	 * 
	 * @param corpusname
	 *            the corpusname to set
	 */
	public void setCorpusname(String corpusname) {
		this.corpusname = corpusname;
	}

	/**
	 * Sets the target of this <code>Corpus</code>.
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Returns a recursively created XML representation of a SALSA corpus,
	 * including the header &lt;?xml version="1.0" encoding="UTF-8"?&gt;
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + newline);

		buffer.append("<" + xmltag + " corpusname=\"" + corpusname
				+ "\" target=\"" + target + "\">" + newline);

		if (null != head) {
			buffer.append("\t" + head.toString());
		}
		if (null != body) {
			buffer.append("\t" + body.toString());
		}

		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
