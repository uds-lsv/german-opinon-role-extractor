package salsa.corpora.elements;

import java.util.ArrayList;

import salsa.corpora.noelement.Id;

/**
 * Represents a 'frame' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Frame {

	private Id id;

	private String name;

	private String source;

	private String usp;

	private ArrayList<Element> elements;

	private Target target;

	private ArrayList<FrameElement> fes;

	private ArrayList<Flag> flags;

	static String xmltag = "frame";

	static String newline = System.getProperty("line.separator");

	/**
	 * This constructor is used for frames in the 'head' section. It takes the
	 * name of the frame as an argument.
	 * 
	 * @param name
	 */
	public Frame(String name) {
		super();
		this.name = name;
		this.elements = new ArrayList<Element>();
		this.fes = new ArrayList<FrameElement>();
		this.flags = new ArrayList<Flag>();
	}

	/**
	 * This constructor is used for frames in the 'body' section. It takes the
	 * name of the frame and the <code>Id</code> as an argument.
	 * 
	 * @param name
	 */
	public Frame(String name, Id id) {
		super();
		this.name = name;
		this.id = id;
		this.elements = new ArrayList<Element>();
		this.fes = new ArrayList<FrameElement>();
		this.flags = new ArrayList<Flag>();
	}

	/**
	 * Returns the <code>Id</code>.
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code>.
	 * @param id the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the name of the this <code>Frame</code>, e. g. 'Weather'.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this frame.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the 'source' attribute, e. g. '1'.
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the value of the 'source' attribute.
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the <code>Target</code> of this <code>Frame</code>.
	 * @return the target
	 */
	public Target getTarget() {
		return target;
	}

	/**
	 * Sets the <code>Target</code>.
	 * @param target the target to set
	 */
	public void setTarget(Target target) {
		this.target = target;
	}

	/**
	 * Returns the list of the <code>Element</code> elements of this <code>Frame</code>.
	 * @return the elements
	 */
	public ArrayList<Element> getElements() {
		return elements;
	}
	
	/**
	 * Adds a <code>Element</code> to this <code>Frame</code>.
	 */
	public void addElement(Element newElement){
		this.elements.add(newElement);
	}

	/**
	 * Returns the list of the <code>FrameElement</code> elements of this <code>Frame</code>.
	 * @return the fes
	 */
	public ArrayList<FrameElement> getFes() {
		return fes;
	}
	
	/**
	 * Adds a new <code>FrameElement</code> to this <code>Frame</code>.
	 */
	public void addFe(FrameElement newFe){
		this.fes.add(newFe);
	}

	/**
	 * Returns the list of the <code>Flag</code> elements of this <code>Frame</code>.
	 * @return the flags
	 */
	public ArrayList<Flag> getFlags() {
		return flags;
	}
	
	/**
	 * Adds a new <code>Flag</code> to this <code>Frame</code>.
	 */
	public void addFlag(Flag newFlag){
		this.flags.add(newFlag);
	}
	
	/**
	 * Returns the value of the 'usp' attribute.
	 * @return
	 */
	public String getUsp() {
		return this.usp;
	}
	
	/**
	 * Sets the value of the 'usp' attribute.
	 * @param usp
	 */
	public void setUsp(String usp){
		this.usp = usp;
	}

	/**
	 * Returns the name of the XML element of <code>Frame</code>, i. e. 'frame'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this <code>Frame</code>.
	 */
	public String toString() {
		
		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag);

		buffer.append(" name=\"" + name + "\"");
		
		if (null != id) {
			buffer.append(" id=\"" + id.getId() + "\"");
		}
		
		if (null != source) {
			buffer.append(" source=\"" + source + "\"");
		}
		
		if (null != usp) {
			buffer.append(" usp=\"" + usp + "\"");
		}
		
		buffer.append(">" + newline);
		
		for (Element currentElement : elements) {
			buffer.append("\t\t\t\t" + currentElement.toString());
		}
		
		if (null != target) {
			buffer.append("\t\t\t\t\t\t" + target.toString());
		}
		
		for (FrameElement currentFe : fes) {
			buffer.append("\t\t\t\t" + currentFe.toString());
		}
		
		for (Flag currentFlag : flags) {
			buffer.append("\t\t\t\t" + currentFlag.toString());
		}		
		
		buffer.append("\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}
	
	

}
