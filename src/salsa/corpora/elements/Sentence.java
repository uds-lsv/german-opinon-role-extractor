package salsa.corpora.elements;

import salsa.corpora.noelement.Id;

/**
 * Represents the 's' section in a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Sentence {

	private Id id;

	private String source;

	private Graph graph;

	private Semantics sem;

	private Matches matches;

	static String xmltag = "s";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the sentence 'id' as an argument.
	 * 
	 * @param id
	 */
	public Sentence(Id id) {
		super();
		this.id = id;
	}

	/**
	 * Returns the <code>Id</code>.
	 * 
	 * @return the id
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Sets the <code>Id</code>.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * Returns the value of 'source'.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the value of 'source'.
	 * 
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the <code>Graph</code>.
	 * 
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Sets the <code>Graph</code>.
	 * 
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Returns the <code>Semantics</code>.
	 * 
	 * @return the sem
	 */
	public Semantics getSem() {
		return sem;
	}

	/**
	 * Sets the <code>Semantics</code>.
	 * 
	 * @param sem
	 *            the sem to set
	 */
	public void setSem(Semantics sem) {
		this.sem = sem;
	}

	/**
	 * Returns the <code>Matches</code>.
	 * 
	 * @return the matches
	 */
	public Matches getMatches() {
		return matches;
	}

	/**
	 * Sets the <code>Matches</code>.
	 * 
	 * @param matches
	 *            the matches to set
	 */
	public void setMatches(Matches matches) {
		this.matches = matches;
	}

	/**
	 * Returns the XML element name of <code>Sentece</code>, i. e. 's'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Sentence</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " id=\"" + id.getId() + "\"");
		if (null != source) {
			buffer.append(" source=\"" + source);
		}
		buffer.append(">" + newline);

		if (null != graph) {
			buffer.append("\t\t\t" + graph.toString());
		}
		if (null != matches) {
			buffer.append("\t\t\t" + matches.toString());
		}
		if (null != sem) {
			buffer.append("\t\t\t" + sem.toString());
		}
		

		buffer.append("\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
