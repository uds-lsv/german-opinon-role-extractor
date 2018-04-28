import java.util.LinkedList;

/**
 * WordObj object contains all information about a specific word
 *
 */
public class WordObj{
	
	private String name;
	private String pos;
	private String lemma;
	private String relation;
	private boolean isParticleVerb;
	private WordObj particle;
	private int edge; 
	private LinkedList<WordObj> deletedWords = new LinkedList<WordObj> ();
	private int position;
	//added by isha 9.06
	private boolean markedAsSource=false;
	private boolean markedAsTarget=false;
	
	/**
	 * Constructs a new WordObj and set {@link #name}
	 * @param name
	 */
	public WordObj(String name){
		this.name = name;
	}
	//added by isha 9.06
	public void setMarkedAsTarget(boolean bool){markedAsTarget=bool;}
	public void setMarkedAsSource(boolean bool){markedAsSource=bool;}
	public boolean getMarkedAsTarget(){return markedAsTarget;}
	public boolean getMarkedAsSource(){return markedAsSource;}
	
	
	/**
	 * @param position {@link #position} is set
	 */
	public void setPosition(int position){
		this.position = position;
	}
	
	/**
	 * @return {@link #position} of WordObj
	 */
	public int getPosition(){
		return this.position;
	}
	
	/**
	 * @return {@link #name} of WordObj
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * @return {@link #pos} of WordObj
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * @param pos {@link #pos} is set
	 */
	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * @return {@link #lemma} of WordObj
	 */
	public String getLemma() {
		return lemma;
	}
	
	/**
	 * @param lemma {@link #lemma} is set
	 */
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	/**
	 * @return {@link #relation} of WordObj
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 * @param relation {@link #relation} is set
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}

	/**
	 * @return {@link #isParticleVerb} for WordObj
	 */
	public boolean getIsParticleVerb() {
		return isParticleVerb;
	}

	/**
	 * @param isParticleVerb {@link #isParticleVerb} is set
	 */
	public void setIsParticleVerb(boolean isParticleVerb) {
		this.isParticleVerb = isParticleVerb;
	}
	
	/**
	 * @return {@link #particle} of WordObj
	 */
	public WordObj getParticle() {
		return particle;
	}

	/**
	 * @param particle {@link #particle} is set
	 */
	public void setParticle(WordObj particle) {
		this.particle = particle;
	}

	/**
	 * @return {@link #edge} of Wordobj
	 */
	public int getEdge() {
		return edge;
	}

	/**
	 * @param edge {@link #edge} is set
	 */
	public void setEdge(int edge) {
		this.edge = edge;
	}
	
	/**
	 * @param deleted is added to {@link #deletedWords}
	 */
	public void addDeleted(WordObj deleted){
		deletedWords.add(deleted);
	}
	
	/**
	 * @return {@link #deletedWords} of WordObj
	 */
	public LinkedList<WordObj> getDeleted(){
		return deletedWords;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		StringBuffer printer = new StringBuffer();
		printer.append(this.name);
		//printer.append("\t" + this.lemma);
		//printer.append("\t" + this.constituent);
		return printer.toString();
		
	}
}