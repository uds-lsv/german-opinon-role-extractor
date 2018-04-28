
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import salsa.corpora.elements.Fenode;
import salsa.corpora.elements.Frame;
import salsa.corpora.elements.Frames;
import salsa.corpora.elements.Graph;
import salsa.corpora.elements.Nonterminal;
import salsa.corpora.elements.Sentence;
import salsa.corpora.elements.Terminal;

import java.io.BufferedWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class NonterminalMwePreprocessor {
  
  //Configure the path to the file in which you want to transform nonTerminals.
  final static String input = "C:/Users/m_s_w_000/Desktop/SoftwareProjekt Sentimentanalyse 2016/softwareproject_sentiment_analysis_ss_2016/System/data/shata14_adjudicated(noSourceTarget).xml";
  
  //Configure the path where you want the output to be.
  final static String output = "C:/Users/m_s_w_000/Desktop/01/goldstandard_terminalisedMwe.xml";
  
  
  Map<String, String[]> mapNonTerminalToTs = new HashMap<String, String[]>();

  public void transformNonTerminalMwes(SalsaAPIConnective salsa, String salsaXmlPath) {

    System.out.println("Processing NonterminalMwePreprocessor...");

    ArrayList<Sentence> salsaSentences = salsa.getBody().getSentences();
    for (int i = 0; i < salsaSentences.size(); i++) {
      Graph graph = salsaSentences.get(i).getGraph();
      ConstituencyTree tree = new ConstituencyTree(graph);

      //section: <frames>
      ArrayList<Frames> subjFrames = salsaSentences.get(i).getSem().getFrames();
      for (int j = 0; j < subjFrames.size(); j++) {

        //1 frame per subjective Expression
        ArrayList<Frame> subjFrame = subjFrames.get(j).getFrames();
        for (int h = 0; h < subjFrame.size(); h++) {

          ArrayList<Fenode> feNodes = subjFrame.get(h).getTarget().getFenodes();

          //1 Mwe spanning over multiple feNodes per subjExpression target possible.
//	          String mwe = "";
//	          boolean mwExpression = false;
          for (int u = 0; u < feNodes.size(); u++) {
            ArrayList<Terminal> tList = tree.getTerminals();

            for (int g = 0; g < tList.size(); g++) {
              Terminal terminal = tList.get(g);
              Nonterminal parent = tree.getParent(terminal);

              String terminalParentId = parent.getId().getId();
              String subjId = feNodes.get(u).getIdref().getId();

              //Search for mwe with parent matching the searched Id
              if (feNodes.size() > 1 && terminalParentId.equals(subjId)) {
                String[] collectTerminals = new String[parent.getEdges().size()];
                for (int terminalcount = 0; terminalcount < parent.getEdges().size(); terminalcount++) {
                  salsa.corpora.elements.Edge edge = parent.getEdges().get(terminalcount);
//	                	System.out.println("Edge: " + edge.getId().getId());
                  collectTerminals[terminalcount] = edge.getId().getId();

                }
                mapNonTerminalToTs.put(terminalParentId, collectTerminals);

              }

            }

          }
        }
      }
    }
    printMap(mapNonTerminalToTs);
    turnNonTerminalMwesToTs(salsaXmlPath, mapNonTerminalToTs);

  }

  public static void printMap(Map<String, String[]> mapNonTerminalToTs) {
    Iterator it = mapNonTerminalToTs.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      String[] out = (String[]) pair.getValue();
      String print = "";
      for (int i = 0; i < out.length; i++) {
        print += out[i] + " ";
      }
      System.out.println(pair.getKey() + " = " + print);
//	        it.remove(); 
    }
  }

  public static final void prettyPrint(Document xml) throws Exception {

    Transformer tf = TransformerFactory.newInstance().newTransformer();

    tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

    tf.setOutputProperty(OutputKeys.INDENT, "yes");

    //Writer out = new StringWriter();
    //Output path.
    BufferedWriter out = new BufferedWriter(new FileWriter(output));

    tf.transform(new DOMSource(xml), new StreamResult(out));

    System.out.println("Completed XML");

  }

  /**
   * @param args the command line arguments
   */
  public void turnNonTerminalMwesToTs(String salsaXmlPath, Map<String, String[]> mapNonTerminalToTs) {

    try {

      System.out.println("DRIN1!");
      //Input path
      File fXmlFile = new File(salsaXmlPath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();
//	            mapNonTerminalToTs
      Iterator it = mapNonTerminalToTs.entrySet().iterator();
      System.out.println("Size: " + mapNonTerminalToTs.size() + it.hasNext());
      while (it.hasNext()) {

        Map.Entry pair = (Map.Entry) it.next();
        String[] out = (String[]) pair.getValue();
//	    	        String print="";
//	    	        for(int i=0; i<out.length; i++){print+=out[i]+ " ";}
//	    	        System.out.println(pair.getKey() + " = " + print);

        NodeList nList = doc.getElementsByTagName("fenode");
        boolean found = false;

        for (int temp = 0; temp < nList.getLength(); temp++) {

          Node nNode = nList.item(temp);
          Element eElement = (Element) nNode;
//	           	 System.out.println("vorhandene idref " + eElement.getAttribute("idref") + "vs." + pair.getKey());
          if (eElement.getAttribute("idref").equals(pair.getKey())) {
            found = true;
            System.out.println("match in nonterminal " + eElement.getAttribute("idref"));

            //Replace non terminal MWE by all subordinated Terminals
            for (int terminalCount = 0; terminalCount < out.length; terminalCount++) {

              Element newTerminal = doc.createElement("fenode");
              newTerminal.setAttribute("idref", out[terminalCount]);
              nNode.getParentNode().appendChild((Node) newTerminal);

            }
            //Delete non terminal MWE
            nNode.getParentNode().removeChild(nNode);
            if (found) {
              break;
            }

          }

          if (found) {
            continue;
          }
//	                System.out.println("\nCurrent Element :" + nNode.getNodeName());
//	                if (nNode.getNodeName().equals("fe")) {
//	                    nNode.getParentNode().removeChild(nNode);
//	                    --temp;
//	                }
        }
        it.remove();
      }
      doc.normalize();
      prettyPrint(doc);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {

    //Get input to build SalsaAPIConnective
    ConfigReader conReader;
    //Read default configurations from config.txt.
    //This file must be located in the data subfolder of system.
    System.out.println("Reading default configurations...");
    java.nio.file.Path configPath = java.nio.file.Paths.get("build", "classes", "data", "config.txt");
    System.out.println("Reading config from : " + configPath.toString());

    conReader = new ConfigReader(configPath.toString());

    final SafeProperties prop = new SafeProperties(conReader.readConfig());
    // read in raw input text and create SentenceList based on it
    String fileRaw = prop.getProperty(ConfigKeys.TEXT_PATH);

    System.out.println("Reading text from " + fileRaw + "...");
    SentenceList sentences = new SentenceList();
    sentences.rawToSentenceList(fileRaw);

    // read in dependency parse file and create a DependencyGraph object for each sentence
    String dependencyFile = prop.getProperty(ConfigKeys.DEPENDENCY_PATH);

    System.out.println("Reading dependency data from " + dependencyFile + "...");
    System.out.println("Creating dependency graph...");
    sentences.readDependencyParse(dependencyFile);

      // normalize DependencyGraph objects if specified in configuration file
      /*
     String normalizeDependencyGraphs = prop.getProperty(ConfigKeys.NORMALIZE_DEPENDENCY_GRAPHS);

     if (normalizeDependencyGraphs.equals("True")) {
     System.out.println("Normalizing dependency graph...");
     sentences.normalizeDependencyGraphs();
     }
     */
    // read in Salsa / Tiger XML file and create a ConstituencyTree object for every sentence
    String constituencyFile = prop.getProperty(ConfigKeys.CONSTITUENCY_PATH);

    System.out.println("Reading constituency data from " + constituencyFile + "...");
    System.out.println("Creating constituency tree...");
    SalsaAPIConnective salsa = new SalsaAPIConnective(input, sentences);

    NonterminalMwePreprocessor proc = new NonterminalMwePreprocessor();
    proc.transformNonTerminalMwes(salsa, input);
  }
}
