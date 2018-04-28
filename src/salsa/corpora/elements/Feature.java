package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents a 'feature' in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Feature {

	private String domain;

	private String name;

	private ArrayList<Value> values;

	static String xmltag = "feature";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the domain and the name of this
	 * <code>Feature</code>.
	 * 
	 * @param domain
	 * @param name
	 */
	public Feature(String domain, String name) {
		super();
		this.domain = domain;
		this.name = name;
		this.values = new ArrayList<Value>();
	}

	/**
	 * Returns the domain of this <code>Feature</code>.
	 * 
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 * 
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Returns the name of this <code>Feature</code>.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this <code>Feature</code>.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the list of <code>Value</code> elements of this
	 * <code>Feature</code>.
	 * 
	 * @return the values
	 */
	public ArrayList<Value> getValues() {
		return values;
	}

	/**
	 * Return the name of the XML element of <code>Feature</code>, i. e.
	 * 'feature'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Adds a new <code>Value</code> to this <code>Feature</code>.
	 * 
	 * @param newValue
	 */
	public void addValue(Value newValue) {
		this.values.add(newValue);
	}

	/**
	 * Returns a recursively created XML representation of <code>Feature</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " domain=\"" + domain + "\" name=\""
				+ name + "\"");

		if (values.size() == 0) {
			buffer.append("/>" + newline);
		} else {

			buffer.append(">" + newline);

			for (Value currentValue : values) {
				buffer.append("\t\t\t\t" + currentValue.toString());
			}

			buffer.append("\t\t\t</" + xmltag + ">" + newline);
		}

		return buffer.toString();
	}

}
