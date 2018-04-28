
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import salsa.corpora.elements.Fenode;
import salsa.corpora.elements.Flag;
import salsa.corpora.elements.Frame;
import salsa.corpora.elements.FrameElement;
import salsa.corpora.elements.Nonterminal;
import salsa.corpora.elements.Target;
import salsa.corpora.elements.Terminal;
import salsa.corpora.noelement.Id;

/**
 * This module is used to set frames and detect sources/targets in the case that
 * the locations of subjective expressions are known and can be found in a 
 * Salsa XML file.
 * Sources and targets are found by either using default rules or lexicon rules,
 * depending on the config value of ignoreLexicon.
 * 
 * This module includes the functionality of the other two modules and should 
 * thus never be used in conjunction with them.
 * Consequently, the options for the other two modules are relevant for the
 * preset SE location module as well.
 *
 * @author isha, mwolf
 */
public class PresetSELocationModule implements Module {

  private Map<String, HashMap<String, String>> SEsFromInput = new HashMap<>();
  private SentenceList sentences = new SentenceList();
  private final SentimentLex lex;
  private final boolean findSources;
  private final boolean findTargets;
  private final boolean ignoreLexicon;
  private final Map<String, String[]> sources = new HashMap<>();
  private final Map<String, String[]> targets = new HashMap<>();
  private final GrammarInducedModule giModule;

  //Check whether a source/target has been found.
  private boolean found = false;

  /**
   * Constructor for PresetSELocationModule.
   *
   * @param SEsFromInput List of SEs collected from the SubtaskParser.
   * @param sentences The sentences from the rawSentences file.
   * @param lex The sentiment lexicon.
   * @param findSources Determines whether to look for sources.
   * @param findTargets Determines whether to look for targets.
   * @param ignoreLexicon Determines whether to consider the sentiment lexicon.
   * @param giModule GrammarInducedModule is used to check for auxilary and
   * modal verbs and find correct sources and targets for these words. The
   * reason PresetSELocationModule uses methods from GrammarInducedModule,
   * instead of using both modules at the same time, is to prevent the setting
   * of multiple Frames for one SE.
   */
  public PresetSELocationModule(Map<String, HashMap<String, String>> SEsFromInput,
          SentenceList sentences, SentimentLex lex, boolean findSources,
          boolean findTargets, boolean ignoreLexicon, Module giModule) {
    this.SEsFromInput = SEsFromInput;
    this.sentences = sentences;
    this.lex = lex;
    this.findSources = findSources;
    this.findTargets = findTargets;
    this.ignoreLexicon = ignoreLexicon;
    this.giModule = (GrammarInducedModule) giModule;
//    if (ignoreLexicon == false) {
//      System.out.println("Alpenschutz ein sentiment: " + lex.getSentiment("Alpenschutz").toString());
//      System.out.println("Alpenschutz+ ein sentiment: " + lex.sentimentMap.get("Alpenschutz+"));
//      System.out.println("Alpenschutz all sentiments: " + lex.getAllSentiments("Alpenschutz").toString());
//    }
  }

  /**
   * Finds and sets frames for SEs and optionally looks for sources and targets.
   * Works with the SEs from the input from the
   * SubjectiveExpressionLocationPath.
   *
   * @param sentence The {@link SentenceObj} which is to be checked for
   * sentiment expressions.
   * @return set frames.
   */
  @Override
  public Collection<Frame> findFrames(SentenceObj sentence) {

    int hitcount = 0;
    int fecount = 0;
    int multiFrameCounter;

    ConstituencyTree tree = sentence.getTree();
    DependencyGraph graph = sentence.getGraph();
    final Collection<Frame> frames = new ArrayList<>();

    // Iterate over all sentences and find the corresponding WordObjs.
    for (Entry<String, HashMap<String, String>> entry
            : SEsFromInput.entrySet()) {
      String entryId = entry.getKey();
      //entryId example: 311_83

      //multiple SEs on the same entry case:
      if (entry.getKey().contains("#")) {
        entryId = entry.getKey().replaceAll("#", "");
      }

      /**
       * A list of every wordId of a mwe.
       */
      List<String> mweIds = new LinkedList<>();
      //Get the Ids of every mwe-part.
      //entryId example (sentenceID_wordId§wordId§...): 330_31§32§33
      if (entry.getKey().contains("§")) {
        String wordIds = entryId.substring(entryId.indexOf("_") + 1, entryId.length());
        //wordIds: 31§32§33
        mweIds.addAll(Arrays.asList(wordIds.split("§")));
      }

      // Use Id of first mwe-part in case of mwe to look for the matching sentence.
      if (entry.getKey().contains("§")) {
        entryId = entryId.substring(0, entryId.indexOf("§"));
      }

      // The corresopnding sentence Id to the currently looked at SE.
      String sentenceId = entryId.substring(0, entryId.indexOf("_"));
      //sentenceID example: 83
      //System.out.println("sentenceID: " + sentenceId); 

      // Find the sentence in the SentenceList matching the current sentenceId.
      SentenceObj toAnalyse = searchMatchingSentence(sentences, sentenceId);
      if (toAnalyse != sentence) {
        continue;
      }

      HashMap<String, String> neu = entry.getValue();
      //neu example: {Preisbindung=ADJA}
      //neu example: {setze_ein=MWE}

      for (Entry<String, String> elem : neu.entrySet()) {
        //elem example: Preisbindung=NN
        //elem example: setze_ein=MWE
        //elem key + value: argumentieren + NE
        //elem key + value: vorsehen_vor + MWE
//        System.out.println("elem: " + elem + " id: " + entryId + " entry.getKey() " + entry.getKey());

        multiFrameCounter = entry.getKey().length() - entry.getKey().
                replaceAll("#", "").length();
        // The WordObj corresponding to the SE in the current sentence.
        WordObj word = findWordObj(toAnalyse, entryId);
//        System.out.println("word: " + word);

        // The String corresponding to the SE in the current sentence.
        String wordStr = elem.getKey();

        // In case of mutliple Frames for one SE the multiFrameCounter equals
        // the iteration of the Frames.
        // MultiFrameCounter 0: Check the first rule
        // MultiFrameCounter 1: Check the second rule...
        if (ignoreLexicon == false) {
          if (findSources) {
            if (lex.getAllSentiments(wordStr) != null
                    && lex.getAllSentiments(wordStr).size() > multiFrameCounter) {
              String[] sourcesFromAdditionalRule = lex.getAllSentiments(wordStr).get(multiFrameCounter).getSources();
              if (sourcesFromAdditionalRule != null) {
                sources.put(wordStr, sourcesFromAdditionalRule);
              }
            } else {
              sources.remove(wordStr);
            }
          }
          if (findTargets) {
            if (lex.getAllSentiments(wordStr) != null
                    && lex.getAllSentiments(wordStr).size() > multiFrameCounter) {
              String[] targetsFromAdditionalRule = lex.getAllSentiments(wordStr).get(multiFrameCounter).getTargets();
              if (targetsFromAdditionalRule != null) {
                targets.put(wordStr, targetsFromAdditionalRule);
              }
            } else {
              targets.remove(wordStr);
            }
          }
        }
        // Start constructing frames for SEs.
        hitcount++;
        String idstr = sentenceId + "_l" + hitcount;
        Id id = new Id(idstr);
        final Frame frame = new Frame("SubjectiveExpression", id);
        final FrameElementIds feIds = new FrameElementIds(frame);

        Target target = new Target();
        Id targetId = sentence.tree.getTerminal(word).getId();

        Fenode targetNode = new Fenode(targetId);
        target.addFenode(targetNode);
        frame.setTarget(target);

//*******************************Case MWE***************************************
        if (wordStr.contains("_")) {
          SentimentUnit newSentiment = new SentimentUnit(wordStr, "mwe");
          newSentiment.typ = "mwe";
          //possible lexicon addition
          //this.lex.addSentiment(newSentiment);

          // Add Frames for every part of a mwe.
          Id targetIdMWE;
          Fenode targetNodeMWE;

          /**
           * mweMatches is an ArrayList of WordObj containing every WordObj of a
           * mwe.
           */
          List<WordObj> mweMatches = new ArrayList<>();
          for (String mweId : mweIds) {
            WordObj mwePart = findWordObj(toAnalyse, mweId);
            mweMatches.add(mwePart);
          }

          for (WordObj match : mweMatches) {
            targetIdMWE = tree.getTerminal(match).getId();
            targetNodeMWE = new Fenode(targetIdMWE);
            target.addFenode(targetNodeMWE);
          }

          // Find sources for mwes using getSentimentSourceTargetMWE from DependencyGraph.
          if (findSources) {
            // If the SE has an entry in the Lexicon use the Lexicon rules
            // if ignoreLexicon is set to false.
            if (ignoreLexicon == false) {
              if (sources.containsKey(wordStr)) {
                // Use the rule found in the lexicon.
                for (String rule : sources.get(wordStr)) {
                  WordObj sourceCandidate;
                  if (!graph.getSentimentSourceTargetMWE(word, newSentiment,
                          rule, word.getDeleted().peekFirst()).isEmpty()) {
                    sourceCandidate = graph.getSentimentSourceTargetMWE(word,
                            newSentiment, rule, word.getDeleted().peekFirst()).get(0);
                    if (mweMatches.contains(sourceCandidate)) {
                      continue;
                    }
                    fecount = setSource(fecount, idstr, sentence, sourceCandidate, tree, word, frame);
                  }
                  //Stop looking for sources after one has been found.
                  if (found) {
                    found = false;
                    break;
                  }
                }
              }
            } // No lexicon entry available.
            if (!sources.containsKey(wordStr) && multiFrameCounter == 0) {
              if (getSourceCandidateDepRelList(toAnalyse, elem.getValue(),
                      wordStr, entryId) != null) {
                for (String sourcez : getSourceCandidateDepRelList(toAnalyse,
                        elem.getValue(), wordStr, entryId)) {
                  List<WordObj> sourceList = graph.getSentimentSourceTargetMWE(word,
                          newSentiment, sourcez, word.getDeleted().peekFirst());
                  for (WordObj tmpword : sourceList) {
                    if (mweMatches.contains(tmpword)) {
                      continue;
                    }
                    if (!tmpword.getPos().equals("PRF")) {
                      fecount = setSource(fecount, idstr, sentence, tmpword, tree,
                              word, frame);
                    }
                  }
                  //Stop looking for sources after one has been found.
                  if (found) {
                    found = false;
                    break;
                  }
                }
              }
            }
          }
          // Find targets for mwes using getSentimentSourceTargetMWE from DependencyGraph.
          if (findTargets) {
            // If the SE has an entry in the Lexicon use the Lexicon rules
            // if ignoreLexicon is set to false.
            if (ignoreLexicon == false) {
              if (targets.containsKey(wordStr)) {
                for (String rule : targets.get(wordStr)) {
                  WordObj targetCandidate;
                  if (!graph.getSentimentSourceTargetMWE(word, newSentiment, rule,
                          word.getDeleted().peekFirst()).isEmpty()) {
                    targetCandidate = graph.getSentimentSourceTargetMWE(word,
                            newSentiment, rule, word.getDeleted().peekFirst()).get(0);
                    if (mweMatches.contains(targetCandidate)) {
                      continue;
                    }
                    fecount = setTarget(fecount, idstr, sentence, targetCandidate,
                            tree, word, frame);
                  }
                  //Stop looking for targets after one has been found.
                  if (found) {
                    found = false;
                    break;
                  }
                }
              }
            } // No lexicon entry available. 
            if (!targets.containsKey(wordStr) && multiFrameCounter == 0) {
              if (getTargetCandidateDepRelList(toAnalyse, elem.getValue(),
                      wordStr, entryId) != null) {
                for (String targetz : getTargetCandidateDepRelList(toAnalyse,
                        elem.getValue(), wordStr, entryId)) {
                  List<WordObj> targetlist = graph.getSentimentSourceTargetMWE(
                          word, newSentiment, targetz, word.getDeleted().peekFirst());

//                  for(WordObj tgt:targetlist){
//                      System.out.println("Sentence: "+ sentence.id);
//                      System.out.println("Pred: " + wordStr);
//                      System.out.println("Target: " + tgt.getLemma());
//                      System.out.println();
//                  }
                  
                  for (WordObj tmpword : targetlist) {
                    if (mweMatches.contains(tmpword)) {
                    	found = false;
                      continue;
                    }
                    if (!tmpword.getPos().equals("PRF")) {
                      fecount = setTarget(fecount, idstr, sentence, tmpword,
                              tree, word, frame);
                    } else {
                    	found = false;
                    }
                  }
                  //Stop looking for targets after one has been found.
                  if (found) {
                    found = false;
                    break;
                  }
                }
              }
            }
          }
      

          
          frame.setTarget(target);
          frames.add(frame);
          continue;
        }

//*******************************Case NON MWE***********************************
        SentimentUnit newSentiment = new SentimentUnit(wordStr);

        // Find sources using getSentimentSourceTarget from DependencyGraph.
        if (findSources) {
          // If the SE has an entry in the Lexicon use the Lexicon rules
          // if ignoreLexicon is set to false.
          if (ignoreLexicon == false) {
            if (sources.containsKey(wordStr)) {
              // Use the rule found in the lexicon.
              for (String rule : sources.get(wordStr)) {
                WordObj sourceCandidate;
                if (!graph.getSentimentSourceTarget(word, rule).isEmpty()) {
                  sourceCandidate = graph.getSentimentSourceTarget(word, rule).get(0);
                  fecount = setSource(fecount, idstr, sentence, sourceCandidate,
                          tree, word, frame);
                }
                //Stop looking for sources after one has been found.
                if (found) {
                  found = false;
                  break;
                }
              }
            }
          } // No lexicon entry available:
          if (!sources.containsKey(wordStr) && multiFrameCounter == 0) {
            boolean author = false;
            if (elem.getValue().startsWith("ADJ") || giModule.isTrigger(word, sentence)) {
              toAnalyse.setSourceIsAuthor(true);
              newSentiment.setSource("author");
              author = true;

              final FrameElement sourceElement = new FrameElement(feIds.next(), "Source");
              sourceElement.addFlag(new Flag("Sprecher"));
              frame.addFe(sourceElement);
            }
            if (author == false) {
              if (getSourceCandidateDepRelList(toAnalyse, elem.getValue(),
                      wordStr, entryId) != null) {
                for (String sourcez : getSourceCandidateDepRelList(toAnalyse,
                        elem.getValue(), wordStr, entryId)) {
//                  System.out.println("sources: " + sources);
                  List<WordObj> sourceList = graph.getSentimentSourceTarget(word, sourcez);
//                  System.out.println("sourceList: " + sourceList);
                  for (WordObj tmpword : sourceList) {
                    fecount = setSource(fecount, idstr, sentence, tmpword, tree,
                            word, frame);
                  }
                  //Stop looking for sources after one has been found.
                  if (found) {
                    found = false;
                    break;
                  }
                }
              }
            }
          }
        }

        // Find targets using getSentimentSourceTarget from DependencyGraph.
        if (findTargets) {

          // If the SE has an entry in the Lexicon use the Lexicon rules
          // if ignoreLexicon is set to false.
          if (ignoreLexicon == false) {
            if (targets.containsKey(wordStr)) {
              for (String rule : targets.get(wordStr)) {
                WordObj targetCandidate;
                if (!graph.getSentimentSourceTarget(word, rule).isEmpty()) {
                  targetCandidate = graph.getSentimentSourceTarget(word, rule).get(0);
                  fecount = setTarget(fecount, idstr, sentence, targetCandidate,
                          tree, word, frame);
                }
                //Stop looking for targets after one has been found.
                if (found) {
                  found = false;
                  break;
                }
              }
            }
          } // No lexicon entry available:
          if (giModule.isTrigger(word, sentence) && multiFrameCounter == 0) {
            fecount++;
            Id sourceId = new Id(idstr + "_e" + fecount);
            final FrameElement targetElement = new FrameElement(sourceId, "Target");
            for (Object node : giModule.findTarget(word, sentence)) {
              targetElement.addFenode(new Fenode(ConstituencyTree.getNodeId(node)));
            }
            frame.addFe(targetElement);

          } else {
            if (!targets.containsKey(wordStr) && multiFrameCounter == 0) {
              if (getTargetCandidateDepRelList(toAnalyse, elem.getValue(), wordStr,
                      entryId) != null) {
                for (String targetCandidateDepRel : getTargetCandidateDepRelList(
                        toAnalyse, elem.getValue(), wordStr, entryId)) {
//                System.out.println("targetCandidateDepRel: " + targetCandidateDepRel);
                  List<WordObj> targetList = graph.getSentimentSourceTarget(word,
                          targetCandidateDepRel);
//                System.out.println("targetList: " + targetList);
                  for (WordObj tmpword : targetList) {
                    if (!tmpword.getPos().equals("PRF")) {
                      fecount = setTarget(fecount, idstr, sentence, tmpword, tree,
                              word, frame);
                    }
                  }
                  //Stop looking for targets after one has been found.
                  if (found) {
                    found = false;
                    break;
                  }
                }
              }
            }
          }
        }
        newSentiment.typ = "non-mwe";

        //in case of lexicon addition
        //this.lex.addSentiment(newSentiment);
        frames.add(frame);
      }
    }
//In case of lexicon addition.
//classic.setLex(lex);
    return frames;
  }

  /**
   * This method is used to set source frames.
   *
   * @param fecount feNode counter.
   * @param idstr sentenceId as String.
   * @param sentence the current sentence.
   * @param sourceCandidate a potential source WordObj.
   * @param tree the ConstituencyTree.
   * @param word the current word.
   * @param frame the frame to be set.
   * @return the updated fecount.
   */
  private int setSource(int fecount, String idstr, SentenceObj sentence,
          WordObj sourceCandidate, ConstituencyTree tree, WordObj word, final Frame frame) {
    fecount++;
    Id sourceId = new Id(idstr + "_e" + fecount);
    final FrameElement sourceElement = new FrameElement(sourceId, "Source");
    Id feNodeId = new Id(sentence.id.getId() + "_" + sourceCandidate.getPosition());
    Object obj = tree.getArgumentNode(word, sourceCandidate, null);
    if (obj instanceof Terminal) {
      feNodeId = ((Terminal) obj).getId();
    }
    if (obj instanceof Nonterminal) {
      feNodeId = ((Nonterminal) obj).getId();
    }
    Fenode feNode = new Fenode(feNodeId);
    sourceElement.addFenode(feNode);
    frame.addFe(sourceElement);
    found = true;
    return fecount;
  }

  /**
   * This method is used to set target frames.
   *
   * @param fecount feNode counter.
   * @param idstr sentenceId as String.
   * @param sentence the current sentence.
   * @param sourceCandidate a potential source WordObj.
   * @param tree the ConstituencyTree.
   * @param word the current word.
   * @param frame the frame to be set.
   * @return the updated fecount.
   */
  private int setTarget(int fecount, String idstr, SentenceObj sentence,
          WordObj targetCandidate, ConstituencyTree tree, WordObj word, final Frame frame) {
    fecount++;
    Id sourceId = new Id(idstr + "_e" + fecount);
    final FrameElement targetElement = new FrameElement(sourceId, "Target");
    Id feNodeId = new Id(sentence.id.getId() + "_" + targetCandidate.getPosition());
    Object obj = tree.getArgumentNode(word, targetCandidate, null);
    if (obj instanceof Terminal) {
      feNodeId = ((Terminal) obj).getId();
    }
    if (obj instanceof Nonterminal) {
      feNodeId = ((Nonterminal) obj).getId();
    }
    Fenode feNode = new Fenode(feNodeId);
    targetElement.addFenode(feNode);
    frame.addFe(targetElement);
    found = true;
    return fecount;
  }

  /**
   * Returns the matching sentence in the SentenceList given a sentenceId.
   *
   * @param sentences The complete sentence list.
   * @param sentenceId The Id of the sentence to be matched.
   * @return The matching sentence as SentenceObj.
   */
  public SentenceObj searchMatchingSentence(SentenceList sentences, String sentenceId) {
    for (int i = 0; i < sentences.getSentenceList().size(); i++) {
      if ((i) == Integer.parseInt(sentenceId)) {
        return sentences.getSentenceList().get(i);
      }
    }
    return null;
  }

  /**
   * Returns the fitting wordObj given a SentenceObj and a word Id as String.
   *
   * @param toAnalyse The sentenceObj representing the current sentence.
   * @param subjId The word's Id.
   * @return The wordObj corresponding to the subjId.
   */
  public WordObj findWordObj(SentenceObj toAnalyse, String subjId) {
    String myId;
    if (subjId.contains("_")) {
      myId = subjId.substring(subjId.indexOf("_") + 1);
    } else {
      myId = subjId;
    }
    int id = Integer.parseInt(myId);

//    System.out.println("wordList " + toAnalyse.getWordList());
//    System.out.println("word: " + toAnalyse.getWordList().get(id));
    return toAnalyse.getWordList().get(id);
  }

  /**
   * Get default rules for source candidates as a list of dependency relations.
   *
   * @param toAnalyse The sentenceObj representing the current sentence.
   * @param posSubjExpr The POS-Tag of the current SE-word.
   * @param nameSubjExpr The name of the current SE-word.
   * @param subjId The current word's Id.
   * @return A list of strings with dependency relations or null.
   */
  public List<String> getSourceCandidateDepRelList(SentenceObj toAnalyse,
          String posSubjExpr, String nameSubjExpr, String subjId) {

    List<String> depRelList = new LinkedList<>();

    if ("MWE".equals(posSubjExpr)) {
      depRelList.add("subj");
      return depRelList;
    }

    if (posSubjExpr.startsWith("N")) {

      depRelList.add("det");
      depRelList.add("gmod");
      return depRelList;
    }

    if (posSubjExpr.startsWith("V")) {

      depRelList.add("subj");
      return depRelList;
    }
    return null;
  }

  /**
   * Get default rules for target candidates as a list of dependency relations.
   *
   * @param toAnalyse The sentenceObj representing the current sentence.
   * @param posSubjExpr The POS-Tag of the current SE-word.
   * @param nameSubjExpr The name of the current SE-word.
   * @param subjId The current word's Id.
   * @return A list of strings with dependency relations or null.
   */
  public List<String> getTargetCandidateDepRelList(SentenceObj toAnalyse,
          String posSubjExpr, String nameSubjExpr, String subjId) {

    List<String> depRelList = new LinkedList<>();
    if ("MWE".equals(posSubjExpr)) {
      depRelList.add("objd");
      depRelList.add("obja");
      depRelList.add("objc");
      depRelList.add("obji");
      depRelList.add("s");
      depRelList.add("objp-*");
      return depRelList;
    }

    if (posSubjExpr.startsWith("N")) {
      depRelList.add("objp");
      return depRelList;
    }

    if (posSubjExpr.startsWith("ADJ")) {
      depRelList.add("attr-rev");
      depRelList.add("subj");
      return depRelList;
    }

    if (posSubjExpr.startsWith("V")) {
      depRelList.add("objd");
      depRelList.add("obja");
      depRelList.add("objc");
      depRelList.add("obji");
      depRelList.add("s");
      depRelList.add("objp-*");
      return depRelList;
    }
    return null;
  }
}
