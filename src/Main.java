
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import de.tuebingen.uni.sfs.germanet.api.GermaNet;

/**
 * The {@link Main} class handles all steps of the process of identifying
 * sentiment words and their sources and targets.
 */
public class Main {

  /**
   * This method executes the steps for identifying sentiment words and their
   * sources and targets.
   *
   * @param args The arguments of the system input. The only argument should be
   * the path of the configuration file (see the README for details on what
   * should be specified in the configuration file). If no argument is given,
   * the default configuration file will be used.
   * @throws IOException If a path as specified in the configuration file does
   * not point to a file.
   */
  public static void main(String args[]) throws IOException {
    // read in configuration file
    System.out.println("Loading files...");
    ConfigReader conReader;
    if (args.length >= 1) {
      System.out.println("Reading configurations from" + args[0] + "...");

      conReader = new ConfigReader(args[0]);
    } else {
      //Read default configurations from config.txt.
      //This file must be located in the data subfolder of system.
      System.out.println("Reading default configurations...");
      //old hard coded way:
      //conReader = new ConfigReader("C:\\Users\\m_s_w_000\\Desktop\\SoProCommitable\\softwareproject_sentiment_analysis_ss_2016\\System\\data\\config.txt");

      //relative way:
      java.nio.file.Path configPath = java.nio.file.Paths.get("build", "classes", "data", "config.txt");
//      System.out.println("Reading config from : " + configPath.toString());

      conReader = new ConfigReader(configPath.toString());
    }
    final SafeProperties prop = new SafeProperties(conReader.readConfig());

    //Retrieve the value of the findSources property, to be used by the classic module.
    boolean findSources = false;
    if (prop.getProperty(ConfigKeys.FIND_SOURCES).equals("True")) {
      findSources = true;
    }
    System.out.println("Find sentiment sources set to " + findSources + ".");

    //Retrieve the value of the findTargets property, to be used by the classic module.
    boolean findTargets = false;
    if (prop.getProperty(ConfigKeys.FIND_TARGETS).equals("True")) {
      findTargets = true;
    }
    System.out.println("Find sentiment targets set to " + findTargets + ".");

    // read in sentiment lexicon 
    String fileLex = prop.getProperty(ConfigKeys.LEXICON_PATH);
    System.out.println("Reading lexicon from " + fileLex + "...");

    boolean flexibleMWEs = false;
    if (prop.getProperty(ConfigKeys.FLEXIBLE_MWES).equals("True")) {
      flexibleMWEs = true;
    }
    SentimentLex lex = new SentimentLex(flexibleMWEs); // argument should become  TODO <
    lex.fileToLex(fileLex);

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
    String normalizeDependencyGraphs = prop.getProperty(ConfigKeys.NORMALIZE_DEPENDENCY_GRAPHS);

    if (normalizeDependencyGraphs.equals("True")) {
      System.out.println("Normalizing dependency graph...");
      sentences.normalizeDependencyGraphs();
    }

    // read in Salsa / Tiger XML file and create a ConstituencyTree object for every sentence
    String constituencyFile = prop.getProperty(ConfigKeys.CONSTITUENCY_PATH);

    System.out.println("Reading constituency data from " + constituencyFile + "...");
    System.out.println("Creating constituency tree...");
    SalsaAPIConnective salsa = new SalsaAPIConnective(constituencyFile, sentences);

    // Retrieve the value of the person check property, to be later used by the morphologyChecker,
    // as well as the classic module.
    String personCheck = prop.getProperty(ConfigKeys.PERSON_CHECK);

    // instantiate a GermaNet object.  The object will be by default null, but will be instantiated
    // as a GermaNet object if a file path for GermaNet has been provided, and personCheck
    // has been set to true.  The GermaNet object will be used with the MorphologyChecker and the 
    // Classic module.
    GermaNet gNet = null;
    if (personCheck.equals("True")) {
      String germaNetDir = prop.getProperty(ConfigKeys.GERMANET_DIR);
      System.out.println("Reading GermaNet data from directory " + germaNetDir + "..." + "\n");

      if (germaNetDir == null) {
        throw new IllegalArgumentException("germaNetDir must not be null if personCheck is set");
      }

      gNet = instantiateGermaNet(germaNetDir);
    }

    // read in the morphology parse file and create a MorphologyChecker object if MorphologyCheck in
    // the Configfile is set to true.  Only updates person nouns if PersonCheck is set to "true".
    // only updates sentiment nouns, if MorphologySentimentOff is set to "False".
    String morphologyCheck = prop.getProperty(ConfigKeys.MORPHOLOGY_CHECK);

    if (morphologyCheck.equals("True")) {

      String MorphologySentimentOff = prop.getProperty(ConfigKeys.MORPHOLOGY_SENTIMENT_OFF);
      String morphologyFile = prop.getProperty(ConfigKeys.MORPHOLOGY_PATH);
      System.out.println("Reading morphology data from " + morphologyFile + "...");
      MorphologyChecker morphcheck = new MorphologyChecker(morphologyFile, gNet, personCheck.equals("True"), lex);

      if (personCheck.equals("True")) {
        System.out.println("Updating lemma information for person nouns");
        morphcheck.checkMorphhology(sentences, true, false);
      }

      if (MorphologySentimentOff.equals("False")) {
        System.out.println("Updating lemma information for sentiment nouns");
        morphcheck.checkMorphhology(sentences, false, true);
      }

    }

    final Set<Module> modules = new HashSet<Module>();

    if (prop.getProperty(ConfigKeys.USE_CLASSIC_MODULE).equals("True")) {
      System.out.println("Initializing the classic module");
      final ClassicModule classicModule;
      // if specified in configuration file, run named entity check for sentiment source phrase. Filter those phrases whose head is NOT a named entity.
      if (personCheck.equals("True")) {
        String namedEntityFile = prop.getProperty(ConfigKeys.NAMED_ENTITY_PATH);
        System.out.println("Reading named entity data from " + namedEntityFile + "...");
        NamedEntityReader namedEntityReader = new NamedEntityReader(namedEntityFile);
        namedEntityReader.readNamedEntities(sentences);
        System.out.println("");

        classicModule = new ClassicModule(lex, true, findSources, findTargets, gNet);
      } // if specified in configuration file, do not run named entity check for sentiment sources. Accept phrases as sentiment sources even if their heads are no named entities.
      else {
        classicModule = new ClassicModule(lex, false, findSources, findTargets);
      }
      modules.add(classicModule);
    }

    Module giModule = null;
    if (prop.getProperty(ConfigKeys.USE_GRAMMAR_INDUCED_MODULE).equals("True")
            || prop.getProperty(ConfigKeys.USE_PRESET_SE_LOCATION_MODULE).equals("True")) {
      System.out.println("Initializing the grammar induced module");
      final Collection<String> customModalVerbs;
      if (prop.getPropertyBool(ConfigKeys.USE_DEFAULT_MODAL_VERBS)) {
        customModalVerbs = null;
      } else {
        customModalVerbs = parseList(prop.getProperty(ConfigKeys.CUSTOM_MODAL_VERB_LEMMAS));
      }
      final boolean modalTrigger = prop.getPropertyBool(ConfigKeys.GIM_TRIGGER_MODAL);
      final boolean futureTrigger = prop.getPropertyBool(ConfigKeys.GIM_TRIGGER_FUTURE);
      final boolean imperativeTrigger = prop.getPropertyBool(ConfigKeys.GIM_TRIGGER_IMPERATIVE);
      final boolean subjunctive2Trigger = prop.getPropertyBool(ConfigKeys.GIM_TRIGGER_SUBJUNCTIVE2_WUERDEB);
      giModule = new GrammarInducedModule(customModalVerbs, imperativeTrigger, modalTrigger, futureTrigger, subjunctive2Trigger, findSources, findTargets);
      if (prop.getProperty(ConfigKeys.USE_GRAMMAR_INDUCED_MODULE).equals("True")) {
        modules.add(giModule);
      }
    }

    boolean ignoreLex = false;
    if (prop.getProperty(ConfigKeys.IGNORE_LEXICON).equals("True")) {
      ignoreLex = true;
    }
    //If IgnoreLexicon=True, UsePresetSELocationModule is automatically considered to be True,
    //since to ignore the lexicon implies to instead use preset SE locations.
    //If UsePresetSELocationModule = False And IgnoreLexicon = False then the SalsaXmlCompleter and SubtaskParser will be skipped.
    if (!(prop.getPropertyBool(ConfigKeys.USE_PRESET_SE_LOCATION_MODULE) == false && ignoreLex == false)) {
      System.out.println("Initializing the preset subjective expression location module");
      if (prop.getPropertyBool(ConfigKeys.USE_GRAMMAR_INDUCED_MODULE) || prop.getPropertyBool(ConfigKeys.USE_CLASSIC_MODULE)) {
        System.err.println("Warning: PresetSELocationModule should be used by itself only!");
        if (prop.getPropertyBool(ConfigKeys.USE_GRAMMAR_INDUCED_MODULE)) {
          System.err.println("GammarInducedModule detected alongside PresetSELocationModule!");
        } if (prop.getPropertyBool(ConfigKeys.USE_CLASSIC_MODULE)) {
          System.err.println("ClassicModule detected alongside PresetSELocationModule!");
        }
      }

      String subjectiveExpressionLocationPath = prop.getProperty(ConfigKeys.SUBJECTIVE_EXPRESSION_LOCATION_PATH);
      SalsaAPIConnective salsa2 = new SalsaAPIConnective(subjectiveExpressionLocationPath, sentences);

      SubtaskParser neu = new SubtaskParser(salsa2, sentences);
      neu.searchSEs();
      System.out.println("Number of preset subjective expressions found " + neu.getSEsFromInput().size());
      boolean ignoreLexicon = prop.getPropertyBool(ConfigKeys.IGNORE_LEXICON);
      final Module comp = new PresetSELocationModule(neu.getSEsFromInput(), sentences, lex, findSources, findTargets, ignoreLexicon, giModule);

      modules.add(comp);
    }
    final SentimentChecker sentcheck = new SentimentChecker(salsa, sentences, modules);
    // search for sentiment expressions and write results to the output file specified in the configuration file
    System.out.println("Looking for sentiment expressions...");
    String outputPath = prop.getProperty(ConfigKeys.OUTPUT_PATH);

    sentcheck.findSentiments(outputPath);
  }

  /**
   * Reads in data from the GermaNet directory if the variable PersonCheck is
   * set to true in the configuration file.
   *
   * @param germaNetDir The directory containing the GermaNet data.
   */
  private static GermaNet instantiateGermaNet(String germaNetDir) {

    GermaNet gNet = null;

    File gNetDir = new File(germaNetDir);

    try {
      gNet = new GermaNet(gNetDir);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return gNet;
  }

  /**
   * Parse a comma-separated list. Implementation based on
   * <a href="http://stackoverflow.com/a/7488676">http://stackoverflow.com/a/7488676</a>
   *
   * @param list A {@link String} that contains a list of zero or more entries
   * separated by commas.
   * @return A {@link List} of {@link String}s
   */
  private static List<String> parseList(String list) {
    final String separator = ",";
    return Arrays.asList(list.split("\\s*" + separator + "\\s*"));
  }

}
