package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents a 'matches' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Matches {

	ArrayList<Match> matches;
	
	static String xmltag = "matches";

	static String newline = System.getProperty("line.separator");
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Matches() {
		super(); 
		matches = new ArrayList<Match>();
	}

	/**
	 * Returns the list of <code>Match</code> elements.
	 * @return the matches
	 */
	public ArrayList<Match> getMatches() {
		return matches;
	}

	/**
	 * Adds a new <code>Match</code> to this <code>Matches</code>.
	 * @param newMatch
	 */
	public void addMatch(Match newMatch) {
		this.matches.add(newMatch);
	}
	
	/**
	 * Returns the XML element name of <code>Matches</code>, i. e. 'matches'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this <code>Matches</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);
		for (Match currentMatch : matches) {
			buffer.append("\t\t\t\t" + currentMatch);
		}
		buffer.append("\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}
	
	
}
