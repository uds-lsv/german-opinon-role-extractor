package salsa.corpora.noelement;

/**
 * This class represents an 'id' or 'idref' in the salsa corpora. It overwrites
 * the equals() and the getHash() method so that two Id objects are the same
 * if they have the same id. 
 * @author Fabian Shirokov
 *
 */
public class Id {
	
	private String id;
	
	private boolean isTerminal;
	
	/**
	 * Default constructor that takes the id as an argument.
	 * @param id
	 */
	public Id(String id) {
		this.id = id;
	}
	
	/**
	 * Constructor that takes the id and isTerminal as an argument.
	 * Set <code>isTerminal</code> to <code>false</code> if this <code>Id</code> 
	 * belongs to a nonterminal in the corpus.
	 * @param id
	 */
	public Id(String id, boolean isTerminal) {
		this.id = id;
		this.isTerminal = isTerminal;
	}
	
	/**
	 * Returns the id.
	 * @return
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * Sets the id.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns <code>true</code> if this <code>Id</code> belongs to a terminal
	 * node in the given corpus.
	 * @return
	 */
	public boolean getIsTerminal() {
		return this.isTerminal;
	}
	
	/**
	 * Set <code>isTerminal</code> to <code>true</code> if this <code>Id</code>
	 * belongs to a terminal node in the given corpus.
	 * @param isTerminal
	 */
	public void setIsTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}
	

}
