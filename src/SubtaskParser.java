
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import salsa.corpora.elements.Fenode;
import salsa.corpora.elements.Frame;
import salsa.corpora.elements.Frames;
import salsa.corpora.elements.Graph;
import salsa.corpora.elements.Nonterminal;
import salsa.corpora.elements.Sentence;
import salsa.corpora.elements.Terminal;

/**
 * A class to find subjective expressions in a Salsa XML file specified by the 
 * SubjectiveExpressionLocationPath. The found SEs are saved in a HashMap and 
 * made accessible for the PresetSELocationModule.
 *
 * @author isha, mwolf
 */
public class SubtaskParser {

  SalsaAPIConnective salsa;
  SentenceList sentences = new SentenceList();
  /**
   * A HashMap containing the information of found SEs. 
   * <ID, <lemma,pos>> ...
   */
  Map<String, HashMap<String, String>> SEsFromInput = new HashMap<String, HashMap<String, String>>();

  /**
   * SubtaskParser constructor.
   *
   * @param salsa Parses a Salsa XML file which represents a corpus.
   * @param sentences A {@link SentenceList} containing all sentences of the XML
   * corpus as {@link SentenceObj}.
   */
  public SubtaskParser(SalsaAPIConnective salsa, SentenceList sentences) {
    this.salsa = salsa;
    this.sentences = sentences;
  }

  /**
   * Getter for the SEsFromInput HashMap containing SE info.
   *
   * @return the SEsFromInput HashMap.
   */
  public Map<String, HashMap<String, String>> getSEsFromInput() {
    return SEsFromInput;
  }

  /**
   * Collect the SEs from the input file. Identified SEs are stored in the 
   * HashMap SEsFromInput.
   */
  public void searchSEs() {
    System.out.println("Processing SubtaskParser...");
    System.out.println("Size of pure sentences: " + sentences.getSentenceList().size());
    ArrayList<Sentence> salsaSentences = salsa.getBody().getSentences();
    for (int i = 0; i < salsaSentences.size(); i++) {
      Graph graph = salsaSentences.get(i).getGraph();
      ConstituencyTree tree = new ConstituencyTree(graph);

//      System.out.println((i + 1) + " of " + salsaSentences.size());
      //section: <frames>
      ArrayList<Frames> subjFrames = salsaSentences.get(i).getSem().getFrames();
      for (int j = 0; j < subjFrames.size(); j++) {

        // One frame per subjective Expression
        ArrayList<Frame> subjFrame = subjFrames.get(j).getFrames();
        for (int h = 0; h < subjFrame.size(); h++) {
          boolean found = false;
          ArrayList<Fenode> feNodes = subjFrame.get(h).getTarget().getFenodes();
          //System.out.println("Number of feNodes: " + feNodes.size() + " if exceeding 1, it is a mwe");

          // One Mwe spanning over multiple feNodes per subjective expression 
          // target possible.
          String mwe = "";
          String mweWordId = "";
          boolean mwExpression = false;
          ArrayList<Terminal> tList = tree.getTerminals();

          // Iterate over fenodes.
          for (int fenodeCount = 0; fenodeCount < feNodes.size(); fenodeCount++) {
            // Iterate over terminals.
            for (int terminalCount = 0; terminalCount < tList.size(); terminalCount++) {
              Terminal terminal = tList.get(terminalCount);
              Nonterminal parent = tree.getParent(terminal);
              Nonterminal grandParent = tree.getParent(parent);

              String terminalId = terminal.getId().getId();
              String terminalParentId = parent.getId().getId();
              String terminalGrandParentId = new String();
              if (grandParent != null) {
                terminalGrandParentId = grandParent.getId().getId();
              }
              String subjId = feNodes.get(fenodeCount).getIdref().getId();

              boolean parentHit = false;
              boolean grandParentHit = false;

              // Search for word belonging to Id found in Subjective Expression 
              // in terminals or parents of terminals.
              if (terminalId.equals(subjId) || terminalParentId.equals(subjId)
                      || terminalGrandParentId.equals(subjId)) {
                parentHit = terminalParentId.equals(subjId);
                grandParentHit = terminalGrandParentId.equals(subjId);

//**********************************Case MWE************************************
                // In case there is more than one fenode for a SE, 
                // we are dealing with a mwe.
                // We could also have a mwe with just one feNode, if the feNode
                // is a nonTerminal like "PP". If this is the case, the idref
                // of the feNode looks like this: "s..._5.."
                if (feNodes.size() > 1 || feNodes.get(0).getIdref().getId().contains("_5")) {
                  boolean onlyOneNode = false;
                  if (feNodes.size() == 1 && feNodes.get(0).getIdref().getId().contains("_5")) {
                    onlyOneNode = true;
                  }
                  if (fenodeCount < feNodes.size()) {
                    if (grandParentHit) {
                      for (int p = 0; p < grandParent.getEdges().size(); p++) {
                        if (tree.getChildren(grandParent).get(p) instanceof Nonterminal) {
                          Nonterminal dad = (Nonterminal) tree.getChildren(grandParent).get(p);
                          for (int t = 0; t < dad.getEdges().size(); t++) {
                            if (tree.getChildren(dad).get(t) instanceof Terminal) {
                              Terminal ter = (Terminal) tree.getChildren(dad).get(t);
                              mwe += ter.getWord() + "_";
                              mweWordId += Integer.toString(terminalCount) + "§";
                              terminalCount++;
                            }
                          }
                        } else if (tree.getChildren(grandParent).get(p) instanceof Terminal) {
                          Terminal ter = (Terminal) tree.getChildren(grandParent).get(p);
                          mwe += ter.getWord() + "_";
                          mweWordId += Integer.toString(terminalCount) + "§";
                          terminalCount++;
                        }
                      }
                    } else if (parentHit) {
                      for (int t = 0; t < parent.getEdges().size(); t++) {
                        if (tree.getChildren(parent).get(t) instanceof Terminal) {
                          Terminal ter = (Terminal) tree.getChildren(parent).get(t);
                          mwe += ter.getWord() + "_";
                          mweWordId += Integer.toString(terminalCount) + "§";
                          terminalCount++;
                        } else if (tree.getChildren(parent).get(t) instanceof Nonterminal) {
                          Nonterminal dad = (Nonterminal) tree.getChildren(parent).get(t);
                          for (int p = 0; p < dad.getEdges().size(); p++) {
                            if (tree.getChildren(dad).get(p) instanceof Terminal) {
                              Terminal ter = (Terminal) tree.getChildren(dad).get(p);
                              mwe += ter.getWord() + "_";
                              mweWordId += Integer.toString(terminalCount) + "§";
                              terminalCount++;
                            }
                          }
                        }
                      }
                    } else {
                      mwe += terminal.getWord() + "_";
                      mweWordId += Integer.toString(terminalCount) + "§";
                    }
                    mwExpression = true;
                    if (!onlyOneNode && !(fenodeCount == feNodes.size() - 1)) {
                      continue;
                    }
                  }
                }

                if (mwExpression && !(mwe.startsWith("_"))) {
                  // Delete the last characters of mwe and mweWordId as they are 
                  // just seperators '_' and '§'.
                  if (mwe.length() > 0 && mwe.charAt(mwe.length() - 1) == '_') {
                    mwe = mwe.substring(0, mwe.length() - 1);
                  }
                  if (mweWordId != null && mweWordId.length() > 0 && mweWordId.charAt(mweWordId.length() - 1) == '§') {
                    mweWordId = mweWordId.substring(0, mweWordId.length() - 1);
                  }
                  // Add SE information to the SEsFromInput HashMap.
                  HashMap<String, String> lemmaPos = new HashMap<String, String>();
                  lemmaPos.put(mwe, "MWE");

                  // Multiple SEs on the same word case:
                  boolean setDuplicateWord = false;
                  while (setDuplicateWord == false) {
                    if (SEsFromInput.containsKey(Integer.toString(i) + "_" + mweWordId)) {
                      mweWordId = mweWordId + "#";
                    } else {
                      setDuplicateWord = true;
                    }
                  }

//                    System.out.println("mwe final: " + mwe);
//                    System.out.println("mweWordId final: " + mweWordId);
                  SEsFromInput.put(Integer.toString(i) + "_" + mweWordId, lemmaPos);
                }

//**********************************CASE NON MWE********************************
                if (mwExpression == false) {
                  // All Subjective Expressions not recognised here could be 
                  // unrecognised because they are not lemmatized. 
                  // Therefore search for the lemma form.
                  String lemma = this.searchLemma(Integer.toString(i), terminal.getWord());
                  if (!(lemma == null)) {
                    HashMap<String, String> lemmaPos = new HashMap<String, String>();
                    lemmaPos.put(lemma, terminal.getPos());

                    String wordId = Integer.toString(terminalCount);

                    // Multiple SEs on the same word case:
                    boolean setDuplicateWord = false;
                    while (setDuplicateWord == false) {
                      if (SEsFromInput.containsKey(Integer.toString(i) + "_" + wordId)) {
                        wordId = wordId + "#";
                      } else {
                        setDuplicateWord = true;
                      }
                    }
                    SEsFromInput.put(Integer.toString(i) + "_" + wordId, lemmaPos);
                  }
                }
                found = true;
                break;
              }
            }
            if (found) {
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Searches for the fitting lemma of a word.
   *
   * @param Id the Id of the word.
   * @param word the unlemmatized word.
   * @return the lemma if found, else null.
   */
  public String searchLemma(String Id, String word) {
    try {
      //Must differentiate cases where the Id contains an "s" Example: s140_2
      //and where the id is directly given like: 130_6.
      if (Id.startsWith("s")) {
        Id = Id.substring(1);
      }
      //Ignore cases where Id in Salsa Xml exceeds number of sentences
      if (!((Integer.parseInt(Id)) >= sentences.getSentenceList().size())) {
        SentenceObj sObj = sentences.getSentenceList().get(Integer.parseInt(Id));
        LinkedList<WordObj> wordList = sObj.getWordList();
        for (int i = 0; i < wordList.size(); i++) {
          if (wordList.get(i).getName().equals(word)) {
            String lemma = wordList.get(i).getLemma();
            return lemma;
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      String infoMessage = "Mismatch in sentences Size and Salsa XML Sentences Size";
      System.out.println(infoMessage);
    }
    return null;
  }
}
