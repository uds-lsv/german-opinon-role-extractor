package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'match' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Match {
	
	private String subgraph;
	
	private ArrayList<Variable> variables;

	static String xmltag = "match";

	static String newline = System.getProperty("line.separator");

	/**
	 * Default constructor that takes the subgraph as an argument
	 * @param subgraph
	 */
	public Match(String subgraph) {
		super();
		this.subgraph = subgraph;
		this.variables = new ArrayList<Variable>();
	}

	/**
	 * Returns the value of the 'subgraph' attribute.
	 * @return the subgraph
	 */
	public String getSubgraph() {
		return subgraph;
	}

	/**
	 * Sets the value of the 'subgraph' attribute.
	 * @param subgraph the subgraph to set
	 */
	public void setSubgraph(String subgraph) {
		this.subgraph = subgraph;
	}

	/**
	 * Returns the list of <code>Variable</code> elements.
	 * @return the variables
	 */
	public ArrayList<Variable> getVariables() {
		return variables;
	}

	/**
	 * Adds a new <code>Variable</code> to this <code>Match</code>.
	 * @param variable the new variable to set
	 */
	public void addVariable(Variable newVariable) {
		this.variables.add(newVariable);
	}

	/**
	 * Returns the name of the XML element of <code>Match</code>, i. e. 'match'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}
	
	/**
	 * Returns a recursively created XML representation of this <code>Match</code>.
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("<" + xmltag + " subgraph=\"" + subgraph + "\">" + newline);
		
		for (Variable currentVariable : variables) {
			buffer.append("\t\t\t\t\t" + currentVariable);
		}
		
		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);
		
		return buffer.toString();
	}
	
	

}
