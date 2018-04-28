import java.util.LinkedList;
import salsa.corpora.noelement.*;

/**
 * A Sentence Object contains all information for one sentence
 *
 */
public class SentenceObj{
	
	String sentence;
	LinkedList<WordObj> wordList = new LinkedList<WordObj>();
	DependencyGraph graph;
	ConstituencyTree tree;
	Id id;
	NamedEntityList namedEntityList;
	private DependencyGraph rawGraph;
	boolean sourceIsAuthor=false;
	
    //added by isha 09.06
    public LinkedList<WordObj> getWordList(){return wordList;}
    public void setSourceIsAuthor(boolean b){sourceIsAuthor=b;}
    public boolean getSourceIsAuthor(){return sourceIsAuthor;}
	/**
	 * Constructs a new Sentence Object for a given sentence.
	 * @param sentence {@link #sentence} is set
	 */
	public SentenceObj(String sentence){
		
		this.sentence = sentence;
		// satz = satz.replace(".", "");
		// satz = satz.replace(",", "");
		// better not strip any punctuation, as it is part of the parses. Numbers of indices when matching parses and sentence objects will not match up otherwise.
		String[] words = sentence.split(" ");
		
		for (int i=0; i < words.length; i++)
			
		{
			if (words[i].length() > 0)
			{
				WordObj word = new WordObj(words[i]);
				word.setPosition(i+1);
				wordList.add(word);
			}
		}
	}
	
	/**
	 * @param word
	 * @return Position of a Word Object {@link #wordList} in the sentence or -1 if the sentence doesn't contain the Word Object
	 */
	public int getWordPosition(WordObj word){
		for (int i=0;i<this.wordList.size();i++){
			if (this.wordList.get(i).equals(word)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @return DependencyGraph {@link #graph} of the sentence
	 */
	public DependencyGraph getGraph() {
		return graph;
	}

	/**
	 * @param graph {@link #graph} to set
	 */
	public void setGraph(DependencyGraph graph) {
		this.graph = graph;
	}
	
	/**
	 * @return ConstituencyTree {@link #tree} of the sentence
	 */
	public ConstituencyTree getTree() {
		return tree;
	}

	/**
	 * @param tree {@link #tree} to set
	 */
	public void setTree(ConstituencyTree tree) {
		this.tree = tree;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String printer = this.sentence;
		return printer;
	}
	
	/**
	 * @return {@link #namedEntityList} of the sentence
	 */
	public NamedEntityList getNamedEntityList() {
		return namedEntityList;
	}

	/**
	 * @param namedEntityList {@link #namedEntityList} to set
	 */
	public void setNamedEntityList(NamedEntityList namedEntityList) {
		this.namedEntityList = namedEntityList;
	}


	public void setRawGraph(DependencyGraph rawGraph) {
		this.rawGraph = rawGraph;
	}

	/**
	 * @return unnormalized {@link DependencyGraph} of the sentence
	 */
	public DependencyGraph getRawGraph() {
		return rawGraph;
	}
}