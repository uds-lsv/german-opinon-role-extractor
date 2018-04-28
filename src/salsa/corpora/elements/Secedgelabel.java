package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'secedgelabel' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Secedgelabel {
	
	private ArrayList<Value> values;
	
	static String xmltag = "secedgelabel";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Secedgelabel() {
		super();
		values = new ArrayList<Value>();
	}

	/**
	 * Returns the list of <code>Value</code> elements.
	 * @return the values
	 */
	public ArrayList<Value> getValues() {
		return values;
	}
	
	/**
	 * Adds a new <code>Value</code> to this <code>Secedgelabel</code>.
	 */
	public void addValue(Value newValue){
		this.values.add(newValue);
	}

	/**
	 * Returns the XML element name of <code>Secedgelabel</code>, i. e. 'secedgelabel'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this <code>Secedgelabel</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);
		
		for (Value currentValue : values){
			buffer.append("\t\t\t\t" + currentValue.toString());
		}
		
		buffer.append("\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	

}
