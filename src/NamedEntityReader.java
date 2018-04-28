import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements a Named Entity Reader that is applicable to preprocessed text.
 *
 */
public class NamedEntityReader {
	
	private String fileName;
	
	/**
	 * This constructs the Named Entity Reader.
	 * @param fileName The name of the file the sentences are in.
	 */
	public NamedEntityReader(String fileName){
		
		this.fileName = fileName;
	}
	
	/**
	 * Given the filename and the text with NE-tags this keeps those words that are tagged as person or organisation.
	 * If a Named Entity consists of more than one word these will be merged.
	 * @param sentences The untagged sentences to which the Named Entities that were found will be assigned.
	 */
	public void readNamedEntities(SentenceList sentences){
		
		LinkedList<SentenceObj> sentenceList = sentences.sentenceList;
		LinkedList<NamedEntityList> namedEntityLists = //sentences.namedEntityLists;
				new LinkedList<NamedEntityList>();
		Scanner scanner;
		
		int sentenceCounter = 0;
		
		System.out.println("");
		
        try {
            scanner = new Scanner(new File(this.fileName), "UTF-8"); // let the encoding be "UTF-8"
            scanner.useLocale(Locale.GERMANY); // let the locale be Germany (working on german text)
            String line;
               		
            while (scanner.hasNext()){
            	
            	SentenceObj sentence = sentenceList.get(sentenceCounter);
            	
        		NamedEntityList namedEntityList = new NamedEntityList();
        		namedEntityLists.add(namedEntityList);
        		ArrayList<NamedEntity> namedEntities = namedEntityList.getNamedEntities();

            	line = scanner.nextLine();
            	String[] words = line.split(" "); // split sentences at blank spaces
            	
            	if (words.length < sentence.wordList.size()){
      
            		System.out.println("Attention! The number of tokens in the named entity file for the sentence below is smaller than in the input file.");
            		System.out.println("Input File: " + Arrays.toString(sentence.sentence.split(" ")));
            		System.out.println("NamedEntityFile: " + Arrays.toString(words) + "\n");

            	}
            	
            	else if (words.length > sentence.wordList.size()){
            		
            		System.out.println("Attention! The number of tokens in the named entity file for the sentence below is bigger than in the input file.");
            		System.out.println("Input File: " + Arrays.toString(sentence.sentence.split(" ")));
            		System.out.println("NamedEntityFile: " + Arrays.toString(words) + "\n");

            	}
            	
            	for (int i = 0; i < words.length; i++){
            		
            		String word = words[i];
            		
            		String[] split = word.split("/"); // split off tags at slashes
            		
            		String name = split[0];
            		String tag = split[1].trim();
            		
            		/* old
            		// regular expression for named entity tag. If not given, do not treat word as a Named Entity.
            		Pattern pattern = Pattern.compile("([BI]\\-)?[A-Z]+");
            		Matcher matcher = pattern.matcher(tag);
            		
            		// match only persons & organisations, not locations & miscellaneous
            		if (matcher.matches() & !tag.equals("I-LOC") & !tag.equals("I-MISC")){
            			
            			NamedEntity ne = new NamedEntity(name, tag, i, i + 1);
            			namedEntities.add(ne);

            		}
                                */
                        
                        if (!tag.equals("O") && !tag.contains("LOC") && !tag.contains("MISC") && !tag.contains("OTH")){

                        NamedEntity ne = new NamedEntity(name, tag, i, i + 1);
                        System.out.println("ADD: " + ne);
                        namedEntities.add(ne);

                    } 
            		
            		
            	
            	}
            	
            	// merge adjacent Named Entities if they have the same tag. Cannot be done in one iteration, as size of list of named entities changes during iteration.
            	boolean flag = false;
            	            	            	
            	while (!flag & namedEntities.size() > 1){
            		
                	for(int i = 0; i < (namedEntities.size() - 1); i++){
                		
                		NamedEntity current = namedEntities.get(i);
                		NamedEntity next = namedEntities.get(i+1);
                		
                		if (current.getEndIndex() == next.getStartIndex() && current.getTag().equals(next.getTag())){ //Named Entities must have the same tag
                			
                			// merge adjacent words with same tag to one Named Entity
                			String name = current.getName() + " " + next.getName();
                			current.setName(name);
                			current.setEndIndex(next.getEndIndex());
                			namedEntities.remove(next);
                			break;
                			
                		}
                		
                		if (i == namedEntities.size() - 2){
                			flag = true;
                		}
            	}
                	
            	}
            	
            	sentenceCounter++;
            }
            
         // if file with specified pathname does not exist or s.th. else went wrong, track bugs (backtrace)
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
        
        
        for (int sentCounter = 0; sentCounter < sentenceList.size(); sentCounter++){
        
        NamedEntityList namedEntityList = namedEntityLists.get(sentCounter);
    	SentenceObj sentence = sentenceList.get(sentCounter);
		sentence.setNamedEntityList(namedEntityList);
		
        }
	}
	
}
