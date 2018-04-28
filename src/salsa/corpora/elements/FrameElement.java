package salsa.corpora.elements;

import java.util.ArrayList;

import salsa.corpora.noelement.Id;

/**
 * Represents a frame element 'fe' in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class FrameElement {

	private Id id;

	private String name;

	private String source;

	private String usp;

	private ArrayList<Fenode> fenodes;

	private ArrayList<Flag> flags;

	static String xmltag = "fe";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the <code>Id</code> and the name of the
	 * <code>FrameElement</code> as arguments.
	 * 
	 * @param id
	 * @param name
	 */
	public FrameElement(Id id, String name) {
		super();
		this.id = id;
		this.name = name;
		fenodes = new ArrayList<Fenode>();
		flags = new ArrayList<Flag>();
	}

	/**
	 * Constructor that takes the <code>Id</code>, the name and the 'usp'
	 * value of the <code>FrameElement</code> as arguments.
	 * 
	 * @param id
	 * @param name
	 * @param usp
	 */
	public FrameElement(Id id, String name, String usp) {
		super();
		this.id = id;
		this.name = name;
		this.usp = usp;
		fenodes = new ArrayList<Fenode>();
		flags = new ArrayList<Flag>();
	}

	/**
	 * Returns the <code>Id</code> of this <code>FrameElement</code>.
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code> of this <code>FrameElement</code>.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the name of this <code>FrameElement</code>, e. g. 'Agent'.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this <code>FrameElement</code>.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the source of this <code>FrameElement</code>.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source of this <code>FrameElement</code>.
	 * 
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the 'usp' value of this <code>FrameElement</code>, e. g.
	 * 'yes'.
	 * 
	 * @return the usp
	 */
	public String getUsp() {
		return usp;
	}

	/**
	 * Sets the 'usp' value of this <code>FrameElement</code>.
	 * 
	 * @param usp
	 *            the usp to set
	 */
	public void setUsp(String usp) {
		this.usp = usp;
	}

	/**
	 * Returns the list of <code>Fenode</code> elements of this
	 * <code>FrameElement</code>.
	 * 
	 * @return the fenodes
	 */
	public ArrayList<Fenode> getFenodes() {
		return fenodes;
	}
	
	/**
	 * Adds a new <code>Fenode</code> to this <code>FrameElement</code>.
	 */
	public void addFenode(Fenode newFenode){
		this.fenodes.add(newFenode);
	}

	/**
	 * Returns the list of <code>Flag</code> elements of this
	 * <code>FrameElement</code>.
	 * 
	 * @return the flags
	 */
	public ArrayList<Flag> getFlags() {
		return flags;
	}
	
	/**
	 * Adds a new <code>Flag</code> to this <code>FrameElement</code>.
	 */
	public void addFlag(Flag newFlag){
		this.flags.add(newFlag);
	}

	/**
	 * Returns the name of the XML element of <code>FrameElement</code>, i.
	 * e. 'fe'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of
	 * <code>FrameElement</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " id=\"" + id.getId() + "\" name=\""
				+ name + "\"");

		if (null != source) {
			buffer.append(" source=\"" + source + "\"");
		}
		if (null != usp) {
			buffer.append(" usp=\"" + usp + "\"");
		}
		
		buffer.append(">" + newline);

		for (Fenode currentFenode : fenodes) {
			buffer.append("\t\t\t\t\t\t\t" + currentFenode.toString());
		}

		for (Flag currentFlag : flags) {
			buffer.append("\t\t\t\t\t\t\t" + currentFlag.toString());
		}

		buffer.append("\t\t\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
