package salsa.corpora.elements;

/**
 * Represents the 'wordtag' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Wordtag {
	
	private String name;

	static String xmltag = "wordtag";

	static String newline = System.getProperty("line.separator");
	
	public Wordtag(String name){
		super();
		this.name = name;
	}

	/**
	 * Returns the value of the 'name' attribute.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the 'name' attribute.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the XML element name of <code>Wordtag</code>.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	

	/**
	 * Returns a recursively created XML representation of this <code>Wordtag</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " name=\"" + name + "\"/>" + newline);

		return buffer.toString();
	}

	


}
