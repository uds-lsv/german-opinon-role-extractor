package salsa.corpora.elements;

import java.util.ArrayList;

public class Flags {
	
	private ArrayList<Flag> flags;

	static String newline = System.getProperty("line.separator");

	static String xmltag = "flags";
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Flags(){
		super();
		this.flags = new ArrayList<Flag>();
	}
	
	

	/**
	 * Returns the <code>Flag</code> elements of this <code>Flags</code>.
	 * @return the flags
	 */
	public ArrayList<Flag> getFlags() {
		return flags;
	}
	
	/**
	 * Adds a new <code>Flag</code> to this <code>Flags</code>.
	 * @param newFlag
	 */
	public void addFlag(Flag newFlag) {
		this.flags.add(newFlag);
	}



	/**
	 * Returns the name of the XML element of <code>Flags</code>, i. e. 'flags'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}


	
	/**
	 * Returns a recursively created XML representation of this <code>Flags</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);
		
		for (Flag currentFlag : flags) {
			buffer.append("\t\t\t" + currentFlag.toString());
		}
		
		buffer.append("\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}


}
