import java.util.Arrays;

/**
 * SentimentUnit object contains the informations of one sentiment expression
 *
 */
public class SentimentUnit{
	String name;
	String[] source;
	String[] target;
	String typ;
	String[] collocations;

	
	/**
	 *  Constructs a new SentimentUnit
	 * @param name {@link #name} is set
	 * @param typ {@link #typ} is set
	 * @param source {@link #source} is set
	 * @param target {@link #target} is set
	 * @param collocations {@link #collocations} are set, in case typ is 'mwe' (multi-word-expression)
	 */
	public SentimentUnit(String name, String typ, String[] source, String[] target){
		if (typ.equals("mwe")){
			String[] parts = name.split("_");
			this.name = parts[parts.length-1];
			this.collocations = Arrays.copyOfRange(parts, 0, parts.length-1);
		}
		else{
			this.name = name;
			this.collocations = new String[0];
		}
		this.typ = typ;
		this.source = source;
		this.target = target;
	}
	
	/**
	 * Constructs a new SentimentUnit
	 * @param name {@link #name} is set
	 */
	public SentimentUnit(String name){
		this.name=name;

	}
	
	public SentimentUnit(String name, String typ){
		this.typ=typ;
		if (typ.equals("mwe")){
			String[] parts = name.split("_");
			this.name = parts[parts.length-1];
			this.collocations = Arrays.copyOfRange(parts, 0, parts.length-1);
		}
	}
	

	public String[]  getTargets(){
		return target;
		}
	
	public String[]  getSources(){
		return source;
		}
	
	public void setTarget(String target){
		if (this.target==null){
			this.target=new String[1];this.target[0]=target;
			}
	}
	public void setSource(String source){
		if (this.source==null){
			this.source=new String[1];this.source[0]=source;
			}
		}
	
	/**
	 * @param word
	 * @return true if the {@link #name} of the SentimentUnit equals the lemma of a given Word Object
	 */
	public boolean equals(WordObj word){
		if (this.name.equals(word.getLemma())){
			return true;
		}
		return false;
	}
	
	/**
	 * @return {@link #typ} of the SentimentUnit
	 */
	public String getTyp(){
		return this.typ;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	public String toString(){
		StringBuffer printer  = new StringBuffer();
		printer.append(this.name);
		for (String col: this.collocations){
			printer.append("_"+col);
		}
		printer.append("["+this.typ+"]");
		printer.append("[");
		for(int i=0;i<this.source.length-1;i++){
			printer.append(this.source[i]+",");
		}
		printer.append(this.source[this.source.length-1]+"]");
		printer.append("[");
		for(int j=0;j<this.target.length-1;j++){
			printer.append(this.target[j]+",");
		}
		printer.append(this.target[this.target.length-1]+"]");
		return printer.toString();		
	}
	
	/**
	 * @param cLex
	 * @return true if the SentimentUnit is valid using a given CheckLex
	 */
	public boolean check(CheckLex cLex){
		for(int i=0;i<this.source.length-1;i++){
			if (cLex.contains(this.source[i])==false){
				return false;
			}
		}
		for(int j=0;j<this.target.length-1;j++){
			if (cLex.contains(this.target[j])==false){
				return false;
			}
		}
		return true;
		
	}
	
}