import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * CheckLex object contains information about what are the restrictions for the used sentiment lexicon 
 *
 */
public class CheckLex{
	List<String> stLex = new ArrayList<String>();
	
	/**
	 * 
	 */
	public CheckLex(){
		
	}
	
	/**
	 * Returns true if expression check is allowed to be used in a sentiment lexicon
	 * @param check
	 * @return
	 */
	public boolean contains(String check){
		if (stLex.contains(check)){
			return true;
		}
		return false;
	}
	
	/**
	 * Reads in the allowed expressions from a valid sentiment lexicon
	 * @param filename
	 */
	public void createCheckLex(String filename){
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filename),
                    "UTF-8");
            scanner.useLocale(Locale.GERMANY);
            String line;
            while (scanner.hasNext()){
            	line = scanner.nextLine();
            	line = line.replace("[", ".");
            	line = line.replace("]", "");
            	String[] parts = line.split("\\.");
            	String[]sources = parts[2].split(",");
            	String[]targets = parts[3].split(",");
            	for (int i=0;i<sources.length;i++){
            		if(stLex.contains(sources[i])==false){
            			stLex.add(sources[i]);
            		}
            	}
            	for (int i=0;i<targets.length;i++){
            		if(stLex.contains(targets[i])==false){
            			stLex.add(targets[i]);
            		}
            	}
            }
         }catch (FileNotFoundException e) {
             e.printStackTrace();
         }
		
	}
	
	/**
	 * Exports informations from CheckLex to filename to create a file containing all allowed expressions
	 * @param filename
	 */
	public void checkLexToFile(String filename){
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> it = stLex.iterator(); 
		while(it.hasNext()) {
		    try {
				out.write(it.next()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds information from a file to CheckLex object
	 * @param filename
	 */
	public void fileToCheckLex(String filename){
		Scanner scanner;
        try {
            scanner = new Scanner(new File(filename),
                    "UTF-8");
            scanner.useLocale(Locale.GERMANY);
            String line;
            while (scanner.hasNext()){
            	line = scanner.nextLine();
            	this.stLex.add(line);
            }
         }catch (FileNotFoundException e) {
             e.printStackTrace();
         }
		
	}
}