import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import de.tuebingen.uni.sfs.germanet.api.ConRel;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.api.Synset;



/**
 * The {@link MorphologyChecker} object is used update lemma information of morphologically complex
 * nouns, which (as a full word) are not recgonized as a person or sentiment expression. If a sub 
 * lemma of the word can be recgonized as a person or sentiment expression, it will replace the lemma
 * information for the word coming from the PaZu file.
 * 
 * @author patrick carroll
 */
public class MorphologyChecker {

  //private String morphologyPath;
  private String morphPath;
  private Map <String, String[]> morphologyMap;
  private GermaNet gNet;
  private boolean personCheck; 
  private Set<String> keys;




  /**
   * Creates a new instance of {@link MorphologyChecker}.
   *
   * @param morphPath is used to open the file containing morphological parses or Nouns
   * @param germanetPath is used to open up germaNet for performing a person check.
   * @param personCheck is a boolean variable set with the same value as the personCheck setting from the Config File. 
   * @param lex the sentiment lexicon is provided so that lemmas can be checked to see if they are sentiment expressions.
   */
  public MorphologyChecker(String morphPath, GermaNet gNet,
      boolean personCheck, SentimentLex lex) {

    this.morphPath = morphPath;
    this.morphologyMap = readMorphologyfile();
    this.gNet = gNet;
    this.personCheck = personCheck;
    this.keys = lex.sentimentMap.keySet();

  }

  /**
   * This method is called from the constructor to read in the morphology file and convert it to
   * and internal representation of a HashMap.
   *
   * @return morphMap {@link #morphologyMap} a map with keys as nouns, and values as a list of morph parses.
   */
  public Map <String, String[]> readMorphologyfile ( ) {
    Scanner scanner;
    Map <String, String[]> morphMap = new HashMap<String, String[]>();

    try {
      scanner = new Scanner(new File(this.morphPath), "UTF-8");
      // File is read in as a single string
      String wholeInput = scanner.useDelimiter("\\Z").next();
      scanner.close();
      // File is split on the '> ' delimiter which occurs only before entry words
      String[] morphEntries = wholeInput.split("> ");

      // Each Entry  is further split into the entry name, and a String[] for
      // each morphological parse following the entry name.
      for (int i = 1; i < morphEntries.length; i++){
        String[] lines = morphEntries[i].split("\n");
        String entryName = lines[0];
        ArrayList<String> morphParses = new ArrayList<String>();
        for (int j = 1; j < lines.length; j++){
          morphParses.add(lines[j]);
        }
        // The entry name, and the parses are added to a HashMap, as a String, String[] key/value pair
        morphMap.put(entryName, morphParses.toArray(new String[0]));   
      }

    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return morphMap;
  }

  
  

  
  /**
   *This method is similar to the person check method used in the classic Module, but it receives
   *as input a lemma, rather than a word object.
   *
   * @param lemma : the lemma being checked against the GermaNet database to determine if it's a person
   * @return value of true if the lemma is a Person, False for everything else.
   */
  public boolean isPerson (String lemma){
    
    if (lemma.equals("man")){
      return true;
    }

    if (this.gNet != null){

      List < Synset > synsets ;
      synsets = this.gNet.getSynsets(lemma);
      /*
       * check if synsnet is empty, if not empty, just continue with the block below
       * else, run some morphological analysis, and retry synset.
       * 
       * This only covers the person filter.  Also need to cover what is and is not a subject expression
       */
      for (Synset synset: synsets){
        List<List<Synset>> hypernyms = synset.getTransRelatedSynsets(ConRel.has_hypernym);
        for (List<Synset> hypernym: hypernyms){
          for (Synset s: hypernym){
            // ID for human / person, not living being in general and ID for group
            if (s.getId() == 34063 || s.getId() == 22562){
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
   *This method checks a lemma to determine if it is present in the sentiment lexicon.
   *
   * @param lemma the lemma is checked against the sentiment lexicon, to determine if it's there.
   * @return boolean value:  true if the lemma is a sentiment word in the lexicon, false otherwise.
   */
  public boolean isSentiment (String lemma){
    boolean sentimentPresent = false;
       
    sentimentPresent = this.keys.contains(lemma);

    return sentimentPresent;
  }


  /**
   * This method iterates through the nouns in the {@link #morphologyMap} to determine if any 
   * sub-lemma can be identified as a sentiment or a person.
   *
   * @param list -- the checker is passed the sentneceList in order to update lemma information if a sub-lemma is identified as person or sentiment.
   * @param checkPersonNow this boolean flag tells the method to determine if sub-lemmas are persons
   * @param checkSentimentNow this boolean flag tells the method to determine if sub-lemmas are sentiments
   */
  public void checkMorphhology ( SentenceList list,  boolean checkPersonNow, boolean checkSentimentNow){
    // Before the check can begin, only a sentiment or person flag can be activated at at time, so return a 
    // error message if both checkSentimentNow and checkPersonNow are set to true
    
    if (checkSentimentNow && checkPersonNow)
    {
      System.out.println("Stopping morphology check, cannot simultaneously check for person and sentiment");
      return;
    }
      
    //Next, just return without searching if the user wants to check for persons but personCheck is off.
    if (this.personCheck == false && checkPersonNow){
      return;
    }

    String tempLemma;
    Pattern removedTagPattern = Pattern.compile("<.*?>");

    for (String key : this.morphologyMap.keySet()){
      // check if the lemma already returns a person result. If this is the case, don't change lemma.
      if (checkPersonNow && isPerson(key)){
        continue;
      }
      // check if the lemma already returns a sentiment result. If this is the case don't change lemma.
      else if( checkSentimentNow && isSentiment(key) ){
        continue;
      }
      else{

        String[] morphParsesTemp = this.morphologyMap.get(key);

        morphParsesLoop: {
          for (String singleMorphParse: morphParsesTemp){
            String spaceSeperatedParse = removedTagPattern.matcher(singleMorphParse).replaceAll(" ");

            // first check if no results are given. In this case don't change lemma
            if (spaceSeperatedParse.contains("no result for")){
              continue;
            }
            // then iterate over sub sections of individual morph parses, build them into a string,
            // and check if that sub-lemma is a person or sentiment expression.
            String[] tokensFromParse = spaceSeperatedParse.split(" ");
            int numOfParseTokens = tokensFromParse.length;

            for (int i = 0; i < numOfParseTokens; i++){
              StringBuilder sb = new StringBuilder();
              for (int j = i; j < numOfParseTokens; j++){
                if (j == i) { 
                  sb.append(tokensFromParse[j]);
                }
                else {
                  sb.append(tokensFromParse[j].toLowerCase());
                }
              }
              tempLemma = sb.toString();
              if (!Character.isUpperCase(tempLemma.codePointAt(0))){
                break;
              }
              if (checkPersonNow && isPerson(tempLemma)){
                this.replaceLemma(list, tempLemma, key);
                System.out.println("Person Morph Check replaced original lemma : " + key + " with shortened version : " + tempLemma);
                break morphParsesLoop;
              }
              if (checkSentimentNow && isSentiment(tempLemma)){
                this.replaceLemma(list, tempLemma, key);
                System.out.println("Sentiment Morph Check replaced original lemma : " + key + " with shortened version : " + tempLemma);
                break morphParsesLoop;
              }

            }

          }
        }
      }
    }
  }

  /**
   *This method is used once a sub-lemma has been identified as a person or sentiment expression.
   *It iterates through all words in all sentences and re-sets the lemma of any word which matches 
   *the key provided to it.
   *
   * @param list : the word list which is being updated with new lemma information
   * @param replacementLemma : the sub-lemma which was identified as either a person or sentiment
   * @param key : the original word which is getting it's lemma replaced
   */
  public void replaceLemma (SentenceList list, String replacementLemma, String key){

 
    int sentenceListSize = list.sentenceList.size();
    // iterate over sentence lists
    for (int i = 0; i < sentenceListSize; i++){
      SentenceObj sentenceObjTemp = list.sentenceList.get(i);

      // get a word list from each sentence
      LinkedList<WordObj> wordListTemp = sentenceObjTemp.wordList;
      int wordListSize = sentenceObjTemp.wordList.size();

      // iterate over wordListTemp and get individual wordObjects
      for (int j = 0; j < wordListSize; j++){

        WordObj wordObjTemp = wordListTemp.get(j);

        if (wordObjTemp.getName().equals(key)){
          wordObjTemp.setLemma(replacementLemma);
          continue;
        }
      }
    }
  }

}


