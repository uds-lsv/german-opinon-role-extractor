package salsa.corpora.elements;

/**
 * Represents the 'step' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Step {

	private String step;

	static String xmltag = "step";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Step() {
		super();

	}

	/**
	 * Returns the step.
	 * 
	 * @return the step
	 */
	public String getStep() {
		return step;
	}

	/**
	 * Sets the step (PCDATA in XML).
	 * 
	 * @param step
	 *            the step to set
	 */
	public void setStep(String step) {
		this.step = step;
	}

	/**
	 * Returns the XML element name of <code>Step</code>.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML represenation of this <code>Step</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">");
		if (null != step) {
			buffer.append(step.trim());
		}
		buffer.append("</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
