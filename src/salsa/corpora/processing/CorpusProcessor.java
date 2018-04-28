package salsa.corpora.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import salsa.corpora.elements.Body;
import salsa.corpora.elements.Corpus;
import salsa.corpora.elements.Edge;
import salsa.corpora.elements.Fenode;
import salsa.corpora.elements.Frame;
import salsa.corpora.elements.FrameElement;
import salsa.corpora.elements.Frames;
import salsa.corpora.elements.Graph;
import salsa.corpora.elements.Match;
import salsa.corpora.elements.Matches;
import salsa.corpora.elements.Nonterminal;
import salsa.corpora.elements.Nonterminals;
import salsa.corpora.elements.Part;
import salsa.corpora.elements.Secedge;
import salsa.corpora.elements.Semantics;
import salsa.corpora.elements.Sentence;
import salsa.corpora.elements.Splitword;
import salsa.corpora.elements.Splitwords;
import salsa.corpora.elements.Target;
import salsa.corpora.elements.Terminal;
import salsa.corpora.elements.Terminals;
import salsa.corpora.elements.Underspecification;
import salsa.corpora.elements.UnderspecificationFrameElements;
import salsa.corpora.elements.UnderspecificationFrames;
import salsa.corpora.elements.Uspblock;
import salsa.corpora.elements.Uspitem;
import salsa.corpora.elements.Variable;
import salsa.corpora.noelement.Id;

/**
 * <code>CorpusProcessor</code> provides different methods to process a
 * SalsaXML <code>Corpus</code>.
 * 
 * @author Fabian Shirokov
 * 
 */
public class CorpusProcessor {

	private Corpus corpus;
	
	private ArrayList<Terminal> allTerminalsInCorpus;
	
	private ArrayList<Nonterminal> allNonterminalsInCorpus;
	
	

	/**
	 * Default constructor that takes the <code>Corpus</code> as an argument.
	 */
	public CorpusProcessor(Corpus corpus) {
		super();
		this.corpus = corpus;
	}

	/**
	 * Returns a list of all values of 'id' and 'idref' attributes that are
	 * represented as an <code>Id</code> in the <code>Corpus</code>.
	 */
	public ArrayList<Id> getAllIds() {

		ArrayList<Id> idlist = new ArrayList<Id>();

		// iterate over the whole Corpus and add each Id to the idlist.

		Body body = corpus.getBody();

		for (Sentence sentence : body.getSentences()) {

			idlist.add(sentence.getId());

			Semantics sem = sentence.getSem();

			Graph graph = sentence.getGraph();

			Matches matches = sentence.getMatches();

			// iterate over the graph
			idlist.add(graph.getRoot());

			Terminals terminals = graph.getTerminals();

			Nonterminals nonterminals = graph.getNonterminals();

			for (Terminal terminal : terminals.getTerminals()) {
				idlist.add(terminal.getId());

				Secedge secedge = terminal.getSecedge();

				if (null != secedge) {
					Id id = secedge.getId();

					if (null != id) {
						idlist.add(id);
					}
				}
			}

			for (Nonterminal nonterminal : nonterminals.getNonterminals()) {

				idlist.add(nonterminal.getId());

				for (Edge edge : nonterminal.getEdges()) {

					idlist.add(edge.getId());
				}
			}

			// iterate over matches

			for (Match match : matches.getMatches()) {

				for (Variable variable : match.getVariables()) {

					idlist.add(variable.getId());
				}
			}

			// iterate over sem

			for (Splitwords splitwords : sem.getSplitwords()) {

				for (Splitword splitword : splitwords.getSplitwords()) {

					idlist.add(splitword.getId());

					for (Part part : splitword.getParts()) {

						idlist.add(part.getId());
					}
				}
			}

			for (Frames frames : sem.getFrames()) {

				for (Frame frame : frames.getFrames()) {

					idlist.add(frame.getId());

					Target target = frame.getTarget();

					idlist.add(target.getId());

					for (Fenode fenode : target.getFenodes()) {
						idlist.add(fenode.getIdref());
					}

					for (FrameElement fe : frame.getFes()) {

						idlist.add(fe.getId());

						for (Fenode fenode : fe.getFenodes()) {

							idlist.add(fenode.getIdref());
						}
					}
				}

				for (Underspecification usp : sem.getUsps()) {

					UnderspecificationFrames uspframes = usp.getUspframes();

					UnderspecificationFrameElements uspFrameElements = usp
							.getUspfes();

					for (Uspblock uspblockFrames : uspframes.getUspblocks()) {

						for (Uspitem uspitemFrames : uspblockFrames
								.getUspitems()) {
							idlist.add(uspitemFrames.getId());
						}
					}

					for (Uspblock uspblockFes : uspFrameElements.getUspblocks()) {

						for (Uspitem uspitemFes : uspblockFes.getUspitems()) {

							idlist.add(uspitemFes.getId());
						}
					}

				}
			}

		}

		return idlist;
	}

	/**
	 * Returns a mapping of all old sentence id's to new sentence ids. The new
	 * ids start with "s1", "s2", ...
	 */
	public HashMap<String, String> getSentenceIdMapping() {

		int currentSentenceNumber = 0;

		HashMap<String, String> idmapping = new HashMap<String, String>();

		// iterate over the whole Corpus and add each sentence Id to the idlist.

		Body body = corpus.getBody();

		for (Sentence sentence : body.getSentences()) {

			currentSentenceNumber++;

			idmapping
					.put(sentence.getId().getId(), "s" + currentSentenceNumber);
		}

		return idmapping;
	}

	/**
	 * Returns a list of all <code>Frame</code>s that stand for frame
	 * annotations.
	 * 
	 * The <code>Frame</code>s in the 'head' part of the XML file are being
	 * ignored.
	 * 
	 * @return allFrames
	 */
	public ArrayList<Frame> getAllAnnotatedFrames() {

		ArrayList<Frame> frameList = new ArrayList<Frame>();

		// iterate over the whole Corpus and add each frame to the frameList.

		Body body = corpus.getBody();

		for (Sentence sentence : body.getSentences()) {

			Semantics sem = sentence.getSem();

			for (Frames frames : sem.getFrames()) {

				for (Frame frame : frames.getFrames()) {

					frameList.add(frame);

				}
			}

		}

		return frameList;
	}

	/**
	 * Returns a list of <code>Terminal</code> elements that correspond to the
	 * given set of <code>Fenode</code> elements. For example, if a frame or
	 * frame element's annotation (-> one or more <code>Fenode</code>s)
	 * covers a complex node (e. g. an NP or VP node), then this method resolves
	 * all <code>Terminal</code> elements that belong to this NP or VP node.
	 * 
	 * @param allFenodes
	 * @return allTerminals
	 */
	public Set<Terminal> getAllTerminals(ArrayList<Fenode> allFenodes) {


		Set<Terminal> allTerminals = new HashSet<Terminal>();

		// gehe durch das Corpus und fuege alle entsprechenden Terminals zu
		// 'allTerminals' hinzu

		Set<String> unresolvedIds = new HashSet<String>();

		//System.out.print("fe-nodes ");
		for (Fenode fenode : allFenodes) {
			unresolvedIds.add(fenode.getIdref().getId());
			//System.out.print(fenode.getIdref().getId() + ",");
		}
		//System.out.println(" are initially unresolved");

		
		while (!unresolvedIds.isEmpty()) {
			
			// versuche zuerst, die Terminals zu resolven
			for (Terminal currentTerminal : getAllTerminalsInCorpus()) {
				
				HashSet<String> idsToRemove = new HashSet<String>();

				for (String unresolvedId : unresolvedIds) {
					
					

					if (currentTerminal.getId().getId().equals(unresolvedId)) {

						allTerminals.add(currentTerminal);

						idsToRemove.add(unresolvedId);
						//System.out.println(unresolvedId
						//		+ " has now been resolved to terminal node.");
					}
				}
				for (String id : idsToRemove) {
					unresolvedIds.remove(id);
				}
			}

			// wenn dadurch immernoch nicht alles resolvt wurde, durchsuche die
			// passenden Nonterminals
			if (!unresolvedIds.isEmpty()) {

				for (Nonterminal currentNonterminal : getAllNonterminalsInCorpus()) {
					
					HashSet<String> idsToRemove = new HashSet<String>();
					
					HashSet<String> idsToAdd = new HashSet<String>();

					for (String unresolvedId : unresolvedIds) {

						if (currentNonterminal.getId().getId().equals(
								unresolvedId)) {

							for (Edge edge : currentNonterminal.getEdges()) {

								idsToAdd.add(edge.getId().getId());
								

							}
							idsToRemove.add(unresolvedId);
							
						}
					}
					for (String id : idsToRemove) {
						
						unresolvedIds.remove(id);
					}
					for (String id : idsToAdd) {
						unresolvedIds.add(id);
					}
				}
			}
		}


		return allTerminals;
	}

	/**
	 * Returns a list of all <code>Terminal</code> elements that are contained
	 * in any <code>Sentence</code> in the <code>Corpus</code>.
	 * 
	 * @return
	 */
	public ArrayList<Terminal> getAllTerminalsInCorpus() {
		
		if (null != allTerminalsInCorpus) {
			return allTerminalsInCorpus;
		}

		//System.out.println("method getAllTerminalsInCorpus() has been called.");

		ArrayList<Terminal> allTerminals = new ArrayList<Terminal>();

		Body body = corpus.getBody();

		for (Sentence sentence : body.getSentences()) {

			Graph graph = sentence.getGraph();
			
			Semantics sem = sentence.getSem();
			
			ArrayList<Splitwords> allSplitwords = sem.getSplitwords();
			
			for (Splitwords splitwords : allSplitwords) {
				
				for (Splitword splitword : splitwords.getSplitwords()) {
					
					for (Part part : splitword.getParts()) {
						
						allTerminals.add(new Terminal(part.getId(), "unknown", "unknown", "unknown", "unknown"));
					}
				}
			}

			Terminals terminals = graph.getTerminals();

			for (Terminal currentTerminal : terminals.getTerminals()) {

				allTerminals.add(currentTerminal);
			}
		}
		return allTerminals;
	}

	/**
	 * Returns a list of all <code>Nonterminal</code> elements that are
	 * contained in any <code>Sentence</code> in the <code>Corpus</code>.
	 * 
	 * @return
	 */
	public ArrayList<Nonterminal> getAllNonterminalsInCorpus() {
		
		if (null != allNonterminalsInCorpus) {
			return allNonterminalsInCorpus;
		}

		ArrayList<Nonterminal> allNonterminals = new ArrayList<Nonterminal>();

		Body body = corpus.getBody();

		for (Sentence sentence : body.getSentences()) {

			Graph graph = sentence.getGraph();

			Nonterminals nonterminals = graph.getNonterminals();

			for (Nonterminal currentNonterminal : nonterminals
					.getNonterminals()) {

				allNonterminals.add(currentNonterminal);
			}
		}
		return allNonterminals;
	}
}
