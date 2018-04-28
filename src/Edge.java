/**
 * 
 * The class Edge is supposed to represent a datatype in which the edges of a graph object should be saved in the 
 * corresponding HashSet edges from the DependencyGraph class. An edge always consists of a WordObj source as the edge's 
 * starting point, a WordObj target as the edge's ending point and a String depRel which marks the dependency label
 * which exists between those two nodes.
 *
 */
public class Edge {
	
	WordObj source;
	WordObj target;
	String depRel;
	
	public Edge (WordObj source, WordObj target, String deprel){
		this.source = source;
		this.target = target;
		this.depRel = deprel;
		
	}
	
	public WordObj getTarget(){
		return target;
	}
public String toString(){
		
		StringBuffer printer = new StringBuffer();
		printer.append(this.source);
		printer.append("\t" + this.target);
		printer.append("\t" + this.depRel);
		return printer.toString();	

	}
}