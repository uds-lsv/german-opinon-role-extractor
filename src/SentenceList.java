import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import salsa.corpora.noelement.*;

/**
 * @author Katja König (conjunction normalization)
 *
 * An instance of {@link SentenceList} contains a {@link LinkedList} of {@link SentenceObj} and represents a text corpus.
 */
public class SentenceList{
  
	LinkedList<SentenceObj> sentenceList = new LinkedList<SentenceObj>();
	//LinkedList<NamedEntityList> namedEntityLists = new LinkedList<NamedEntityList>();
	
	public 	LinkedList<SentenceObj> getSentenceList(){return sentenceList;}
	
	/**
	 * Read in a raw text file and split file into {@link SentenceObj}s at line breaks.
	 * @param filename A {@link String} of the raw text file's path.
	 */
	public void rawToSentenceList(String filename){
        Scanner scanner;
        int idcount = 0;
        try {
            scanner = new Scanner(new File(filename), "UTF-8");
            scanner.useLocale(Locale.GERMANY);
            String line;
            while (scanner.hasNext()){
            	line = scanner.nextLine();
            	idcount++;
            	SentenceObj sentence = new SentenceObj(line);
            	Integer tmp = new Integer(idcount);
            	sentence.id =  new Id(tmp.toString());
            	
            	this.sentenceList.add(sentence);
            }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
	}
	
	/**
	 * Read in a ParZu dependency parse file.
	 * Save the following information for all {@link WordObj}s of a {@link SentenceObj}:
	 * - part of speech tag
	 * - lemma
	 * - label of incoming dependency edge
	 * - index of the head of the incoming dependency edge.
	 * Given this information, a {@link DependencyGraph} can be constructed for each sentence.
	 * @param filename A {@link String} of the dependency parse file's path which will be read in.
	 */
	public void readDependencyParse(String filename){
		
		Scanner scanner;
		
		try {
			
			scanner = new Scanner(new File(filename), "UTF-8");
			scanner.useLocale(Locale.GERMANY);
            String line;
            
            int sentCounter = 0;
            int wordCounter = 0;
            
    		SentenceObj sentence = sentenceList.get(sentCounter);

            while (scanner.hasNext()){
            	
            	line = scanner.nextLine();
            	
            	if (!line.isEmpty()) {
           
            		
            		String[] splitResult = line.split("\t");             		
            		
            		WordObj word = sentence.wordList.get(wordCounter);
            		
            		
            		
            		String lemma = splitResult[2];
            		String pos = splitResult[4];
            		String stringEdge = splitResult[6];
            		int edge = Integer.parseInt(stringEdge);
            		String relation = splitResult[7];

            		
            		word.setLemma(lemma);
            		word.setPos(pos);
            		word.setEdge(edge);
            		word.setRelation(relation);
            		
            		wordCounter++;
            		            		
            	}
            	
            	else {
            		
            		DependencyGraph graph = new DependencyGraph(sentence.wordList);
            		sentence.setGraph(graph);
					sentence.setRawGraph(graph);
            		//add node for every wordobj of the sentence
            		for (WordObj wordobj: sentence.wordList){
            			graph.addNode(wordobj);
            		}
            		for (WordObj target: sentence.wordList){
            			/*wordobj is already target
            		    find out source by identifying the origin of the target's incoming edge*/
            			int sourceNum = target.getEdge();
            			//check if root or a "normal" node is source 
            			if (sourceNum == 0){
            				WordObj source = graph.getRoot();
            				graph.addEdge(source, target, target.getRelation());
            			}
            			else{
            				WordObj source = sentence.wordList.get(sourceNum-1);
            				graph.addEdge(source, target, target.getRelation());
                    		
            			}
            		}
            		
              		wordCounter = 0;
                    sentCounter++;
            		sentence = sentenceList.get(sentCounter);

                    
            	}
            	
            }
            
            //create a dependency graph for the last sentence.
            DependencyGraph graph = new DependencyGraph(sentence.wordList);
    		sentence.setGraph(graph);
			sentence.setRawGraph(graph);
    		//add node for every wordobj of the sentence
    		for (WordObj wordobj: sentence.wordList){
    			graph.addNode(wordobj);
    		}
    		for (WordObj target: sentence.wordList){
    			/*wordobj is already target
    		    find out source by identifying the origin of the target's incoming edge*/
    			int sourceNum = target.getEdge();
    			//check if root or a "normal" node is source 
    			if (sourceNum == 0){
    				WordObj source = graph.getRoot();
    				graph.addEdge(source, target, target.getRelation());
    			}
    			else{
    				WordObj source = sentence.wordList.get(sourceNum-1);
    				graph.addEdge(source, target, target.getRelation());
          
		}
    		}
		}
        catch (FileNotFoundException e) {
                e.printStackTrace();
           }
		
	}

	
	/**
	 * Normalize all {@link DependencyGraph} objects. This involves the
	 * following steps:
	 *
	 * - general normalization
	 * - normalization for active and passive voice
	 * - normalization of conjunctions
	 */
	public void normalizeDependencyGraphs(){
		
		for (int j = 0; j <= this.sentenceList.size() - 1; j++){

			DependencyGraph graph = this.sentenceList.get(j).getGraph();

			SentenceObj sentence = this.sentenceList.get(j);
			DependencyGraph normalized = graph.normalize(graph);
			DependencyGraph normalizedready = graph.normalizeActivePassive(normalized);
			DependencyGraph normalizedcon = graph.normalizeConjunctions(normalizedready);

			sentence.setGraph(normalizedcon);
		}
	}
		
	
	public String toString(){
		
		String printer  = "";
		
		for (int i=0; i < this.sentenceList.size(); i++){
			
			printer = printer+"\n";
			printer = printer+this.sentenceList.get(i);
		}
		return printer;
	}
}
