import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import salsa.corpora.elements.*;
import salsa.corpora.xmlparser.*;

/**
 * @author Christine Bocionek
 * 
 * An interface to use the Salsa Java API (http://www.coli.uni-saarland.de/projects/salsa/page.php?id=software) to parse a Salsa XML corpus.
 * 
 * Uses the Salsa API classes listed below to parse the Salsa XML corpus.
 	 * @see salsa.corpora.elements.CorpusParser
	 * @see salsa.corpora.elements.Corpus
	 * @see salsa.corpora.elements.Body
	 * @see salsa.corpora.elements.Sentences
	 * @see salsa.corpora.elements.Sentence
 */
public class SalsaAPIConnective {
	
	private CorpusParser parser;
	private Corpus corpus;
	private Body body;
	private Head head;
	private ArrayList<Sentence> sentences;
	private LinkedList<Graph> graphs;
	private LinkedList<ConstituencyTree> trees;

	/**
	 * Parses a Salsa XML file which represents a corpus by:
	 * 
	 * a) creating a {@link Corpus} object from said Salsa XML file,
	 * b) extracting this {@link Corpus} object's {@link Body} (Salsa XML file minus the header),
	 * c) extracting an {@link ArrayList} of all {@link Sentence}s from the {@link Body},
	 * d) iterating over all {@link Sentence}s and extracting their {@link Graph}s,
	 * e) creating {@link ConstituencyTree}s for all {@link Graph}s,
	 * f) assigning a {@link ConstituencyTree} to the corresponding {@link SentenceObj} in the {@link SentenceList}.
	 * 
	 * @param file A {@link String} of the path of the Salsa XML corpus.
	 * @param sentenceList A {@link SentenceList} containing all sentences of the XML corpus as {@link SentenceObj}.
	 */
	public SalsaAPIConnective(String file, SentenceList sentenceList){
		
		this.graphs = new LinkedList<Graph>();
		this.trees = new LinkedList<ConstituencyTree>();

		this.parser = null;
		
		try {
			parser = new CorpusParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		this.corpus = null;

		try {
			corpus = parser.parseCorpusFromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		this.body = corpus.getBody();
		this.head = corpus.getHead();

		this.sentences = body.getSentences();
		
		// for every Sentence in the Corpus, get its Graph representation. From the Graph, create a ConstituencyTree.
		
		for (Sentence sentence: this.sentences){
			Graph graph = sentence.getGraph();
			this.graphs.add(graph);
			ConstituencyTree tree = new ConstituencyTree(graph);
			this.trees.add(tree);
			
		}
			
		// iterate over SentenceList and assign the corresponding ConstituencyTree to each SentenceObj.

		for (int j = 0; j <= sentenceList.sentenceList.size() - 1; j++){
			
			SentenceObj sent = sentenceList.sentenceList.get(j);
			ConstituencyTree constTree = this.trees.get(j);
			sent.setTree(constTree);
		}
		
		
	}
	
	/**
	 * @return The {@link Head} of the {@link Corpus}, representing the 'head' section of a Salsa XML corpus.
	 */
	public Head getHead(){
		return head;
	}

	/**
	 * @return The {@link Corpus} object representing the Salsa XML corpus.
	 */
	public Corpus getCorpus() {
		return corpus;
	}

	/**
	 * @return The {@link Body} object representing the 'body' section of a Salsa XML corpus.
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * @return An {@link ArrayList} containing all {@link Sentence} objects of the Salsa XML corpus. 
	 */
	public ArrayList<Sentence> getSentences() {
		return sentences;
	}

	/**
	 * @return A {@link LinkedList} of {@link Graph} objects which represent the tree structure of every sentence in the Salsa XML corpus.
	 */
	public LinkedList<Graph> getGraphs() {
		return graphs;
	}
	
	/**
	 * @return A {@link LinkedList} of {@link ConstituencyTree} objects which represent the tree structure of every sentence in the Salsa XML corpus and are created from the {@link Graph} objects of the Salsa Java API.
	 */
	public LinkedList<ConstituencyTree> getTrees() {
		return trees;
	}


	/**
	 * @return The {@link CorpusParser} object which parses the Salsa XML corpus.
	 */
	public CorpusParser getParser() {
		return parser;
	}

	
}
