
import de.tuebingen.uni.sfs.germanet.api.ConRel;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.api.Synset;
import salsa.corpora.elements.*;
import salsa.corpora.noelement.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author Katja König ("ADJD bug" fixed)
 *
 */
public class ClassicModule implements Module {

  private SentimentLex lex;
  private final GermaNet germaNet;
  private final boolean personCheck;
  private final boolean findSources;
  private final boolean findTargets;

  //to check if there is WordObj that would be tagged with both source and target
//  private WordObj doublecheck = null;

  /**
   * Constructs a new {@link ClassicModule} object and instantiates a GermaNet
   * object. Sentiment sources are filtered based on whether they are a person
   * or a group of persons.
   *
   * @param lex A {@link SentimentLex} object in which the rules for finding
   * sentiment sources and targets of a sentiment word are saved as
   * {@link SentimentUnit}s.
   * @param personCheck A variable that, if set to "True", will filter out
   * sentiment sources which are not a person or group of persons.
   * @param findSources Only if this is set to "True", will the system look for
   * sentiment sources.
   * @param findTargets Only if this is set to "True", will the system look for
   * sentiment targets.
   * @param germaNet A {@link GermaNet} object
   */
  public ClassicModule(SentimentLex lex, boolean personCheck, boolean findSources, boolean findTargets, GermaNet germaNet) {
    this.lex = lex;
    this.personCheck = personCheck;
    this.germaNet = germaNet;
    this.findSources = findSources;
    this.findTargets = findTargets;
  }

  public ClassicModule(SentimentLex lex, boolean personCheck, boolean findSources, boolean findTargets) {
    this(lex, personCheck, findSources, findTargets, null);
  }


  /**
   * This method checks the existence of a hypernym-relation between the synset
   * human / person respectively the synset group and the synset that contains
   * the word.
   *
   * @param word The word that is examined whether it designates a person or a
   * group of persons.
   * @return True if one of the synsets with human / person or group is in a
   * hypernym-relation to the synset that contains the word, false otherwise.
   * <p/>
   * Uses these GermaNet API classes
   * (http://www.sfs.uni-tuebingen.de/GermaNet/documents/api/javadoc8.0/index.html):
   * @see de.tuebingen.uni.sfs.germanet.api.GermaNet
   * @see de.tuebingen.uni.sfs.germanet.api.Synset
   * @see de.tuebingen.uni.sfs.germanet.api.ConRel
   */
  private boolean isPerson(WordObj word) {

    String lemma = word.getLemma();
    if (word.getName().equals("man")) {
      return true;
    }

    if (this.germaNet != null) {

      List<Synset> synsets;
      synsets = this.germaNet.getSynsets(lemma);

      for (Synset synset : synsets) {
        List<List<Synset>> hypernyms = synset.getTransRelatedSynsets(ConRel.has_hypernym);
        for (List<Synset> hypernym : hypernyms) {
          for (Synset s : hypernym) {
            // ID for human / person, not living being in general and ID for group
            if (s.getId() == 34063 || s.getId() == 22562) {
              return true;
            }
          }

        }
      }
      return false;
    }
    return false;
  }

  /**
   * Checks whether a potential person is denoted by a personal pronoun.
   *
   * @param word The word that is examined whether it designates a personal
   * pronoun.
   * @return True if the word is an element of the list with the personal
   * pronouns, else false.
   */
  private boolean isPersonalPronoun(WordObj word) {

    String lemma = word.getLemma();

    ArrayList<String> personalPronouns = new ArrayList<String>();
    personalPronouns.add("ich");
    personalPronouns.add("du");
    personalPronouns.add("er");
    personalPronouns.add("sie");
    // personalPronouns.add("es"); // remove 'es' because of grammatical use where 'es' is not a person
    personalPronouns.add("wir");
    personalPronouns.add("ihr");
    personalPronouns.add("sie");
    personalPronouns.add("Sie");

    if (personalPronouns.contains(lemma)) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Checks whether a potential person is denoted by a relative pronoun.
   *
   * @param word The word that is examined whether it designates a relative
   * pronoun.
   * @return True if the POS of the word indicates a relative pronoun that is
   * not neuter, else false.
   */
  private boolean isRelativePronoun(WordObj word) {

    String pos = word.getPos();
    String lemma = word.getLemma();

    if (pos.equals("PRELS") && !lemma.equals("welches") && !lemma.equals("das")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Looks at a single {@link SentenceObj} to find any sentiment expressions and
   * adds the information to the Salsa XML structure. Multiple frames per
   * sentiment expression are possible, multiple sources and targets are not. We
   * filter out a sentiment target if its head is identical to the head of the
   * already tagged sentiment source. If the variable PersonCheck is set to
   * "True" in the configuration file, we filter out sentiment sources which are
   * none of the following: 1) a {@link NamedEntity} which is a person or an
   * organization 2) a proper noun referring to a person or group of persons
   * (see {@link #isPerson(WordObj)}) 3) a personal or relative pronoun which is
   * not of neuter gender
   *
   * @param sentence The {@link SentenceObj} which is to be checked for
   * sentiment expressions.
   */
  public Collection<Frame> findFrames(SentenceObj sentence) {

    int hitcount = 0;
    DependencyGraph graph = sentence.graph;
    ConstituencyTree tree = sentence.tree;

    final Collection<Frame> sentFrames = new ArrayList<Frame>();
    NamedEntityList namedEntityList = sentence.getNamedEntityList();

    // ADDED last fix
    HashSet<WordObj> wordsMatchedByMWE = new HashSet<WordObj>();
    // System.out.println("SENTENCE:" + sentence.toString());
    // System.out.println("GRAPH:" + graph.toString());
    for (WordObj node : graph.getNodes()) {

      //nodes which have been deleted through normalization can still function as sentiment expressions
      WordObj containsDeleted = new WordObj("");
      ArrayList<WordObj> deletedWords = new ArrayList<WordObj>();
      deletedWords.add(node);

      for (WordObj deleted : node.getDeleted()) {
        deletedWords.add(deleted);
      }

      for (WordObj wtmp : deletedWords) {
        // String lemma = wtmp.getLemma(); // REMOVED

        // Adverbs (ADV) can never be subjective expressions. Thus they cannot
        // be part of a frame and are dismissed.
        //
        // Predicative adjectives (ADJD) can be opinion predicate candidates.
        // But they may also be considered adverbs in certain cases. Hence they
        // were originally dismissed. This leads to problems with "regular"
        // adjectives as there are no frames constructed for them.
        if (!((wtmp.getPos()
                .equals("ADV")) /* || (wtmp.getPos().equals("ADJD")) */)) {

          ArrayList<String> wordAndLemma = new ArrayList<String>(); // ADDED
          wordAndLemma.add(wtmp.getLemma()); // ADDED
          // wordAndLemma.add(wtmp.getName());
          if (!(wtmp.getName().equals(wtmp.getLemma()))) {
            wordAndLemma.add(wtmp.getName()); // ADDED
          }
          for (String lemma : wordAndLemma) { // ADDED
            while (lex.sentimentMap.containsKey(lemma)) {
              if (!(wtmp.equals(deletedWords.get(0)))) {
                containsDeleted = deletedWords.get(0);
              }

              SentimentUnit utmp = lex.sentimentMap.get(lemma);
              // System.out.println("checking:" + utmp.toString());

              // If the sentimentunit is a mwe, and it is not matched: continue
              // the next loop
              if (utmp.typ.equals("mwe") && !graph.mweMatch(wtmp, utmp)) {
                lemma = lemma + "+";
                // System.out.println("not matched:"+utmp.toString());
                continue;
              }
              hitcount++;
              String idstr = sentence.id.getId() + "_f" + hitcount;
              Id id = new Id(idstr);
              int fecount = 0;
              Frame sentFrame = new Frame("SubjectiveExpression", id);

              Target target = new Target();
              Id targetId = tree.getTerminal(wtmp).getId();

              Fenode targetNode = new Fenode(targetId);
              target.addFenode(targetNode);

              // NEW
			  boolean passedQuickFix = true;
			  			  
              // In case of mwe: add all collocations to the sentimentexpression
              // xml/frame
              if (utmp.typ.equals("mwe")) {
                ArrayList<WordObj> matches = graph.getMweMatches(wtmp,
                        new ArrayList<String>(Arrays.asList(utmp.collocations)), true);
                
                //NEW
				if(!(isMWEWellformed(matches,graph))){
					passedQuickFix = false;
				}
				
                Id targetIdMWE;
                Fenode targetNodeMWE;
                for (WordObj match : matches) {
                  targetIdMWE = tree.getTerminal(match).getId();
                  targetNodeMWE = new Fenode(targetIdMWE);
                  target.addFenode(targetNodeMWE);
                }
              }

              // add extra Fenode for particle of a verb if existent
              if (wtmp.getIsParticleVerb()) {
                WordObj particle = wtmp.getParticle();
                Id particleId = tree.getTerminal(particle).getId();
                Fenode particleNode = new Fenode(particleId);
                target.addFenode(particleNode);
              }

              sentFrame.setTarget(target);

              //find sources and set Fe nodes in xml output
              if (findSources) {
                setSources(utmp, graph, wtmp, containsDeleted, fecount, idstr, sentence, tree, sentFrame, namedEntityList);
              }

              // find targets and set Fe nodes in xml output
              if (findTargets) {
                setTargets(utmp, graph, wtmp, containsDeleted, fecount, idstr, sentence, tree, sentFrame);
              }
              
            //NEW
			if(passedQuickFix){
				sentFrames.add(sentFrame);
			}
				
              lemma = lemma + "+";
            }
          } // END for wordAndLemma
        }
      }
    }

    // Remove non-MWE expressions that are matching a word that was also matched
    // by a MWE.
    HashSet<String> takenPositions = new HashSet<String>();
    for (Iterator<Frame> it = sentFrames.iterator(); it.hasNext();) {
      Frame f = it.next();
      if (f.getTarget().getFenodes().size() > 1) { // in case of a MWE
        for (Fenode targetPos : f.getTarget().getFenodes()) {
          takenPositions.add(targetPos.toString());
        }
      }
    }
    HashSet<Frame> toBeRemoved = new HashSet<Frame>();
    for (Iterator<Frame> it = sentFrames.iterator(); it.hasNext();) {
      Frame f = it.next();
      if (f.getTarget().getFenodes().size() == 1) { // in case of NO MWE
        if (takenPositions.contains(f.getTarget().getFenodes().get(0).toString())) {
// if the expression is at the same position as a MWE-word.
          toBeRemoved.add(f);
        }
      }
    }
    for (Frame tbrFrame : toBeRemoved) { // Remove what has to be removed
      sentFrames.remove(tbrFrame);
    }

    return sentFrames;
  }

	/**
	 * This method is a quick-fix-method to correctly match MWEs being
	 * either light verb constructions (LVCs) or reflexive verbs (RVs):;
	 * the actual MWE matching is very flexible with matching tokens in
	 * a sentence; for LVCs and RVs this often results in false positive;
	 * this method tries to detect these false positives;
	 * For LVC: the noun must be an accusative object (obja) of the light verb.
	 * For RVs: we check whether a pronoun is actually a reflexive pronoun.
	 * 
	 * @param matches words comprising an MWE that are to be checked for well-formedness
	 * @param graph dependency gram
	 * 
	 * @return boolean value confirming or rejecting well-formedness
	 */
	
	private boolean isMWEWellformed(List<WordObj> matches,DependencyGraph graph){
		boolean isWellFormed = true;

		//if((matches.size()==2)||(matches.size()==3)){
		if((matches.size()==2)||((matches.size()==3) && isOneTokenIsParticle(matches))){
			// is there is a MWE with a light verb
			// one other token of the MWE must be its accusative object
			// i.e. "obja"
			WordObj lv = getLightVerbTokenFromList(matches);
//			for(WordObj match:matches){
//				System.out.println("WO: " + match);
//			}
			if(lv != null){
				

				
				isWellFormed = false;
				for(WordObj match:matches){
					if(!(match.getLemma().equals(lv.getLemma()))){
						for(Edge edge:graph.getEdges()){
							if(edge.source.getName().equals(lv.getName()) &&
									edge.target.getName().equals(match.getName())){
								String relation = edge.depRel;
								if(relation.equals("obja")){
									isWellFormed = true;
									break;
								}
							}
						}
					}
				}
				

			}
			
			// if one token is a pronoun, it must also be a reflexive pronoun;
			// currently the matching procedure also allows other personal pronouns
			// which overgenerates MWEs
			boolean sawPRF = false;
			boolean sawOtherPersonalPron = false;
			for(WordObj match:matches){
				if(match.getPos().equals("PPER")){
					sawOtherPersonalPron = true;
					//System.out.println("HERE");
				} else if(match.getPos().equals("PRF")){
					sawPRF = true;
				}
			}
			
			if(sawOtherPersonalPron && !sawPRF){
//				System.out.println("malformed reflexive");
//				System.out.println();
				isWellFormed = false;
			} else {
//				System.out.println("wellformed reflexive");
//				System.out.println();
			}
			
		}

		return isWellFormed;
	}
	
	
	/***
	 * Checks whether a list of words contains a particle
	 * 
	 * @param the list of words to be inspected for containing a particle
	 * 
	 * @return boolean value confirming or rejecting the presence of a particle
	 */
	private boolean isOneTokenIsParticle(List<WordObj> matches){
		boolean isParticle = false;
		
		for(WordObj match:matches){
			if(match.getPos().equals("PTKVZ")){
				isParticle = true;
				break;
			}
		}
		
		return isParticle;
	}
	
	
	/***
	 * Checks whether a list of words contains a light verb;
	 * the list of light verbs is hard encoded in this method
	 * 
	 * @param the list of words to be inspected to be a light verb
	 * 
	 * @return the word object representing the light verb; if no such verb could be found
	 * {@code null} is returned
	 */
	private WordObj getLightVerbTokenFromList(List<WordObj> matches){
		WordObj lv = null;
		for(WordObj match:matches){
			if(match.getLemma().equals("haben")||match.getLemma().equals("machen")||
					match.getLemma().equals("bringen")||match.getLemma().equals("geben")||
					match.getLemma().equals("nehmen")||match.getLemma().equals("bekommen")||
					match.getLemma().equals("finden")||match.getLemma().equals("kommen")){
				lv = match;
				break;
			}
		}
		return lv;
	}


  
  
  /**
   * Used in {@link #findFrames(SentenceObj)} to add FrameElements for every
   * target.
   *
   * @param utmp
   * @param graph
   * @param wtmp
   * @param containsDeleted
   * @param doublecheck
   * @param fecount
   * @param idstr
   * @param sentence
   * @param tree
   * @param sentFrame
   */
  private void setTargets(SentimentUnit utmp, DependencyGraph graph, WordObj wtmp, WordObj containsDeleted, int fecount, String idstr, SentenceObj sentence, ConstituencyTree tree, Frame sentFrame) {
    if (utmp.target != null) {
      for (String targets : utmp.target) {
        //mwe:
        ArrayList<WordObj> targetlist = new ArrayList<WordObj>();
        if (utmp.typ.equals("mwe")) {
          //System.out.println("getSSTMWE(" + wtmp +"," + utmp +"," + targets +","+ containsDeleted+")");
          targetlist = graph.getSentimentSourceTargetMWE(wtmp, utmp, targets, containsDeleted);
          //System.out.println("Utmp:" + utmp.toString());
          //System.out.println("targetlist:" + targetlist.toString());
        } else {
          targetlist = graph.getSentimentSourceTarget(wtmp, targets, containsDeleted);
        }

        for (WordObj tmpword : targetlist) {
          //System.out.println("tg:"+tmpword + tmpword.getPos());
          if (!tmpword.getPos().equals("PRF")) {
            //if the object is already tagged as a source of this sentiment we don't tag it to be the target
            if (targetlist.size() > 0) {

//              if (targetlist.get(0) == doublecheck) {
//                break;
//              }
            }

            fecount++;
            Id feId = new Id(idstr + "_e" + fecount);
            FrameElement sourceFe = new FrameElement(feId, "Target");
            Id feNodeId = new Id(sentence.id.getId() + "_" + tmpword.getPosition());
            Object obj = tree.getArgumentNode(wtmp, tmpword, null);
            
          //NEW BLOCK:
			if(utmp.typ.equals("mwe")){
				ArrayList<String> mweWords = new ArrayList<>();
			    for (String word : utmp.collocations) {
			      mweWords.add(word);
			    }
				List<WordObj> allPredicateTokens = graph.getMweMatches(wtmp,mweWords, false);
//				for(WordObj wo:allPredicateTokens){
//					System.out.println("WO(1): " + wo.getName());
//				}
				System.out.println();
				obj = tree.getArgumentNodeMWE(wtmp,tmpword,allPredicateTokens);
			}
			
            if (obj instanceof Terminal) {
              feNodeId = ((Terminal) obj).getId();
            }
            if (obj instanceof Nonterminal) {
              feNodeId = ((Nonterminal) obj).getId();
            }
            Fenode feNode = new Fenode(feNodeId);
            sourceFe.addFenode(feNode);
            sentFrame.addFe(sourceFe);
          }
        }

        if (!targetlist.isEmpty()) {

          break;
        }

      }
    }
  }

  /**
   * Used in {@link #findFrames(SentenceObj)} to add FrameElements for every
   * source.
   *
   * @param utmp
   * @param graph
   * @param wtmp
   * @param containsDeleted
   * @param doublecheck
   * @param fecount
   * @param idstr
   * @param sentence
   * @param tree
   * @param sentFrame
   * @param namedEntityList
   */
  private void setSources(SentimentUnit utmp, DependencyGraph graph, WordObj wtmp, WordObj containsDeleted, int fecount, String idstr, SentenceObj sentence, ConstituencyTree tree, Frame sentFrame, NamedEntityList namedEntityList) {
    if (utmp.source != null) {
      for (String sources : utmp.source) {
        if (sources.equals("author")) {
          fecount++;
          Flag flag = new Flag("Sprecher");
          Id feId = new Id(idstr + "_e" + fecount);
          FrameElement fe = new FrameElement(feId, "Source");
          fe.addFlag(flag); // add author flag to source Fenode, not to whole Frame
          sentFrame.addFe(fe);
        } else {

          //mwe:
          ArrayList<WordObj> sourcelist = new ArrayList<WordObj>();
          if (utmp.typ.equals("mwe")) {
            sourcelist = graph.getSentimentSourceTargetMWE(wtmp, utmp, sources, containsDeleted);
          } else {
            sourcelist = graph.getSentimentSourceTarget(wtmp, sources, containsDeleted);
          }

//          if (sourcelist.size() > 0) {
//            doublecheck = sourcelist.get(0);
//          }

          for (WordObj tmpword : sourcelist) {
            if (!tmpword.getPos().equals("PRF")) {
              if (this.personCheck) {
                if (namedEntityList != null) {

                  if (namedEntityList.headIsNamedEntity(tmpword) || isPerson(tmpword) || isPersonalPronoun(tmpword) || isRelativePronoun(tmpword)) {

                    fecount++;
                    Id feId = new Id(idstr + "_e" + fecount);
                    FrameElement sourceFe = new FrameElement(feId, "Source");
                    Id feNodeId = new Id(sentence.id.getId() + "_" + tmpword.getPosition());
                    Object obj = tree.getArgumentNode(wtmp, tmpword, graph);
                    if (obj instanceof Terminal) {
                      feNodeId = ((Terminal) obj).getId();
                    }
                    if (obj instanceof Nonterminal) {
                      feNodeId = ((Nonterminal) obj).getId();
                    }
                    Fenode feNode = new Fenode(feNodeId);
                    sourceFe.addFenode(feNode);
                    sentFrame.addFe(sourceFe);

                  }

                }

              } else {
                fecount++;
                Id feId = new Id(idstr + "_e" + fecount);
                FrameElement sourceFe = new FrameElement(feId, "Source");
                Id feNodeId = new Id(sentence.id.getId() + "_" + tmpword.getPosition());
                Object obj = tree.getArgumentNode(wtmp, tmpword, graph);
                if (obj instanceof Terminal) {
                  feNodeId = ((Terminal) obj).getId();
                }
                if (obj instanceof Nonterminal) {
                  feNodeId = ((Nonterminal) obj).getId();
                }
                Fenode feNode = new Fenode(feNodeId);
                sourceFe.addFenode(feNode);
                sentFrame.addFe(sourceFe);

              }
            }

          }

          if (!sourcelist.isEmpty()) {

            break;
          }

        }

      }
    }
  }
}
