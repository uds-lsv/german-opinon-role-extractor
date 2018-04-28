package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'history' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class History {

	private ArrayList<Action> actions;
	
	static String newline = System.getProperty("line.separator");

	static String xmltag = "history";
	
	/**
	 * Zero-argumented default constructor.
	 */
	public History() {
		super();
		actions = new ArrayList<Action>();
	}
	
	

	/**
	 * Returns the list of <code>Action</code> elements.
	 * @return the actions
	 */
	public ArrayList<Action> getActions() {
		return actions;
	}
	
	/**
	 * Adds a new <code>Action</code> to this <code>History</code>.
	 * @param newAction
	 */
	public void addAction (Action newAction) {
		this.actions.add(newAction);
	}



	/**
	 * Returns the XML element name of <code>History</code>, i. e. 'history'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}


	/**
	 * Returns a recursively created XML represenation of this <code>History</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);
		
		for (Action currentAction : actions){
			buffer.append("\t\t\t\t" + currentAction);
		}
		
		buffer.append("\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}


}
