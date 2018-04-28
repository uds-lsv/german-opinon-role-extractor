
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lennart Schmeling (initial version)
 * @author Katja König (conjunction normalization, stringify)
 *
 * An instance of the class DependencyGraph represents an instance of the
 * dependency graph structure of a sentence. All dependency graphs have a
 * WordObj root as their uppermost node. Then they consist of a HashSet in which
 * all the graph's edges are stored (existing dependency relations between
 * WordObj). Also, all the nodes of the graph are stored in a separate HashSet.
 *
 */
public class DependencyGraph {

  private WordObj root;
  private HashSet<Edge> edges;
  private HashSet<WordObj> nodes;
  private List<WordObj> wordList;

  // true if the graph is the result of the normalization for active and passive
  // voice
  private boolean ap;

  public DependencyGraph(List<WordObj> wl) {
    this.root = new WordObj("root");
    this.edges = new HashSet<Edge>();
    this.nodes = new HashSet<WordObj>();
    wordList = wl;
  }

  public DependencyGraph(DependencyGraph old) {
    this.root = new WordObj("root");
    this.edges = new HashSet<>(old.edges);
    this.nodes = new HashSet<>(old.nodes);
    this.wordList = old.wordList;
    this.ap = old.ap;
  }

  /**
   *
   * @return The WordObj root of a graph object
   */
  public WordObj getRoot() {
    return root;
  }

  /**
   *
   * @return the HashSet in which all edges of a graph are stored
   */
  public HashSet<Edge> getEdges() {
    return edges;
  }

  /**
   *
   * @return the HashSet in which all the nodes of a graph are stored
   */
  public HashSet<WordObj> getNodes() {
    return nodes;
  }

  /**
   * Returns the list of word objects.
   *
   * @return the list of word objects.
   */
  public List<WordObj> getWordList() {
    return wordList;
  }

  /**
   * add a node to the HashSet nodes of a graph object
   *
   * @param node The node which should be added
   * @return True if the node is not already in the HashSet, False otherwise
   */
  public boolean addNode(WordObj node) {
    if (!this.nodes.contains(node)) {
      this.nodes.add(node);
      return true;
    }

    return false;
  }

  /**
   * add an edge to the Hashset edges of a graph object
   *
   * @param source the node where the edge starts
   * @param target the node where the edge terminates
   * @param depRel the dependency label which should be annotated for this edge
   * @return the newly added edge
   */
  public Edge addEdge(WordObj source, WordObj target, String depRel) {
    Edge edge = new Edge(source, target, depRel);
    this.edges.add(edge);
    return edge;
  }

  /**
   *
   * @param target A node in the graph which is supposed to have an incoming
   * edge with a certain label
   * @param depRel the dependency relation of this edge
   * @return True if edge exists, False otherwise
   */
  public boolean hasParent(WordObj target, String depRel) {
    for (Edge edge : this.edges) {
      if (target.equals(edge.target) && depRel.equals(edge.depRel)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return a node who is starting point for an edge with a specified dependency
   * relation to a certain child node
   *
   * @param target the node to which an edge with a dependency label should
   * point
   * @param depRel the dependency relation for this edge
   * @return the parent node if it has an edge in the graph to the child with
   * the dependency relation
   */
  public WordObj getParent(WordObj target, String depRel) {
    for (Edge edge : this.edges) {
      if (target.equals(edge.target) && depRel.equals(edge.depRel)) {
        return edge.source;
      }
    }
    return null;
  }

  /**
   * Return a node to which points an edge from the parent node with a specified
   * dependency relation
   *
   * @param source a node in the graph
   * @param depRel the dependency relation
   * @return the node if it has an incoming edge from the source WordObj with
   * the depRel indicated
   */
  public WordObj getChild(WordObj source, String depRel) {
    for (Edge edge : this.edges) {
      if (source.equals(edge.source) && depRel.equals(edge.depRel)) {
        return edge.target;
      }
    }
    return null;
  }

  /**
   * Given a string as input, return the corresponding WordObj from the graph as
   * output if existing
   *
   * @param word A string which is supposed to be equivalent to a node in the
   * graph
   * @return The WordObj if it exists
   */
  public WordObj getNode(String word) {
    for (WordObj wordobj : this.nodes) {
      if (wordobj.getName().equals(word)) {
        return wordobj;
      }
    }
    return null;
  }

  /**
   * compute the source node of a certain WordObj in the graph
   *
   * @param node A WordObj whose parent should be investigated
   * @return the WordObj which has an outgoing edge to the node
   */
  public ArrayList<WordObj> getSources(WordObj node) {

    ArrayList<WordObj> list = new ArrayList<WordObj>();

    for (Edge edge : this.edges) {
      if (edge.target == node) {
        list.add(edge.source);
      }
    }

    return list;
  }

  /**
   * collect all WordObj to which a certain node has edges in an ArrayList
   *
   * @param node A WordObj from the graph
   * @return a list of all nodes who have an incoming edge from the node WordObj
   */
  public ArrayList<WordObj> getTargets(WordObj node) {

    ArrayList<WordObj> list = new ArrayList<WordObj>();

    for (Edge edge : this.edges) {
      if (edge.source == node) {
        list.add(edge.target);
      }
    }

    return list;
  }

  /**
   * check if there is a node in graph which has an incoming edge with a
   * specified dependency relation
   *
   * @param target The node whose incoming edges should be examined
   * @param depRel The dependency relation, annotated on an edge pointing on
   * this node
   * @return true if relation exists, false otherwise
   */
  public boolean hasTargetRelation(WordObj target, String depRel) {
    for (Edge edge : this.edges) {
      if (target.equals(edge.target) && depRel.equals(edge.depRel)) {
        return true;
      }

    }
    return false;
  }

  /**
   * check if there are two linked edges in the graph where the dependency label
   * of 1st edge is either objp or pp and the 2nd one is a pn - this is
   * important to normalize prepositional phrases two for-loops needed because
   * there are seldom occurrences of objp/pp only, those should not be
   * considered
   *
   * @param source WordObj which is supposed to be the parent node of the
   * preposition
   * @param depRel either objp or pp
   * @return true if prepositional phrase exists, no otherwise
   */
  public boolean hasObjpPpToPnRelation(WordObj source, String depRel) {
    for (Edge edge : this.edges) {
      if (source.equals(edge.source) && depRel.equals(edge.depRel)) {
        WordObj tar = edge.target;
        for (Edge e : this.edges) {
          if (tar.equals(e.source) && "pn".equals(e.depRel)) {
            return true;
          }
        }

      }
    }
    return false;
  }

  /**
   * Given the node in a graph which is starting point for a objp/pp + pn
   * relation (2 edges in a row, indicating a PP), return the node which is
   * ending point of this extended relation. This is normally the WordObj which
   * is at the same time the head of the noun phrase in the prepositional
   * phrase.
   *
   * @param source the WordObj from which the prepositional relation starts
   * @param depRel depending if the prepositional phrase is complement or
   * adjunct, either objp or pp
   * @return the node in the graph to which the objp/pp + pn- Relation points
   */
  public WordObj getPnFromObjpPpToPnRelation(WordObj source, String depRel) {
    for (Edge edge : this.edges) {
      if (source.equals(edge.source) && depRel.equals(edge.depRel)) {
        WordObj tar = edge.target;
        for (Edge e : this.edges) {
          if (tar.equals(e.source) && "pn".equals(e.depRel)) {
            return e.target;
          }
        }

      }
    }
    return null;
  }

  /**
   * This method returns either the corresponding source or the target
   * (depending if it has been called to identify source or target in
   * SentimentChecker class) for a given sentiment expression.
   *
   * @param sentiment The sentiment expression the graph contains (WordObj is
   * entry in the sentiment lexicon)
   * @param label It should be checked whether the sentiment expression has an
   * outgoing node to another node (this would then be the source or the target)
   * with a certain dependency relation specified in this parameter
   * @param containsDeleted If there is a sentiment expression whose
   * corresponding node in the graph of the sentence has been deleted due to
   * normalization, the WordObj parent node (which is still in the graph,
   * necessary to yield existing graph relations) will be stored in this
   * parameter (empty otherwise)
   * @return a list in which the source or the target is stored, empty otherwise
   * if no fitting source/target exists for given label and sent expr
   */
  public ArrayList<WordObj> getSentimentSourceTarget(WordObj sentiment, String label, WordObj containsDeleted) {
    // if sentiment expression in deletedNodes, find the parentNode in which
    // the information about DeletedWord Obj is stored
    // and take this node as sentiment expr (to find graph relations)
    if (!(containsDeleted.getName().equals(""))) {
      sentiment = containsDeleted;
    }
    ArrayList<WordObj> list = new ArrayList<WordObj>();
    if ("objp-*".equals(label)) {
      for (Edge edge : this.edges) {
        if (!list.isEmpty()) {
          break;
        }
        if (edge.source.equals(sentiment) && edge.depRel.contains("objp")) {
          list.add(edge.target);
        }
      }
      return list;
    } else if ("attr-rev".equals(label)) {
      for (Edge edge : this.edges) {
        if (edge.target.equals(sentiment) && edge.depRel.equals("attr")) {
          list.add(edge.source);
        }
      }
      return list;
    } else if ("det".equals(label)) {
      for (Edge edge : this.edges) {
        if (edge.source.equals(sentiment) && edge.depRel.equals(label)) {
          if (edge.target.getPos().equals("PPOSAT")) {
            list.add(edge.target);
          }

        }
      }
      return list;
    } else {
      for (Edge edge : this.edges) {
        if (!list.isEmpty()) {
          break;
        }
        if (edge.source.equals(sentiment) && edge.depRel.equals(label)) {
          list.add(edge.target);
        }
      }
    }
    return list;
  }

  /**
   * This method returns either the corresponding source or the target
   * (depending if it has been called to identify source or target in
   * SentimentChecker class) for a given sentiment expression.
   *
   * @param sentiment The sentiment expression the graph contains (WordObj is
   * entry in the sentiment lexicon)
   * @param label It should be checked whether the sentiment expression has an
   * outgoing node to another node (this would then be the source or the target)
   * with a certain dependency relation specified in this parameter
   * @return a list in which the source or the target is stored, empty otherwise
   * if no fitting source/target exists for given label and sent expr
   */
  public ArrayList<WordObj> getSentimentSourceTarget(WordObj sentiment, String label) {
    // if sentiment expression in deletedNodes, find the parentNode in which
    // the information about DeletedWord Obj is stored
    // and take this node as sentiment expr (to find graph relations)
    ArrayList<WordObj> list = new ArrayList<WordObj>();
    if ("objp-*".equals(label)) {
      for (Edge edge : this.edges) {
        if (!list.isEmpty()) {
          break;
        }
        if (edge.source.equals(sentiment) && edge.depRel.contains("objp")) {
          list.add(edge.target);
        }
      }
      return list;
    } else if ("attr-rev".equals(label)) {
      for (Edge edge : this.edges) {
        if (edge.target.equals(sentiment) && edge.depRel.equals("attr")) {
          list.add(edge.source);
        }
      }
      return list;
    } else if ("det".equals(label)) {
      for (Edge edge : this.edges) {
        if (edge.source.equals(sentiment) && edge.depRel.equals(label)) {
          if (edge.target.getPos().equals("PPOSAT")) {
            list.add(edge.target);
          }

        }
      }
      return list;
    } else {
      for (Edge edge : this.edges) {
        if (!list.isEmpty()) {
          break;
        }
        if (edge.source.equals(sentiment) && edge.depRel.equals(label)) {
          list.add(edge.target);
        }
      }
    }
    return list;
  }

  /**
   * if participle contains the lemma "werden" in deletedWords (after
   * aux-Normalization), we encountered a passive sentence in which
   * normalization of dependency labels is needed. Attention: rule out
   * Konjunktiv II-sentences in German by boolean "habenInside": in "Er wird
   * gegessen haben" is no normalization needed although "werden" is contained
   *
   * @param source the source which is supposed to be the participle of a
   * passive form (Partizip II in German)
   * @return true if "haben" or "werden" in DeletedWords, false otherwise
   */
  //
  public boolean checkPassive(WordObj source) {
    boolean habenInside = false;
    for (WordObj w : source.getDeleted()) {
      if ("haben".equals(w.getLemma())) {
        habenInside = true;
      }
      if ("werden".equals(w.getLemma())) {
        if (habenInside == false) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This method checks if there are phrasal verbs in a sentence (e.g.
   * "auf|brauchen, ab|nehmen") if this is the case, the lemma entry in the
   * WordObj class will be changed for the root of the verb (nehmen --> abnehmen
   * etc.) if phrasal verbs are sentiment expressions, they will not be
   * retrieved otherwise
   *
   * @param graph dependency graph as input
   */
  public void correctPhrasalVerbs(DependencyGraph graph) {
    for (Edge edge : graph.edges) {
      if ("avz".equals(edge.depRel)) {
        String particle = edge.target.getLemma();
        String verb = edge.source.getLemma();
        String newVerbLemma = particle + verb;
        edge.source.setLemma(newVerbLemma);
        edge.source.setIsParticleVerb(true);
        edge.source.setParticle(edge.target);
      }
    }
  }

  /**
   * check if graph is already normalized (no more edges with dependency labels
   * pred, aux, pp and pn)
   *
   * @return True if this is the case, False otherwise
   */
  public boolean isNormalized() {

    for (WordObj word : this.wordList) {
      if (this.hasTargetRelation(word, "pred") || this.hasTargetRelation(word, "aux")
              || this.hasObjpPpToPnRelation(word, "objp") || this.hasObjpPpToPnRelation(word, "pp")) {
        return false;
      }
    }
    return true;
  }

  /**
   * check if graph has edges which call for one of the normalization methods
   * (for preposition phrases / predicative adjectives / auxillary verbs)
   * while-loop forces the method to run until all the normalizations have taken
   * place (often more than one per sentence)
   *
   * @param graph the sentence's graph as input
   * @return the normalized graph, or if no normalization needed, the same graph
   * as the input
   */
  public DependencyGraph normalize(DependencyGraph graph) {

    while (!graph.isNormalized()) {

      for (WordObj word : this.wordList) {

        // pred and aux can be normalized in the same way (rising one
        // level higher in the dependency parse)
        if (graph.hasTargetRelation(word, "pred") || graph.hasTargetRelation(word, "aux")) {
          graph = normalizePredVerb(word, graph);
          break;
        }

        // objp and pp can be normalized in the same way (join two
        // edges, adding a new objp-* relation)
        else if (graph.hasObjpPpToPnRelation(word, "objp")) {
          graph = normalizeObjpPP(word, graph, "objp");
          break;
        } else if (graph.hasObjpPpToPnRelation(word, "pp")) {
          graph = normalizeObjpPP(word, graph, "pp");
          break;
        }
      }
    }

    graph.correctPhrasalVerbs(graph);

    return graph;
  }

  /**
   * normalize auxillary verbs and predicative expressions and return an updated
   * graph object
   *
   * @param word The WordObj of the auxillary verb or predicative expression
   * respectively
   * @param graph The graph object of the sentence
   * @return an updated graph object in with adjusted nodes and edges
   */
  public DependencyGraph normalizePredVerb(WordObj word, DependencyGraph graph) {

    DependencyGraph normalized = new DependencyGraph(this.wordList);

    WordObj parentNode = graph.getParent(word, word.getRelation());

    // the parent node of pred/aux object will be replaced by pred/aux
    // object, so collect all
    // dependencies of the parent node
    ArrayList<WordObj> parentTargets = graph.getTargets(parentNode);
    ArrayList<WordObj> parentSources = graph.getSources(parentNode);
    // this is to fix the case in which two moved predicates are in a
    // dependency, which might
    // otherwise result in infinite loop (wrong dependency relations)
    if (parentTargets.isEmpty() && parentSources.isEmpty()) {
      for (Edge edge : graph.edges) {
        if ((word.equals(edge.target) && "pred".equals(edge.depRel))
                || (word.equals(edge.target) && "aux".equals(edge.depRel))) {
          graph.edges.remove(edge);
          graph.addEdge(edge.source, edge.target, parentNode.getRelation());
          break;
        }
      }
    }

    // since parent node is removed, the child node now takes all incoming
    // edges of the parent node. Hence, the label of the incoming edge
    // (constituent) is changed to that of the incoming edge of the parent
    // node.
    word.setRelation(parentNode.getRelation());

    // save every node in graph (deleted node will be saved in pred's/ aux's
    // node)
    for (WordObj w : graph.nodes) {
      if (w != parentNode && w != word) {
        normalized.addNode(w);
      } else if (w == word) {
        if (parentNode != word) {
          word.addDeleted(parentNode);
        }
        normalized.addNode(word);
      }
    }

    // predicative expr. /auxillary one level higher, generate corresponding
    // new edges
    for (WordObj target : parentTargets) {
      if (target != word) {
        for (Edge edges : graph.edges) {
          if (target.equals(edges.target)) {
            normalized.addEdge(word, target, edges.depRel);
          }

        }

      }
    }
    // add edges from source of the (former) predicate's / auxillary's
    // parent node to predicate / aux
    for (WordObj source : parentSources) {
      if (source.equals(graph.root)) {
        normalized.addEdge(source, word, "root");
      } else {
        normalized.addEdge(source, word, parentNode.getRelation());
      }
    }
    // add all old edges
    for (Edge edge : graph.edges) {
      if (edge.source != parentNode && edge.target != parentNode) {
        normalized.addEdge(edge.source, edge.target, edge.depRel);
      }
    }

    return normalized;
  }

  /**
   * normalize prepositional phrases and return an updated graph object
   *
   * @param word the WordObj in the graph from which the prepositional phrase's
   * edges leave
   * @param graph the sentence's graph object
   * @param depRel specify if the prepositional phrase is either introduced by a
   * pp- oder an objp-relation
   * @return the normalized graph object
   */
  public DependencyGraph normalizeObjpPP(WordObj word, DependencyGraph graph, String depRel) {

    DependencyGraph normalized = new DependencyGraph(this.wordList);

    WordObj parentNode = word;

    WordObj childNode = graph.getChild(word, depRel);
    WordObj childNodeObjpPP = graph.getPnFromObjpPpToPnRelation(word, depRel);

    // save every node in graph - save deleted information in pp's/pn's node
    for (WordObj w : graph.nodes) {
      if (w == childNode) {
        parentNode.addDeleted(w);
      } else {
        normalized.addNode(w);
      }
    }

    // add new edge with deprel objp-*+right prep
    String prep = childNode.getName().toLowerCase();
    String depRelation = "objp-" + prep;
    normalized.addEdge(parentNode, childNodeObjpPP, depRelation);

    // add old edges except for those two replaced by new edge
    for (Edge edge : graph.edges) {
      if (edge.source != childNode && edge.target != childNode) {
        normalized.addEdge(edge.source, edge.target, edge.depRel);
      }
    }
    return normalized;
  }

  /**
   * normalize dependency relations for passive forms: swap dependency relations
   * for subj and obja/objp-von/objp-vom Example: "Der Kuchen wurde von Jan
   * geliebt" - "der Kuchen" must be target and therefore changed to an
   * obja-relation (although it is the sentence's subject) "von Jan" is source
   * and its dependency relation must change from objp-von to subj
   *
   * @param graph the sentence's graph as input
   * @return the updated dependency graph with new relations (if necessary)
   */
  public DependencyGraph normalizeActivePassive(DependencyGraph graph) {

    DependencyGraph normalizedActPass = new DependencyGraph(this.wordList);

    normalizedActPass.nodes = graph.nodes;

    for (Edge edge : graph.edges) {
      if ("VVPP".equals(edge.source.getPos())) {
        if (graph.checkPassive(edge.source)) {
          if ("subj".equals(edge.depRel)) {
            normalizedActPass.addEdge(edge.source, edge.target, "obja");
            // as soon as an edge is added change active-passive flag to true
            normalizedActPass.ap = true;
          } else if ("objp-von".equals(edge.depRel) || ("objp-vom".equals(edge.depRel))) {
            normalizedActPass.addEdge(edge.source, edge.target, "subj");
            normalizedActPass.ap = true;

          } else {
            normalizedActPass.addEdge(edge.source, edge.target, edge.depRel);
            normalizedActPass.ap = true;
          }

        } else {
          normalizedActPass.addEdge(edge.source, edge.target, edge.depRel);
          normalizedActPass.ap = true;
        }

      } else {
        normalizedActPass.addEdge(edge.source, edge.target, edge.depRel);
        normalizedActPass.ap = true;
      }

    }
    return normalizedActPass;
  }

  /**
   * Adds the 'obja' relation of the 'parent' to the 'child' if applicable
   * ('parent' remains unchanged).
   *
   * @param graph Used to find 'obja' relation.
   * @param parent Word object considered to be conjunction parent.
   * @param child Word object considered to be conjunction child.
   */
  private void normalizeConjunctionPassive(DependencyGraph graph, WordObj parent, WordObj child) {
    String label = "obja";
    WordObj target = graph.getChild(parent, label);
    if (target != null) {
      graph.addEdge(child, target, label);
    }
  }

  /**
   * For a given pair of conjuncts transfer (copy) the contemplable relation
   * from the first to the second conjunct.
   *
   * Contemplable relation (depending on the POS-tag):
   *
   * - "ADJA" -> "attr"
   * - "VVFIN", "VVPP", "ADJD" -> "subj"
   * - "VVPP" -> "obja"
   *
   * As commas are not considered to be conjunctions by the parser they are
   * skipped in the word list iteration and their treatment is postponed by
   * setting 'prevComma' of the next word in the word list to true.
   *
   * @param graph Dependency graph used to extract relational information.
   * @param word Current word object in the word list.
   * @param prevComma Was the last word object in the word list a comma?
   */
  private void normalizeConjunction(DependencyGraph graph, WordObj word, boolean prevComma) {
    // has "kon" parent and "cj" child
    // if 'prevComma' is set consider 'word' as "cj" child
    WordObj parent = graph.getParent(word, "kon");
    WordObj child = prevComma ? word : graph.getChild(word, "cj");
    if (parent == null || child == null) {
      return;
    }

    // We have a potential relation source (parent) and target (child).
    // Verify they are of equal part of speech.
    if (!parent.getPos().equals(child.getPos())) {
      return;
    }

    WordObj from = child, to = child;
    String label;

    // are parent and child "ADJA"
    if (parent.getPos().equals("ADJA")) {
      label = "attr";

      from = graph.getParent(parent, label);
      if (from == null) {
        return;
      }

    } else
    // are parent and child either "ADJD", "VVFIN" or "VVPP"
    if (child.getPos().equals("VVFIN") || child.getPos().equals("ADJD") || child.getPos().equals("VVPP")) {

      // if a passive normalization took place: add additional edges for "obja"
      // (sentiment targets)
      if (child.getPos().equals("VVPP") && graph.ap) {
        normalizeConjunctionPassive(graph, parent, child);
      }

      label = "subj";

      // Does the child already belong to another parent?
      // This may be the case in subordinate clause constructions:
      //
      //   "Er lief und sie fuhr zur Arbeit."
      //
      // "lief" is in relation with "er" and "fuhr" is in relation with "sie".
      // If not handled "fuhr" would also be in relation with "er" which is
      // clearly unintended.
      if (graph.getChild(child, label) != null) {
        return;
      }

      // does parent have an edge labeled "subj"
      to = graph.getChild(parent, label);
      if (to == null) {
        return;
      }

    } else {
      return;
    }

    // Finally, add the new relational edge.
    graph.addEdge(from, to, label);
  }

  /**
   * Checks whether the given word object is considered to be a conjunction
   * (i.e. is either "und", "oder" or "bzw.").
   *
   * Commas are not taken into account as they are handled separately.
   *
   * @param word The word object to be checked.
   *
   * @return true if the given word object is considered to be a conjunction.
   */
  private boolean isConjunction(WordObj word) {
    return (word.getName().equals("und") || word.getName().equals("oder") || word.getName().equals("bzw."));
  }

  /**
   * Normalizes the given graph by taking conjunctions into account. In
   * particular, it adds relational edges to the conjuncts such that all of them
   * are related as the first conjunct (only within "a single" (conceived)
   * conjunction). For this only the 'subj', 'attr' and 'obja' relations are
   * considered depending on the POS-tags of the conjuncts and whether or not
   * the sentence was normalized due to active and passive voice.
   *
   * These additional edges enable later steps to find opinion sources or
   * targets more reliably (frame construction).
   *
   * @param graph The graph to be normalized.
   *
   * @return the normalized graph.
   */
  public DependencyGraph normalizeConjunctions(DependencyGraph graph) {

    graph = new DependencyGraph(graph);

    boolean comma = false;

    // iterate over all words
    for (WordObj word : this.wordList) {
      // is 'word' a conjunction or was the last word object a comma
      if (isConjunction(word) || comma) {
        normalizeConjunction(graph, word, comma);
      }

      comma = false;

      // Commas are considered conjunctions as well.
      // They are treated specially as they are part of the word list but not
      // part of the tree generated by the parser.
      if (word.getName().equals(",")) {
        comma = true;
      }
    }

    return graph;
  }

  /**
   * Returns the list of WordObj nodes from the that are matched with the
   * mweWordsToBeMatched in lemma or name, and are chained with the startNode
   * (recursive function).
   *
   * @param startNode the node from which the chain search starts.
   * @param mweWordsToBeMatched is the list of strings/words that are to be
   * found/matched in the graph/tree.
   * @param topCall indicates if it is the top call of the recursive function or
   * not.
   * @return the list of connected WordObj nodes that are matched with the
   * mweWordsToBeMatched.
   */
  public ArrayList<WordObj> getMweMatches(WordObj startNode, ArrayList<String> mweWordsToBeMatched, boolean topCall) {
    ArrayList<String> mweWords = new ArrayList<String>(mweWordsToBeMatched);
    ArrayList<String> matchedMWEWords = new ArrayList<String>();
    // returns all WordObj nodes (also deleted ones) that match a string
    // list, and form a catenae (are all connected in the tree)
    ArrayList<WordObj> list = new ArrayList<>();
    list.add(startNode);

    // Base case of recursive function)
    if (mweWords.size() == 0) {
      return list;
    }

    // If the node itself is a deleted word, use the WordObj in which it is
    // contained instead of the word itself as start for searching.
    for (WordObj node : this.nodes) {
      if (node.getDeleted().contains(startNode)) {
        startNode = node;
      }
    }

    // If any of the deleted words are in the mweWords, delete it from the
    // still to find mweWords, and add the WordObj node to the list
    Iterator<String> itt = mweWords.iterator();
    ArrayList<WordObj> currentNodes = new ArrayList<WordObj>(startNode.getDeleted());
    currentNodes.add(startNode);
    while (itt.hasNext()) {
      String mweWord = itt.next();
      for (WordObj startNodeWord : currentNodes) {
        if (mweWord.equals(startNodeWord.getLemma()) || mweWord.equals(startNodeWord.getName())) {
          list.add(startNodeWord);
          matchedMWEWords.add(startNodeWord.getLemma());
          matchedMWEWords.add(startNodeWord.getName());
        }
      }
    }

    mweWords.removeAll(matchedMWEWords);
    ArrayList<String> toMatch = new ArrayList<String>(mweWords);

    // search down in the tree
    for (WordObj siblingNode : this.getTargets(startNode)) {
      Iterator<String> it = mweWords.iterator();
      while (it.hasNext()) {
        String mweString = it.next();
        // if one of the mweWords matches a siblingnode
        if (mweString.equals(siblingNode.getLemma()) || mweString.equals(siblingNode.getName())) {
          it.remove();
          toMatch.remove(mweString);
          list.addAll(this.getMweMatches(siblingNode, toMatch, false));
        }
      }
    }

    // search up in the tree
    for (WordObj parentNode : this.getSources(startNode)) {
      Iterator<String> it = mweWords.iterator();
      while (it.hasNext()) {
        String mweString = it.next();
        // if one of the mweWords matches a siblingnode
        if (mweString.equals(parentNode.getLemma()) || mweString.equals(parentNode.getName())) {
          it.remove();
          toMatch.remove(mweString);
          list.addAll(this.getMweMatches(parentNode, toMatch, false));
        }
      }
    }

    for (WordObj matchedWord : list) {
      toMatch.remove(matchedWord.getLemma());
      toMatch.remove(matchedWord.getName());
    }

    if (toMatch.size() != 0 && topCall && list.size() > (mweWordsToBeMatched.size())) {
      // in case a not all words are matched, but the size is still the
      // same
      list = removeDistantDuplicates(list, mweWordsToBeMatched.size() + 1);
    }
    if (list.size() > (mweWordsToBeMatched.size() + 1) && topCall) {
      // Remove duplicate matches, if there are too many matches.
      list = removeDistantDuplicates(list, mweWordsToBeMatched.size() + 1);
    }
    return list;
  }

  /**
   * This method is similar to
   * {@link #getSentimentSourceTarget(WordObj, String, WordObj)} except that it
   * deals with multi-word expressions (MWEs). It returns either the
   * corresponding source or the target (depending if it has been called to
   * identify source or target in SentimentChecker class) for a given sentiment
   * expression that is of the 'mwe' type.
   *
   * @param wtmp The word from which the MWE sentiment expression is recognized.
   * @param mwe The sentiment expression (mwe) that is matched.
   * @param label It should be checked whether the sentiment expression has an
   * outgoing node to another node (this would then be the source or the target)
   * with a certain dependency relation specified in this parameter
   * @param containsDeleted If there is a sentiment expression whose
   * corresponding node in the graph of the sentence has been deleted due to
   * normalization, the WordObj parent node (which is still in the graph,
   * necessary to yield existing graph relations) will be stored in this
   * parameter (empty otherwise)
   * @return a list in which the source or the target is stored, empty otherwise
   * if no fitting source/target exists for given label and sent expr
   */
  public ArrayList<WordObj> getSentimentSourceTargetMWE(WordObj wtmp, SentimentUnit mwe, String label,
          WordObj containsDeleted) {
    ArrayList<String> mweWords = new ArrayList<>();
    for (String word : mwe.collocations) {
      mweWords.add(word);
    }

    ArrayList<WordObj> list = new ArrayList<>();
    ArrayList<WordObj> matches = getMweMatches(wtmp, mweWords, true);
    if (!(matches.size() > mwe.collocations.length)) {
      return new ArrayList<WordObj>();
    }

    for (WordObj mweNode : matches) {
      list.addAll(this.getSentimentSourceTarget(mweNode, label, new WordObj("")));
    }

    // remove doubles
    HashSet<WordObj> hs = new HashSet<WordObj>();
    hs.addAll(list);
    list.clear();
    list.addAll(hs);

    return list;
  }

  /**
   * This method returns if a multi-word expression (MWE) is matched on a
   * particular word, ie. if the word is member of a connected subgraph of which
   * the nodes match the words of the MWE.
   *
   * @param wtmp The word from which the MWE could be evoked (lexicon entry).
   * @param mwe The MWE that is checked.
   * @return Boolean representing if the MWE is matched in the graph
   */
  public boolean mweMatch(WordObj wtmp, SentimentUnit mwe) {
    ArrayList<String> mweWords = new ArrayList<>();
    for (String word : mwe.collocations) {
      mweWords.add(word);
    }
    ArrayList<WordObj> matches = getMweMatches(wtmp, mweWords, true);

    boolean match = (matches.size() > (mwe.collocations.length));

    if (match) { // If the length is correct, double check if all words in
      // the mwe are in the matched objects
      mweWords.add(mwe.name);
      ArrayList<String> stringMatches = new ArrayList<String>();
      for (WordObj m : matches) {
        stringMatches.add(m.getName().toString());
        stringMatches.add(m.getLemma().toString());
      }
      for (String word : mweWords) {
        if (!stringMatches.contains(word)) {
          match = false;
        }
      }
    }

    return match;
  }

  /**
   * Removes from an array list of word objects those duplicates that lie
   * farthest (position in sentence) from the other (non-duplicate) words, as
   * long as the list does not become smaller than the minLength
   *
   * @param matches is the ArrayList from which duplicates are removed
   * @param minLength is the length that the matches should have at least. (So
   * if there are still duplicates, but the minLength is reached nothing is
   * removed)
   * @return ArrayList
   */
  public ArrayList<WordObj> removeDistantDuplicates(ArrayList<WordObj> matches, int minLength) {
    // First remove REAL duplicates (same position/wordObj):
    HashSet<WordObj> hs = new HashSet<WordObj>(matches);
    matches.clear();
    matches.addAll(hs);
    // Then remove string or lemma duplicates:
    ArrayList<WordObj> duplicates = getDuplicates(matches);

    if (duplicates.size() == 0) {
      return matches;
    }
    double avg = 0;

    // get average position of the non-duplicate words
    for (WordObj word : matches) {
      if (!(duplicates.contains(word))) {
        avg += (double) word.getPosition();
      }
    }
    if (avg == 0) { // if the matches only contain duplicates, don't remove
      // anything
      return matches;
    }
    avg = avg / ((double) (matches.size() - duplicates.size()));
    if (duplicates.size() > 0) {
      // get the farthest duplicate
      double max = Math.abs(duplicates.get(0).getPosition() - avg);
      WordObj maxDupl = duplicates.get(0);
      double dist;
      for (WordObj duplicate : duplicates) {
        dist = Math.abs(duplicate.getPosition() - avg);

        if (dist > max) {
          max = dist;
          maxDupl = duplicate;
        }
      }
      // remove the farthest duplicate
      matches.remove(maxDupl);

    }
    ArrayList<WordObj> otherduplicates = getDuplicates(matches);
    if (otherduplicates.size() == 0) {

      return matches;
    } else { // if there are other duplicate pairs, remove them
      if (matches.size() > minLength) {
        return removeDistantDuplicates(matches, minLength);
      } else {
        return matches;
      }
    }
  }

  /**
   * Returns two duplicate words (WordObj) that are found in matches (not more
   * than two!), if there are any duplicates.
   *
   * @param matches is the ArrayList in which two duplicates are searched for.
   * @return ArrayList of the duplicates, if they are found.
   */
  public ArrayList<WordObj> getDuplicates(ArrayList<WordObj> matches) {
    // ArrayList<WordObj> duplicates = new ArrayList<WordObj>();
    HashSet<WordObj> duplicates = new HashSet<WordObj>();
    for (WordObj word1 : matches) {
      for (WordObj word2 : matches) {
        if (word1 != word2) {
          if (word1.getLemma().equals(word2.getLemma()) || word1.getName().equals(word2.getName())) {
            duplicates.add(word1);
            duplicates.add(word2);
          }
        }
      }
    }
    // remove WordObj duplicates (same object)
    return new ArrayList<WordObj>(duplicates);
  }

  public String toString() {

    StringBuffer printer = new StringBuffer();
    for (Edge edge : this.edges) {
      printer.append("\n");
      printer.append(edge);
      printer.append("\t|\ts-lemma: " + edge.source.getLemma() + ",");
      printer.append(" t-lemma: " + edge.target.getLemma() + ",");
      printer.append(" s-del: " + edge.source.getDeleted() + ",");
      printer.append(" t-del: " + edge.target.getDeleted());
    }
    return printer.toString();
  }

  /**
   * Returns a string representation of all edges in the dependency graph. This
   * is done by calling the 'toString' method of 'Edge'. Each edge has its own
   * line in this representation.
   *
   * @return a string representation of all edges in the dependency graph.
   */
  public String stringifyEdges() {
    StringBuilder sb = new StringBuilder();

    for (Edge e : this.edges) {
      sb.append(e.toString());
      sb.append('\n');
    }

    return sb.toString();
  }

  /**
   * Returns a string representation of all word objects in the word list. They
   * are separated by a single space character.
   *
   * @return a string representation of all word objects in the word list.
   */
  public String stringifyWordList() {
    StringBuilder sb = new StringBuilder();

    String delim = "";
    for (WordObj o : this.wordList) {
      sb.append(delim);
      sb.append(o.toString());
      delim = " ";
    }

    return sb.toString();
  }

}
