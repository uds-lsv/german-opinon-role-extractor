package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'sem' section of a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 */
public class Semantics {

	private ArrayList<Frames> frames;

	private ArrayList<Globals> globals;

	private ArrayList<Splitwords> splitwords;

	private ArrayList<Underspecification> usps;

	private ArrayList<Wordtags> wordtags;

	static String xmltag = "sem";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Semantics() {
		super();
		frames = new ArrayList<Frames>();
		globals = new ArrayList<Globals>();
		splitwords = new ArrayList<Splitwords>();
		usps = new ArrayList<Underspecification>();
		wordtags = new ArrayList<Wordtags>();
	}

	/**
	 * Returns the list of <code>Frames</code> (this list usually contains
	 * only one element).
	 * 
	 * @return the frames
	 */
	public ArrayList<Frames> getFrames() {
		return frames;
	}

	/**
	 * Adds a new <code>Frames</code> to this <code>Semantics</code>.
	 */
	public void addFrames(Frames newFrames) {
		this.frames.add(newFrames);
	}

	/**
	 * Returns the list of <code>Globals</code> of this <code>Semantics</code>
	 * (this list usually contains only one element).
	 * 
	 * @return the globals
	 */
	public ArrayList<Globals> getGlobals() {
		return globals;
	}

	/**
	 * Adds a new <code>Globals</code> to this <code>Semantics</code>.
	 */
	public void addGlobals(Globals newGlobals) {
		this.globals.add(newGlobals);
	}

	/**
	 * Returns the list of <code>Splitwords</code> of this
	 * <code>Semantics</code>.
	 * 
	 * @return the splitwords
	 */
	public ArrayList<Splitwords> getSplitwords() {
		return splitwords;
	}

	/**
	 * Adds a new <code>Splitwords</code> to this <code>Semantics</code>.
	 */
	public void addSplitwords(Splitwords newSplitwords) {
		this.splitwords.add(newSplitwords);
	}

	/**
	 * Returns the list of <code>Underspecification</code> elements.
	 * 
	 * @return the usps
	 */
	public ArrayList<Underspecification> getUsps() {
		return usps;
	}

	/**
	 * Adds a new <code>Underspecification</code> to this
	 * <code>Semantics</code>.
	 */
	public void addUsp(Underspecification newUsp) {
		this.usps.add(newUsp);
	}

	/**
	 * Returns the list of <code>Wordtags</code>.
	 * 
	 * @return the wordtags
	 */
	public ArrayList<Wordtags> getWordtags() {
		return wordtags;
	}

	/**
	 * Adds a new <code>Wordtags</code> to this <code>Semantics</code>.
	 */
	public void addWordtags(Wordtags newWordtags) {
		this.wordtags.add(newWordtags);
	}

	/**
	 * Returns the XML element name of <code>Semantics</code>, i. e. 'sem'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Wordtags</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);
		for (Globals currentGlobals : globals) {
			buffer.append("\t\t\t\t" + currentGlobals.toString());
		}
		for (Frames currentFrames : frames) {
			buffer.append("\t\t\t\t" + currentFrames.toString());
		}

		for (Splitwords currentSplitwords : splitwords) {
			buffer.append("\t\t\t\t" + currentSplitwords.toString());
		}
		for (Underspecification currentUnderspecification : usps) {
			buffer.append("\t\t\t\t" + currentUnderspecification.toString());
		}
		for (Wordtags currentWordtags : wordtags) {
			buffer.append("\t\t\t\t" + currentWordtags.toString());
		}

		buffer.append("\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
