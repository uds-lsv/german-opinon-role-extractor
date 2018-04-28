package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'edgelabel' section of a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Edgelabel {

	private ArrayList<Value> values;

	static String xmltag = "edgelabel";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Edgelabel() {
		super();
		values = new ArrayList<Value>();
	}

	/**
	 * Returns the list of <code>Value</code> elements of this <code>Edgelabel</code>.
	 * @return the values
	 */
	public ArrayList<Value> getValues() {
		return values;
	}

	/**
	 * Returns the name of the XML element of <code>Edgelabel</code>, i. e. 'edgelabel'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Adds a new <code>Value</code> to this <code>Edgelabel</code>.
	 * @param newValue
	 */
	public void addValue(Value newValue) {
		this.values.add(newValue);
	}


	/**
	 * Returns a recursively created XML representation of this <code>Edgelabel</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);

		for (Value currentValue : values) {
			buffer.append("\t\t\t\t" + currentValue.toString());
		}
		
		buffer.append("\t\t\t" + "</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	
	
	
	
}
