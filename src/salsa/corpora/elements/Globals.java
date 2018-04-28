package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'globals' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Globals {
	
	private ArrayList<Global> globals;
	
	static String xmltag = "globals";

	static String newline = System.getProperty("line.separator");
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Globals() {
		super();
		globals = new ArrayList<Global>();
	}

	/**
	 * Returns the list of <code>Global</code> elements of this <code>Globals</code>.
	 * @return the globals
	 */
	public ArrayList<Global> getGlobals() {
		return globals;
	}
	
	/**
	 * Adds a new <code>Global</code> to this <code>Globals</code>.
	 * @param newGlobal
	 */
	public void addGlobal(Global newGlobal) {
		this.globals.add(newGlobal);
	}

	/**
	 * Returns the name of the XML element of <code>Globals</code>, i. e. 'globals'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of <code>Globals</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);

		for (Global currentGlobal : globals) {
			buffer.append("\t\t\t\t\t" + currentGlobal.toString());
		}
		
		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	
	
	


}
