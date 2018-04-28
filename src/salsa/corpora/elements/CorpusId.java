package salsa.corpora.elements;

/**
 * Represents the 'corpus_id' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class CorpusId {

	private String id;
	
	static String xmltag = "corpus_id";
	
	static String newline = System.getProperty("line.separator");
	
	
	/**
	 * Zero-argumented default constructor.
	 * @param id
	 */
	public CorpusId() {
		super();
	}
	
	



	/**
	 * Returns the id (PCDATA in XML).
	 * @return the id
	 */
	public String getId() {
		return id;
	}





	/**
	 * Sets the id (PCDATA in XML).
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}





	/**
	 * Returns the name of the XML element of <code>CorpusId</code>, i. e. 'corpus_id'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}





	/**
	 * Returns a recursively created XML representation of <code>CorpusId</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">");
		buffer.append(id.trim());
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}
}
