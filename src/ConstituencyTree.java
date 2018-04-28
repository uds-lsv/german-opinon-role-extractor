import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import salsa.corpora.elements.Graph;
import salsa.corpora.elements.Nonterminal;
import salsa.corpora.elements.Terminal;
import salsa.corpora.noelement.Id;

/**
 * @author Christine Bocionek (initial version)
 * @author Katja König (computeTerminalCandidates, adjustments to getArgumentNode for conjunction normalization)
 *
 * An instance of the class {@link ConstituencyTree} represents the constituent structure of a sentence.
 * It consists of a {@link Nonterminal} root node, {@link Nonterminal} and {@link Terminal} nodes and a {@link HashMap} transitions where a parent node is a key and the value is a list of child nodes (representing the parent-child relationship between nodes in a tree).
 * The list of child nodes is organized such that the leftmost child node is the first item in the list and the rightmost child node is the last.
 * An instance of {@link ConstituencyTree} is built from an instance of the Salsa Java API class {@link Graph}.
 * 
 * Uses the Salsa Java API classes listed below (http://www.coli.uni-saarland.de/projects/salsa/page.php?id=software) to build the tree:
	 * @see salsa.corpora.elements.Graph
	 * @see salsa.corpora.elements.Nonterminal
	 * @see salsa.corpora.elements.Terminal
	 * @see salsa.corpora.elements.Edge
	 * @see salsa.corpora.noelement.Id
 */
public class ConstituencyTree {

	private Graph graph;
	private Nonterminal root;
	private ArrayList<Terminal> terminals;
	private ArrayList<Nonterminal> nonterminals;
	private HashMap<Nonterminal, ArrayList<Object>> transitions;
	
	/**
	 * Given a {@link Graph} object from the Salsa API representing the constituency structure of a sentence, extract its hierarchy to create a tree structure.
	 * The extraction is performed by the method {@link #extractConstituencyTree(Graph graph)}.
	 *
	 * @param graph The {@link Graph} object from which the tree is constructed.
	 */
	public ConstituencyTree(Graph graph){
		
		this.transitions = new HashMap<Nonterminal, ArrayList<Object>>();
		extractConstituencyTree(graph);
		
		}

	public static Id getNodeId(Object node) {
			if (!(node instanceof Terminal || node instanceof Nonterminal)) {
				throw new IllegalArgumentException();
			}

			if (node instanceof Terminal) {
				return ((Terminal) node).getId();
			} else {
				return ((Nonterminal) node).getId();
			}
		}

	/**
	 * @return The {@link Graph} object from which the {@link ConstituencyTree} was created.
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * @return The {@link Nonterminal} root node of the {@link ConstituencyTree}. 
	 */
	public Nonterminal getRoot() {
		return root;
	}

	/**
	 * The root of most parse trees is a node with category "PSEUDO". This method tries to find the node that actually
	 * represents the parsed sentence and usually has the category "S".
	 * @return The true root node if it can be detected. Else, getRoot()
	 */
	public Nonterminal getTrueRoot() {
		final Object potentialTrueRoot = getChildren(getRoot()).get(0);
		if (getRoot().getCat().equals("PSEUDO")
				&& getChildren(getRoot()).size() == 2
				&& potentialTrueRoot instanceof Nonterminal) {
			return (Nonterminal) potentialTrueRoot;
		} else {
			return getRoot();
		}
	}
	
	/**
	 * @return An {@link ArrayList} of all {@link Terminal} nodes of the {@link ConstituencyTree}. 
	 */
	public ArrayList<Terminal> getTerminals() {
		return terminals;
	}

	/**
	 * @return An {@link ArrayList} of all {@link Nonterminal} nodes of the {@link ConstituencyTree}. 
	 */
	public ArrayList<Nonterminal> getNonterminals() {
		return nonterminals;
	}
	
	/**
	 * @return A {@link HashMap} representing the edges between nodes in the {@link ConstituencyTree}.
	 * A key is a {@link Nonterminal} parent node and its value is an {@link ArrayList} of child nodes.
	 * A child node can be either a {@link Nonterminal} or a {@link Terminal} node. 
	 */
	public HashMap<Nonterminal, ArrayList<Object>> getTransitions() {
		return transitions;
	}
	
	/**
	 * Checks whether a given {@link Nonterminal} node has child nodes.
	 * @param node A {@link Nonterminal} node of the {@link ConstituencyTree}.
	 * @return "True" if the given {@link Nonterminal} node has child nodes, "False" if it does not.
	 */
	public boolean hasChildren(Nonterminal node){
		
		if (this.transitions.get(node) != null){
			return true;
		}
		else return false;
	}
	
	/**
	 * @param node A {@link Nonterminal} node of the {@link ConstituencyTree}.
	 * @return An {@link ArrayList} of child nodes of the given {@link Nonterminal} node.
	 * The return value is "null" if the {@link Nonterminal} node does not have child nodes.
	 */
	public ArrayList<Object> getChildren(Nonterminal node){
		
		return this.transitions.get(node);
	}
	
	/**
	 * Checks whether a given {@link Nonterminal} or {@link Terminal} node has a parent node.
	 * @param node A {@link Nonterminal} or {@link Terminal} node.
	 * @return "True" if the given node has a {@link Nonterminal} parent node, "False" if it does not.
	 * In the latter case, the given node is the root node.
	 */
	public boolean hasParent(Object node){
		
			for (Map.Entry<Nonterminal, ArrayList<Object>> me: this.transitions.entrySet()){
				for (Object nd: me.getValue()){
					if (nd == node){
						return true;
					}
				}
			}
			return false;		
	}
	
	/**
	 * @param node A {@link Nonterminal} or {@link Terminal} node.
	 * @return The {@link Nonterminal} parent node of the given node.
	 * If the given node is the root node, "null" will be returned.
	 */
	public Nonterminal getParent(Object node){
		
		for (Map.Entry<Nonterminal, ArrayList<Object>> me: this.transitions.entrySet()){
			for (Object nd: me.getValue()){
				if (nd == node){
					Nonterminal parent = me.getKey();
					return parent;
					}
				}
		}
		
		return null;
			
		}
	
	
	/**
	 * @param word A {@link String} representing a word.
	 * @return "True" if the given word is part of the sentence which the {@link ConstituencyTree} represents, "False" if it is not.
	 */
	public boolean isTerminal(String word){
		
		for (Terminal term: this.terminals){
			if (term.getWord().equals(word)){
				return true;
			}
	}
		return false;
	}
	
	/**
	 * @param word A {@link WordObj} containing a word as a {@link String} and additional information associated with it.
	 * @return "True" if the word associated with the given {@link WordObj} is part of the sentence which the {@link ConstituencyTree} represents.
	 * Additionally, the given {@link WordObj}'s position in the sentence which it was retrieved from has to be identical to the position of the word in the sentence which the {@link ConstituencyTree} represents.
	 * If one or both of these conditions does not hold, "False" is returned.
	 */
	public Terminal getTerminal(WordObj word){
		
	
		for (int i = 0; i < this.terminals.size(); i++){
			
			Terminal terminal = this.terminals.get(i);

			if (terminal.getWord().equals(word.getName()) && (i + 1) == word.getPosition()){
				return terminal;
			}
		}
		return null;
	}
	
	/**
	 * Checks whether a given {@link Nonterminal} node dominates a given {@link Terminal} node, i.e. whether it is an ancestor of that node.
	 * Iterates over all nodes that are reachable from the given {@link Nonterminal} node and checks whether one of them is the given {@link Terminal} node.
	 * @param nonterminal A {@link Nonterminal} node.
	 * @param terminal A {@link Terminal} node.
	 * @return "True" if the given {@link Nonterminal} node dominates the given {@link Terminal} node, "False" if it does not.
	 */
	public boolean dominates(Nonterminal nonterminal, Terminal terminal){
		
		ArrayList<Object> agenda = new ArrayList<Object>();

		agenda.add(nonterminal);
		
		// search all nodes reachable from the given Nonterminal node in a breadth-first manner for the given Terminal node.
		while (!agenda.isEmpty()){
			
			Object node = agenda.remove(0);
			if (node instanceof Terminal){
				Terminal terminalNode = (Terminal) node;
				if (terminalNode == terminal){
					return true;
				}
			}
			
			else {
				
				for (Object nd: this.transitions.get(node)){
					agenda.add(nd);
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Given a {@link Nonterminal} parent node and a {@link Nonterminal} or {@link Terminal} child node, create a transition in the tree between them.
	 * Helper function of the method {@link #extractConstituencyTree(Graph graph)}.
	 * @param parent The {@link Nonterminal} parent node of the transition.
	 * @param child The {@link Nonterminal} or {@link Terminal} child node of the transition.
	 */
	public void addTransition(Nonterminal parent, Object child){
		
		if (child instanceof Terminal){
			
			Terminal terminal = (Terminal) child;
			
			if (this.transitions.containsKey(parent)){
				
				ArrayList<Object> valueArray = this.transitions.get(parent);
				valueArray.add(terminal);
				this.transitions.put(parent, valueArray);
			}
			
			else {
				
				ArrayList<Object> valueArray = new ArrayList<Object>();
				valueArray.add(terminal);
				this.transitions.put(parent, valueArray);
			}
		}
		
		else if (child instanceof Nonterminal){
			
			Nonterminal nonterminal = (Nonterminal) child;
			
			if (this.transitions.containsKey(parent)){
				
				ArrayList<Object> valueArray = this.transitions.get(parent);
				valueArray.add(nonterminal);
				this.transitions.put(parent, valueArray);
			}
			
			else {
				
				ArrayList<Object> valueArray = new ArrayList<Object>();
				valueArray.add(nonterminal);
				this.transitions.put(parent, valueArray);
			}
		}
		
		
	}
	
	
	/**
	 * Given a {@link Graph} object from the Salsa API, build the {@link ConstituencyTree}.
	 * Starting from the root, the tree is created tier by tier.
	 * Nodes are identified by the {@link Id} assigned by the Salsa API.
	 * 
	 * @param graph The {@link Graph} object as built by the Salsa API.
	 */
	public void extractConstituencyTree(Graph graph) {
		
		this.graph = graph;
		this.terminals = graph.getTerminals().getTerminals();
		this.nonterminals = graph.getNonterminals().getNonterminals();
		
		ArrayList<Nonterminal> agenda = new ArrayList<Nonterminal>();
		
		for (Nonterminal nonterminal: this.nonterminals){
			
			// retrieve root of tree by finding the Nonterminal node that has the same Id as the root.
			if (nonterminal.getId().getId().equals(graph.getRoot().getId())) {
				
				this.root = nonterminal;
				agenda.add(nonterminal);
				
			}
			
		}
			
		// retrieve Ids of child nodes from outgoing edges of the current Nonterminal node.
		// add transitions for the current Nonterminal node and all its child nodes. Add all child nodes which are Nonterminal nodes to the agenda.
	
		while (!agenda.isEmpty()) {
			
			Nonterminal current = agenda.remove(0);
			
			for (salsa.corpora.elements.Edge edge: current.getEdges()){
				
				String childId = edge.getId().getId();
				
				for  (Nonterminal nt: this.nonterminals) {
					
					if (nt.getId().getId().equals(childId)){
						agenda.add(nt);
						addTransition(current, nt);

					}
				}
				
				for  (Terminal t: this.terminals) {
					
					if (t.getId().getId().equals(childId)){
						addTransition(current, t);

					}
				}
			}
		}
	}
		
	
	/**
	 * Given a {@link Nonterminal} node and a {@link Terminal} node, the {@link ConstituencyTree} will be descended for a given path length starting from the {@link Nonterminal} node towards the {@link Terminal} node.
	 * 
	 * @param nonterminal The {@link Nonterminal} node from which the descent starts.
	 * @param terminal The {@link Terminal} node towards which the descent is directed.
	 * @param lenPath The distance of the target node from the given {@link Nonterminal} node.
	 * @return The {@link Nonterminal} or {@link Terminal} node which is separated from the given {@link Nonterminal} node by a given path length and either dominates the given {@link Terminal} node or is the node itself.
	 */
	public Object descendTree(Nonterminal nonterminal, Terminal terminal, int lenPath){
		
		ArrayList<Nonterminal> path = new ArrayList<Nonterminal>(); // records the nonterminal nodes that are passed by ascending the tree from the given terminal node. The first item is the given nonterminal node. The second item is the nonterminal node that dominates the terminal node and is immediately dominated by the given nonterminal node.  
		Nonterminal currentNode = null;
		
		for (Map.Entry<Nonterminal, ArrayList<Object>> me: this.transitions.entrySet()){
			for (Object node: me.getValue()){
				if (node instanceof Terminal){
					Terminal nd = (Terminal) node;
					if (nd == terminal){
						currentNode = me.getKey();
						path.add(currentNode);
					}
				}
			}
		}
		
		while (currentNode != nonterminal){
			
			// if we have reached the root of the tree ascending from the given Terminal node, but have not encountered the given Nonterminal node, the Nonterminal node does not dominate the Terminal node
			if (currentNode == this.root){
				return null;
			}
			for (Map.Entry<Nonterminal, ArrayList<Object>> e: this.transitions.entrySet()){
				for (Object node: e.getValue()){
					if (node instanceof Nonterminal){
						Nonterminal nd = (Nonterminal) node;
						if (nd == currentNode){
							currentNode = e.getKey();
							path.add(0, currentNode);
							
							if (currentNode == nonterminal){
								return path.get(lenPath);
							}
						}
					}
						
					}
				}
		}
		
		// for the case that there is NO Nonterminal node between the given Nonterminal node and the given Terminal node, i.e. that the given Terminal node is a direct child of the given Nonterminal node
		if (path.size() == 1){
			return terminal;
		}
		
		// for the case that there IS a Nonterminal node between the given Terminal node and the given Nonterminal node, i.e. that the given Terminal node is NOT a direct child of the given Nonterminal node
		return path.get(lenPath);

	}
	
	/**
	 * Given an argument and a predicate in a {@link ConstituencyTree}, we want to return the {@link String} which corresponds to the phrase which
	 * a) either dominates the argument node or is the argument node
	 * b) is directly dominated by the node which dominates both the predicate and the argument node
	 * 
	 * Example: [ S [ NP [ DET The] [ N gardener] [ VP  [V likes ] [ NP [ PN his ] [ N flowers ] ] ] ] ]
	 * Given that "likes" is the predicate and "gardener" is the argument, this function will first find the lowest node in the tree which dominates both the predicate and argument node (here: S: "The gardener likes his flowers").
	 * From this node, the function will then descend the tree for a path length of 1 in the direction of the argument.
	 * The node retrieved in this way is our target node. The string corresponding to this node will be returned.
	 *
	 * @param predicate The predicate represented by a {@link WordObj}.
	 * @param argument The argument represented by a {@link WordObj}.
	 * @return The {@link String} corresponding to the phrase whose node is dominated directly by the lowest node in the tree dominating both the predicate and the argument node.
	 */
	public String getArgumentString(WordObj predicate, WordObj argument){
		
		String sourceTargetPhrase;
		
		Terminal predicateNode = getTerminal(predicate);
		Terminal argumentNode = getTerminal(argument);
		
		// for arguments which are relative pronouns with the syntactical function of subject or direct object, return the String of the argument itself
		if ((argument.getRelation().equals("subj") || argument.getRelation().equals("dobj")) && argument.getPos().equals("PRELS")){
			return argument.getName();
		}
		
		Nonterminal parentNode = getParent(predicateNode);
		
		// ascend the tree from the predicate node until a node is found which also dominates the argument node.
		while (!(dominates(parentNode, predicateNode) && dominates(parentNode, argumentNode))){
			parentNode = getParent(parentNode);
		}
				
		Object phrase = descendTree(parentNode, argumentNode, 1);
		
		// retrieve the String corresponding to the target node
		if (phrase instanceof Nonterminal){
			Nonterminal nonterminalPhrase = (Nonterminal) phrase;
			sourceTargetPhrase = getPhraseAsString(nonterminalPhrase);
			return sourceTargetPhrase;
		}
		
		else if (phrase instanceof Terminal){
			Terminal terminalPhrase = (Terminal) phrase;
			sourceTargetPhrase = terminalPhrase.getWord();
			return sourceTargetPhrase;
		}
		
		return null;
		
	}
	
	/**
	 * Given an argument and a predicate in a {@link ConstituencyTree}, we want to return a node which
	 * a) either dominates the argument node or is the argument node
	 * b) is directly dominated by the node which dominates both the predicate and the argument node
	 * 
	 * Example: [ S [ NP [ DET The] [ N gardener] [ VP  [V likes ] [ NP [ PN his ] [ N flowers ] ] ] ] ]
	 * Given that "likes" is the predicate and "gardener" is the argument, this function will first find the lowest node in the tree which dominates both the predicate and argument node (here: S: "The gardener likes his flowers").
	 * From this node, the function will then descend the tree for a path length of 1 in the direction of the argument.
	 * The node retrieved in this way is our target node.
	 *
	 * If 'depGraph' is not {@code null} the returned node may violate point a)
	 * and b). This case occurs whenever the opinion source is searched. For
	 * cases such as sentences containing conjunctions of verbs, predicative and
	 * attributive adjectives the "dominates"-relation is not always
	 * appropriate. Consider the following example:
	 *
	 * - Sentence: "The gardener likes and loves this flowers."
	 * - Constituency tree:
	 *   [ S [ VP [ NP [ DET The ] [ N gardener ] ] [ V likes ] ] [ CON and ] [ VP [ V loves ] [ NP [ PN his] [ N flowers ] ] ] ]
	 *
	 * Opinion source for "loves" should be "The gardener" but with the original
	 * implementation it was "The gardener likes" (i.e. 'VP' node instead of the
	 * 'NP' node was returned). As only the 'S' dominates the predicate 'loves'
	 * and the argument 'gardener' the implementation chose the 'VP' node since
	 * it is direct child node of 'S' (path length 1). Obviously this not
	 * desired.
	 *
	 * The improved implementation follows the constructed path (S, VP, NP, N,
	 * gardener) as long as the corresponding subtree contains at least one
	 * terminal with the same POS-tag as the predicate (in steps of size 1).
	 * Once no such terminal node is found the current node on the path is
	 * returned.
	 *
	 * - S, VP, NP, N, gardener: 'likes' is part of the subtree of S
	 * - VP, NP, N, gardener: 'likes' is part of the subtree of VP
	 * - NP, N, gardener: 'likes' is no longer part of any subtree
	 *
	 * As there is no other "candidate" besides 'likes' the search is finished.
	 *
	 * @param predicate The predicate represented by a {@link WordObj}.
	 * @param argument The argument represented by a {@link WordObj}.
	 * @param depGraph Graph used to retrieve the word list.
	 * @return The node which is dominated directly by the lowest node in the tree dominating both the predicate and the argument node.
	 */
	public Object getArgumentNode(WordObj predicate, WordObj argument, DependencyGraph depGraph){
		
		Terminal predicateNode = getTerminal(predicate);
		Terminal argumentNode = getTerminal(argument);
		
		// for arguments which are relative pronouns with the syntactical function of subject or direct object, return the node of the argument itself
		if ((argument.getRelation().equals("subj") || argument.getRelation().equals("dobj")) && argument.getPos().equals("PRELS")){
			return argumentNode;
		}
		
		Nonterminal parentNode = getParent(predicateNode);
		
		// ascend the tree from the predicate node until a node is found which also dominates the argument node.
		while (!(dominates(parentNode, predicateNode) && dominates(parentNode, argumentNode))){
			parentNode = getParent(parentNode);
		}

		Object phrase = descendTree(parentNode, argumentNode, 1);

		// In case there was a DependyGraph given do the following steps:
		// - find all WordObj's that have the same POS-tag as 'predicate'
		// - substitute the WordObj's with their respective terminal node
		// - if 'phrase' contains at least one of these terminals repeat with 'phrase' as the new top node
		if (depGraph != null) {
			List<Terminal> candidates = computeTerminalCandidates(predicate, depGraph);
			
			while (containsTerminal(phrase, candidates))
				phrase = descendTree((Nonterminal) phrase, argumentNode, 1);
		}
		
		return phrase;
	}

	/**
	 * This method is for the extraction of phrases for MWE-predicates
	 * Given an argument and an MWE-predicate in a {@link ConstituencyTree}, we want to return a node which
	 * a) either dominates the argument node or is the argument node
	 * b) is dominated by the node which dominates both the MWE-predicate and argument node
	 * c) the node must NOT dominate any token of the MWE-predicate
	 *
	 * @param predicate The predicate represented by a {@link WordObj}.
	 * @param argument The argument represented by a {@link WordObj}.
	 * @param depGraph Graph used to retrieve the word list.
	 * @param allPredicateTokens All tokens part of the MWE-predicate represented by by a {@link List<WordObj>}.
	 * @return The node which is dominated directly by the lowest node in the tree dominating both the predicate and the argument node.
	 */
	public Object getArgumentNodeMWE(WordObj predicate, WordObj argument, List<WordObj> allPredicateTokens){

		Terminal predicateNode = getTerminal(predicate);
		Terminal argumentNode = getTerminal(argument);

		// for arguments which are relative pronouns with the syntactical function of subject or direct object, return the node of the argument itself
		if ((argument.getRelation().equals("subj") || argument.getRelation().equals("dobj")) && argument.getPos().equals("PRELS")){
			return argumentNode;
		}

		Nonterminal parentNode = getParent(predicateNode);

		// ascend the tree from the predicate node until a node is found which also dominates the argument node.
		while (!(dominates(parentNode, predicateNode) && dominates(parentNode, argumentNode))){
			parentNode = getParent(parentNode);
		}

		// preliminary phrase (if further operations fail; use this node!)
		Object phrase = descendTree(parentNode, argumentNode, 1);

		// get all terminal nodes representing the the tokens of the MWE-predicate
		List<Terminal> candidates = computeExactTerminalCandidates(allPredicateTokens);
//		for(Terminal c:candidates){
//			System.out.println("TERMINAL: " + c.getLemma() + " (" + c.toString() + ")");
//		}
//		System.out.println();

		// descend the tree as long as tokens of the MWE-predicate are part of the phrase
		while (containsTerminal(phrase, candidates)){
			phrase = descendTree((Nonterminal) phrase, argumentNode, 1);
		}

		return phrase;
	}

	/**
	 * Computes all leafs (terminal nodes) in the constituency tree that correspond
	 * to a set of tokens from the dependency graph
	 *
	 * @param List<WordObj> object representing the tokens
	 *
	 * @return A list of all terminal nodes representing the predicateTokens
	 */
	private List<Terminal> computeExactTerminalCandidates(List<WordObj> tokens) {
		List<Terminal> candidates = new ArrayList<>();

		for(WordObj predicateToken:tokens){
			candidates.add(getTerminal(predicateToken));
		}

		return candidates;
	}
	
	/**
	 * Computes all leafs (terminal nodes) in the constituency tree that have a
	 * corresponding word object with the same POS-tag as the given 'predicate'
	 * word object.
	 *
	 * @param predicate Word object to compare with.
	 * @param depGraph Dependency graph used to find all relevant word objects.
	 *
	 * @return A list of all terminal nodes that have the desired POS-tag and
	 * are not 'predicate'.
	 */
	private List<Terminal> computeTerminalCandidates(WordObj predicate, DependencyGraph depGraph) {
		List<Terminal> candidates = new ArrayList<>();

		for (WordObj t : depGraph.getWordList()) {
			if (!t.equals(predicate) && t.getPos().equals(predicate.getPos())) {
				candidates.add(getTerminal(t));
			}
		}

		return candidates;
	}

	private boolean containsTerminal(Object phrase, List<Terminal> candidates) {
		// cannot contain any terminal node if it is not a nonterminal
		if (!(phrase instanceof Nonterminal))
			return false;
		
		// Gather all child nodes in 'todo' and check whether one of them is a
		// candidate terminal. If this is the case one of the terminals is
		// part of the phrase.
		Queue<Object> todo = new LinkedList<>();
		todo.offer(phrase);
		while (todo.peek() != null) {
			Object top = todo.poll();
			if (candidates.contains(top))
				return true;
			
			List<Object> children = this.transitions.get(top);
			if (children == null)
				continue;
			
			for (Object child : children)
				todo.offer(child);
		}
		
		// default case: no candidate terminal is part of the phrase
		return false;
	}

	/**
	 * Given a {@link Nonterminal} node, return the {@link String} over which the corresponding phrase spans.
	 * E.g. for the sentence "The talented musician played a concert last night", for the {@link Nonterminal} node corresponding to the noun phrase "The talented musician", exactly this {@link String} will be returned.
	 * @param nonterminal The {@link Nonterminal} node for which the corresponding {@link String} is returned.
	 * @return The {@link String} corresponding to the phrase of the {@link Nonterminal} node.
	 */
	public String getPhraseAsString(Nonterminal nonterminal){
		
		StringBuffer phraseString = new StringBuffer();
		
		ArrayList<Object> agenda = new ArrayList<Object>();
		
		ArrayList<Object> children = this.transitions.get(nonterminal);
		
		for (int j = children.size() - 1; j >= 0; j--){
			  agenda.add(0, children.get(j));
			}
		
		// recursively search all child nodes of the given Nonterminal node in a depth-first manner. If a Terminal node is encountered, concatenate the String corresponding to it to the String of the phrase.
		while (!agenda.isEmpty()){
			
			Object node = agenda.remove(0);
			
			if (node instanceof Terminal){
				Terminal terminal = (Terminal) node;
				phraseString.append(terminal.getWord());
				phraseString.append(" ");
			}
			
			else {
				
				Nonterminal nonterminalNode = (Nonterminal) node;
				phraseString.append(getPhraseAsString(nonterminalNode).toString());
				
			}
		}
		return phraseString.toString();
	}


	/**
	 * @param wordNode
	 * @param category
	 * @return true if any {@link Nonterminal} of the given category dominates {@code wordNode}, false otherwise
	 */
	boolean hasDominatingNode(Terminal wordNode, String category) {
		final Nonterminal dominatingNode = getLowestDominatingNode(wordNode, category);
		return dominatingNode != null;
	}

	/**
	 * @param wordNode
	 * @param category
	 * @return The lowest {@link Nonterminal} of the given category that dominates {@code wordNode}. {@code null} if none does.
	 */
	Nonterminal getLowestDominatingNode(Terminal wordNode, String category) {
		// TODO: case of category
		assert hasParent(wordNode);

		Nonterminal cursor;
		cursor = getParent(wordNode);
		while (!cursor.getCat().equals(category)) {
			if (!hasParent(cursor)) {
				return null;
			} else {
				cursor = getParent(cursor);
			}
		}
		return cursor;
	}

	/**
	 * @param clause A {@link Nonterminal} representing a clause or sentence
	 * @return The list of clauses contained within {@code clause}
	 */
	private List<Nonterminal> getSubclauses(Nonterminal clause) {
		final List<Nonterminal> result = new ArrayList<Nonterminal>();
		for (Object child: getChildren(clause)) {
			assert child instanceof Terminal || child instanceof Nonterminal;
			if (child instanceof Nonterminal && ((Nonterminal) child).getCat().equals("S")) {
				result.add((Nonterminal) child);
			}
		}
		return result;
	}

	/**
	 * @param containingClause A {@link Nonterminal}, generally with type S.
	 * @return Only the main clause without any subclauses. The returned collection contains
	 * {@link Terminal} and {@link Nonterminal} objects.
	 */
	List<Object> getMainClause(Nonterminal containingClause) {

		final List<Nonterminal> subclausesToRemove = getSubclauses(containingClause);

		if (subclausesToRemove.isEmpty()) {
			
			return Arrays.asList((Object) containingClause);
		} else {
			final List<Object> result = new ArrayList<Object>(getChildren(containingClause));

			result.removeAll(subclausesToRemove);

			// Punctuation
			{
				final List<Terminal> punctuation = new ArrayList<Terminal>();
				for (Object child: getChildren(containingClause)) {
					if (child instanceof Terminal && new Pos(((Terminal) child)).getCoarse() == PosCoarse.PUNCTUATION) {
						punctuation.add((Terminal) child);
					} else {
						assert child instanceof Nonterminal;
					}
				}
				result.removeAll(punctuation);
			}

			return result;
		}
	}


	
	public String toString() {
		
		ArrayList<Object> agenda = new ArrayList<Object>();
		
		StringBuffer printer = new StringBuffer();
		
		if (this.root != null) {
			printer.append("\n" + this.root.getCat());
			ArrayList<Object> children = this.transitions.get(this.root);
			
			for (Object child: children) {
				agenda.add(child);
			}
			
			while (!agenda.isEmpty()){
				
				Object node = agenda.remove(0);	

				
				if (node instanceof Terminal){
					Terminal nd = (Terminal) node;
					printer.append("(" + nd.getPos() + " " + nd.getWord() + ") ");

				}
				
				else if (node instanceof Nonterminal){
					Nonterminal nd = (Nonterminal) node;
					printer.append("\n" + nd.getCat() + " ");
				}
				
				
				if (this.transitions.containsKey(node)){
					
					ArrayList<Object> ch = this.transitions.get(node);
					
					for(int j = ch.size() - 1; j >= 0; j--){
						  agenda.add(0, ch.get(j));
						}
			}
				
			}
			return printer.toString();
		}
		return "";
	}

}
	
