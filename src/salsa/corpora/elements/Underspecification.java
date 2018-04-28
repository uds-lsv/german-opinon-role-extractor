package salsa.corpora.elements;

/**
 * Represents the 'usp' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Underspecification {

	private UnderspecificationFrames uspframes;

	private UnderspecificationFrameElements uspfes;

	static String xmltag = "usp";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Underspecification() {
		super();
	}

	/**
	 * Returns the <code>UnderspecificationFrames</code> of this
	 * <code>Underspecification</code>.
	 * 
	 * @return the uspframes
	 */
	public UnderspecificationFrames getUspframes() {
		return uspframes;
	}

	/**
	 * Sets the <code>UnderspecificationFrames</code> of this
	 * <code>Underspecification</code>.
	 * 
	 * @param uspframes
	 *            the uspframes to set
	 */
	public void setUspframes(UnderspecificationFrames uspframes) {
		this.uspframes = uspframes;
	}

	/**
	 * Returns the <code>UnderspecificationFrameElements</code> of this
	 * <code>Underspecification</code>.
	 * 
	 * @return the uspfes
	 */
	public UnderspecificationFrameElements getUspfes() {
		return uspfes;
	}

	/**
	 * Sets the <code>UnderspecificationFrameElements</code> of this
	 * <code>Underspecification</code>.
	 * 
	 * @param uspfes
	 *            the uspfes to set
	 */
	public void setUspfes(UnderspecificationFrameElements uspfes) {
		this.uspfes = uspfes;
	}

	/**
	 * Returns the XML element name of <code>Underspecification</code>, i. e.
	 * 'usp'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Underspecification</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);

		if (null != uspframes) {
			buffer.append("\t\t\t\t\t" + uspframes.toString());
		}
		if (null != uspfes) {
			buffer.append("\t\t\t\t\t" + uspfes.toString());
		}

		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
