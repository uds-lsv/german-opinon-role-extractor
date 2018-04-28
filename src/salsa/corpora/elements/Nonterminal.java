package salsa.corpora.elements;

import java.util.ArrayList;

import salsa.corpora.noelement.Id;

/**
 * Represents the 'nonterminal' section of a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Nonterminal {

	private String cat;

	private Id id;

	private ArrayList<Edge> edges;

	private Secedge secedge;

	static String xmltag = "nt";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the values of 'cat' and 'id' as arguments.
	 * 
	 * @param cat
	 * @param id
	 */
	public Nonterminal(String cat, Id id) {
		super();
		this.cat = cat;
		this.id = id;
		edges = new ArrayList<Edge>();
	}

	/**
	 * Returns the value of 'cat'.
	 * 
	 * @return the cat
	 */
	public String getCat() {
		return cat;
	}

	/**
	 * Sets the value of 'cat'.
	 * 
	 * @param cat
	 *            the cat to set
	 */
	public void setCat(String cat) {
		this.cat = cat;
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
	 * Returns the <code>Secedge</code>.
	 * 
	 * @return the secedge
	 */
	public Secedge getSecedge() {
		return secedge;
	}

	/**
	 * Sets the <code>Secedge</code>.
	 * 
	 * @param secedge
	 *            the secedge to set
	 */
	public void setSecedge(Secedge secedge) {
		this.secedge = secedge;
	}

	/**
	 * Returns the list of <code>Edge</code> elements of this
	 * <code>Nonterminal</code>.
	 * 
	 * @return the edges
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * Adds a new <code>Edge</code> to this <code>Nonterminal</code>.
	 * 
	 * @param newEdge
	 */
	public void addEdge(Edge newEdge) {
		this.edges.add(newEdge);
	}

	/**
	 * Returns the XML element name of <code>Nonterminal</code>, i. e. 'nt'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this
	 * <code>Nonterminal</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + " cat=\"" + cat + "\" id=\"" + id.getId()
				+ "\">" + newline);
		
		for (Edge currentEdge : edges) {
			buffer.append("\t\t\t\t\t\t" + currentEdge.toString());
		}
		
		buffer.append("\t\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
