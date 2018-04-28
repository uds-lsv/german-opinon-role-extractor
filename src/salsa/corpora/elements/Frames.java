package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'frames' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Frames {

	private String xmlns;

	private ArrayList<Frame> frames;

	static String xmltag = "frames";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Frames() {
		super();
		frames = new ArrayList<Frame>();
	}

	/**
	 * This constructor gets the value of the 'xmlns' (XML namespace) value as
	 * an argument.
	 * 
	 * @param xmlns
	 */
	public Frames(String xmlns) {
		super();
		this.xmlns = xmlns;
		frames = new ArrayList<Frame>();
	}

	/**
	 * Returns the value of the 'xmlns' (XML namespace) attribute, e. g. 
	 * 'http://www.clt-st.de/framenet/frame-database'.
	 * @return the xmlns
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * Sets the value of the xmlns attribute.
	 * @param xmlns the xmlns to set
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * Returns the list of <code>Frame</code> elements of this <code>Frames</code>.
	 * @return the frames
	 */
	public ArrayList<Frame> getFrames() {
		return frames;
	}
	
	/**
	 * Adds a new <code>Frame</code> to this <code>Frames</code>.
	 * @param newFrame
	 */
	public void addFrame(Frame newFrame) {
		this.frames.add(newFrame);
	}

	/**
	 * Returns the name of the XML element of <code>Frames</code>, i. e. 'frames'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	

	/**
	 * Returns a recursively created XML representation of <code>Frames</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag);
		
		if (null != xmlns) {
			buffer.append(" xmlns=\"" + xmlns + "\"");
		}
		
		buffer.append(">" + newline);
		
		for (Frame currentFrame : frames) {
			buffer.append("\t\t\t" + currentFrame.toString());
		}
		
		buffer.append("\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

	
	

}
