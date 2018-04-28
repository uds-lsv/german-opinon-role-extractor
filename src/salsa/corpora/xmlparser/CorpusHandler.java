package salsa.corpora.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import salsa.corpora.elements.Action;
import salsa.corpora.elements.Annotation;
import salsa.corpora.elements.Author;
import salsa.corpora.elements.Body;
import salsa.corpora.elements.Corpus;
import salsa.corpora.elements.CorpusId;
import salsa.corpora.elements.Date;
import salsa.corpora.elements.Description;
import salsa.corpora.elements.Edge;
import salsa.corpora.elements.Edgelabel;
import salsa.corpora.elements.Element;
import salsa.corpora.elements.Feature;
import salsa.corpora.elements.Fenode;
import salsa.corpora.elements.Flag;
import salsa.corpora.elements.Flags;
import salsa.corpora.elements.Format;
import salsa.corpora.elements.Frame;
import salsa.corpora.elements.FrameElement;
import salsa.corpora.elements.Frames;
import salsa.corpora.elements.Global;
import salsa.corpora.elements.Globals;
import salsa.corpora.elements.Graph;
import salsa.corpora.elements.Head;
import salsa.corpora.elements.History;
import salsa.corpora.elements.Match;
import salsa.corpora.elements.Matches;
import salsa.corpora.elements.Meta;
import salsa.corpora.elements.Name;
import salsa.corpora.elements.Nonterminal;
import salsa.corpora.elements.Nonterminals;
import salsa.corpora.elements.Part;
import salsa.corpora.elements.Recipient;
import salsa.corpora.elements.Secedge;
import salsa.corpora.elements.Secedgelabel;
import salsa.corpora.elements.Semantics;
import salsa.corpora.elements.Sentence;
import salsa.corpora.elements.Splitword;
import salsa.corpora.elements.Splitwords;
import salsa.corpora.elements.Step;
import salsa.corpora.elements.Target;
import salsa.corpora.elements.Terminal;
import salsa.corpora.elements.Terminals;
import salsa.corpora.elements.Underspecification;
import salsa.corpora.elements.UnderspecificationFrameElements;
import salsa.corpora.elements.UnderspecificationFrames;
import salsa.corpora.elements.Uspblock;
import salsa.corpora.elements.Uspitem;
import salsa.corpora.elements.Value;
import salsa.corpora.elements.Variable;
import salsa.corpora.elements.Wordtag;
import salsa.corpora.elements.Wordtags;
import salsa.corpora.noelement.Id;

/**
 * This handles events of the <code>CorpusParser</code>. It tells the parser
 * how to properly read in a SalsaXML file into the <code>Corpus</code> data
 * structure.
 * 
 * @author Fabian Shirokov
 * 
 */
public class CorpusHandler extends DefaultHandler {

	// the currently active element
	private String activeElement;

	// if the 'target' element is active, then <code>isTarget</code> is true.
	private boolean isTarget;

	// if 'fe' is active, the <code>isFrameElement</code> is true.
	private boolean isFrameElement;

	// if 'frame' is active, the <code>isFrame</code> is true.
	private boolean isFrame;

	// if 'uspframes' is active, the <code>isUspframes</code> is true.
	private boolean isUspframes;

	private boolean isEdgelabel;

	private boolean isSecedgelabel;

	private boolean isFeature;

	// the corpus that will at the end represent the whole XML file.
	private Corpus corpus;

	private Action currentAction;

	private Annotation currentAnnotation;

	private Author currentAuthor;

	private Body currentBody;

	private CorpusId currentCorpusId;

	private Date currentDate;

	private Description currentDescription;

	private Edge currentEdge;

	private Edgelabel currentEdgelabel;

	private Element currentElement;

	private Feature currentFeature;

	private Fenode currentFenode;

	private Flag currentFlag;

	private Flags currentFlags;

	private Format currentFormat;

	private Frame currentFrame;

	private FrameElement currentFrameElement;

	private Frames currentFrames;

	private Global currentGlobal;

	private Globals currentGlobals;

	private Graph currentGraph;

	private Head currentHead;

	private History currentHistory;

	private Match currentMatch;

	private Matches currentMatches;

	private Meta currentMeta;

	private Name currentName;

	private Nonterminal currentNonterminal;

	private Nonterminals currentNonterminals;

	private Part currentPart;

	private Recipient currentRecipient;

	private Secedge currentSecedge;

	private Secedgelabel currentSecedgelabel;

	private Semantics currentSemantics;

	private Sentence currentSentence;

	private Splitword currentSplitword;

	private Splitwords currentSplitwords;

	private Step currentStep;

	private Target currentTarget;

	private Terminal currentTerminal;

	private Terminals currentTerminals;

	private Underspecification currentUnderspecification;

	private UnderspecificationFrameElements currentUnderspecificationFrameElements;

	private UnderspecificationFrames currentUnderspecificationFrames;

	private Uspblock currentUspblock;

	private Uspitem currentUspitem;

	private Value currentValue;

	private Variable currentVariable;

	private Wordtag currentWordtag;

	private Wordtags currentWordtags;

	/**
	 * Zero-argumented default constructor.
	 */
	public CorpusHandler() {

	}

	/**
	 * This method is called when the XML document starts. By now, nothing
	 * happens in this method.
	 */
	public void startDocument() {

	}

	/**
	 * This overrides {@link org.xml.sax.helpers.DefaultHandler#startElement}.
	 * This method is called when the parser has found an opening element tag.
	 * It initializes new objects and assigns them to their superordinating
	 * elements. For example, it creates a new <code>Head</code> and assigns
	 * it to the <code>Corpus</code>.
	 * 
	 * @param uri
	 *            a <code>String</code> with the namespace URI, empty if
	 *            parser factory is not namespace aware (default)
	 * @param localName
	 *            a <code>String</code> with the local name (without prefix),
	 *            empty if parser factory is not namespace aware (default)
	 * @param qualName
	 *            a <code>String</code> with the qualified (with prefix) name,
	 *            or the empty string if qualified names are not available
	 * @param atts
	 *            <code>Attributes</code> attached to the element, empty if
	 *            there are no attributes
	 * @throws SAXException
	 *             if an error occurs, possibly wrapping another exception
	 */
	public void startElement(@SuppressWarnings("unused")
	String uri, @SuppressWarnings("unused")
	String localName, String qualName, Attributes atts) throws SAXException {

		activeElement = qualName;

		if (qualName.equalsIgnoreCase("action")) {

			currentAction = new Action(atts.getValue("date"), atts
					.getValue("time"), atts.getValue("user"), atts
					.getValue("type"));

			currentHistory.addAction(currentAction);

		} else if (qualName.equalsIgnoreCase("annotation")) {

			currentAnnotation = new Annotation();

			currentHead.setAnnotation(currentAnnotation);

		} else if (qualName.equalsIgnoreCase("author")) {

			currentAuthor = new Author();

			currentMeta.setAuthor(currentAuthor);

		}

		else if (qualName.equalsIgnoreCase("body")) {

			currentBody = new Body();

			corpus.setBody(currentBody);

		} else if (qualName.equalsIgnoreCase("corpus")) {

			corpus = new Corpus(atts.getValue("corpusname"), atts
					.getValue("target"));

		} else if (qualName.equalsIgnoreCase("corpus_id")) {

			currentCorpusId = new CorpusId();

			currentMeta.setCorpus_id(currentCorpusId);

		} else if (qualName.equalsIgnoreCase("date")) {

			currentDate = new Date();

			currentMeta.setDate(currentDate);

		} else if (qualName.equalsIgnoreCase("description")) {

			currentDescription = new Description();

			currentMeta.setDescription(currentDescription);

		} else if (qualName.equalsIgnoreCase("edge")) {

			currentEdge = new Edge(new Id(atts.getValue("idref")), atts
					.getValue("label"));

			currentNonterminal.addEdge(currentEdge);

		} else if (qualName.equalsIgnoreCase("edgelabel")) {

			isEdgelabel = true;

			currentEdgelabel = new Edgelabel();

			currentAnnotation.setEdgelabel(currentEdgelabel);

		} else if (qualName.equalsIgnoreCase("element")) {

			currentElement = new Element(atts.getValue("name"), atts
					.getValue("optional"));

			currentFrame.addElement(currentElement);

		} else if (qualName.equalsIgnoreCase("fe")) {

			isFrameElement = true;

			currentFrameElement = new FrameElement(new Id(atts.getValue("id")),
					atts.getValue("name"));

			String source = atts.getValue("source");

			String usp = atts.getValue("usp");

			if (null != source) {
				currentFrameElement.setSource(source);
			}
			if (null != usp) {
				currentFrameElement.setUsp(usp);
			}

			currentFrame.addFe(currentFrameElement);

		} else if (qualName.equalsIgnoreCase("feature")) {

			isFeature = true;

			currentFeature = new Feature(atts.getValue("domain"), atts
					.getValue("name"));

			currentAnnotation.addFeature(currentFeature);

		} else if (qualName.equalsIgnoreCase("fenode")) {

			currentFenode = new Fenode(new Id(atts.getValue("idref")), atts
					.getValue("is_split"));

			if (isTarget) {
				currentTarget.addFenode(currentFenode);
			} else {
				currentFrameElement.addFenode(currentFenode);
			}

		} else if (qualName.equalsIgnoreCase("flag")) {

			currentFlag = new Flag(atts.getValue("name"));

			String forWhat = atts.getValue("for");
			if (null != forWhat) {
				currentFlag.setForWhat(forWhat);
			}
			String source = atts.getValue("source");
			if (null != source) {
				currentFlag.setSource(source);
			}

			if (isFrameElement) {
				currentFrameElement.addFlag(currentFlag);
			} else if (isFrame) {
				currentFrame.addFlag(currentFlag);
			} else {

				currentFlags.addFlag(currentFlag);
			}

		} else if (qualName.equalsIgnoreCase("flags")) {

			currentFlags = new Flags();

			currentHead.setFlags(currentFlags);

		} else if (qualName.equalsIgnoreCase("format")) {

			currentFormat = new Format();

			currentMeta.setFormat(currentFormat);

		} else if (qualName.equalsIgnoreCase("frame")) {

			isFrame = true;

			currentFrame = new Frame(atts.getValue("name"));

			String idString = atts.getValue("id");

			String source = atts.getValue("source");

			String usp = atts.getValue("usp");

			if (null != idString) {
				Id id = new Id(idString);

				currentFrame.setId(id);
			}
			if (null != source) {
				currentFrame.setSource(source);
			}
			if (null != usp) {
				currentFrame.setUsp(usp);
			}

			currentFrames.addFrame(currentFrame);

		} else if (qualName.equalsIgnoreCase("frames")) {

			currentFrames = new Frames();

			String xmlns = atts.getValue("xmlns");

			if (null != xmlns) {
				currentFrames.setXmlns(xmlns);
			}

			if (null == currentHead.getFrames()) {
				currentHead.setFrames(currentFrames);
			} else {
				currentSemantics.addFrames(currentFrames);
			}

		} else if (qualName.equalsIgnoreCase("global")) {

			currentGlobal = new Global(atts.getValue("type"));

			String param = atts.getValue("param");

			if (null != param) {
				currentGlobal.setParam(param);
			}

			currentGlobals.addGlobal(currentGlobal);

		} else if (qualName.equalsIgnoreCase("globals")) {

			currentGlobals = new Globals();

			currentSemantics.addGlobals(currentGlobals);

		}

		else if (qualName.equalsIgnoreCase("graph")) {

			currentGraph = new Graph(new Id(atts.getValue("root")));

			currentSentence.setGraph(currentGraph);

		} else if (qualName.equalsIgnoreCase("head")) {

			currentHead = new Head();

			corpus.setHead(currentHead);

		} else if (qualName.equalsIgnoreCase("history")) {

			currentHistory = new History();

			currentMeta.setHistory(currentHistory);

		} else if (qualName.equalsIgnoreCase("match")) {

			currentMatch = new Match(atts.getValue("subgraph"));

			currentMatches.addMatch(currentMatch);

		} else if (qualName.equalsIgnoreCase("matches")) {

			currentMatches = new Matches();

			currentSentence.setMatches(currentMatches);

		} else if (qualName.equalsIgnoreCase("meta")) {

			currentMeta = new Meta();

			currentHead.setMeta(currentMeta);

		} else if (qualName.equalsIgnoreCase("name")) {

			currentName = new Name();

			currentMeta.setName(currentName);

		}

		else if (qualName.equalsIgnoreCase("step")) {

			currentStep = new Step();

			currentAction.setStep(currentStep);

		} else if (qualName.equalsIgnoreCase("recipient")) {

			currentRecipient = new Recipient(atts.getValue("id"));

			currentAction.addRecipient(currentRecipient);

		} else if (qualName.equalsIgnoreCase("nonterminals")) {

			currentNonterminals = new Nonterminals();

			currentGraph.setNonterminals(currentNonterminals);

		} else if (qualName.equalsIgnoreCase("nt")) {

			currentNonterminal = new Nonterminal(atts.getValue("cat"), new Id(
					atts.getValue("id")));

			currentNonterminals.addNonterminal(currentNonterminal);

		} else if (qualName.equalsIgnoreCase("part")) {

			currentPart = new Part(atts.getValue("word"), new Id(atts
					.getValue("id")));

			currentSplitword.addPart(currentPart);

		} else if (qualName.equalsIgnoreCase("s")) {

			currentSentence = new Sentence(new Id(atts.getValue("id")));

			String source = atts.getValue("source");

			if (null != source) {
				currentSentence.setSource(source);
			}

			currentBody.addSentence(currentSentence);

		} else if (qualName.equalsIgnoreCase("secedge")) {

			currentSecedge = new Secedge(new Id(atts.getValue("id")), atts
					.getValue("label"));

			currentTerminal.setSecedge(currentSecedge);

		} else if (qualName.equalsIgnoreCase("secedgelabel")) {

			isSecedgelabel = true;

			currentSecedgelabel = new Secedgelabel();

			currentAnnotation.setSecedgelabel(currentSecedgelabel);

		} else if (qualName.equalsIgnoreCase("sem")) {

			currentSemantics = new Semantics();

			currentSentence.setSem(currentSemantics);

		} else if (qualName.equalsIgnoreCase("splitwords")) {

			currentSplitwords = new Splitwords();

			currentSemantics.addSplitwords(currentSplitwords);

		} else if (qualName.equalsIgnoreCase("splitword")) {

			currentSplitword = new Splitword(new Id(atts.getValue("idref")));

			currentSplitwords.addSplitword(currentSplitword);

		} else if (qualName.equalsIgnoreCase("step")) {

			currentStep = new Step();

			currentAction.setStep(currentStep);

		} else if (qualName.equalsIgnoreCase("t")) {
		
			currentTerminal = new Terminal(new Id(atts.getValue("id")), atts
					.getValue("lemma"), atts.getValue("morph"), atts
					.getValue("pos"), atts.getValue("word"));
			
		
			currentTerminals.addTerminal(currentTerminal);

		} else if (qualName.equalsIgnoreCase("target")) {

			isTarget = true;

			currentTarget = new Target();

			String idString = atts.getValue("id");
			String lemma = atts.getValue("lemma");
			String headlemma = atts.getValue("headlemma");

			if (null != idString) {
				Id id = new Id(idString);
				currentTarget.setId(id);
			}
			if (null != lemma) {
				currentTarget.setLemma(lemma);
			}
			if (null != headlemma) {
				currentTarget.setHeadlemma(headlemma);
			}

			currentFrame.setTarget(currentTarget);

		} else if (qualName.equalsIgnoreCase("terminals")) {

			currentTerminals = new Terminals();

			currentGraph.setTerminals(currentTerminals);

		} else if (qualName.equalsIgnoreCase("usp")) {

			currentUnderspecification = new Underspecification();

			currentSemantics.addUsp(currentUnderspecification);

		} else if (qualName.equalsIgnoreCase("uspblock")) {

			currentUspblock = new Uspblock();

			if (isUspframes) {
				currentUnderspecificationFrames.addUspblock(currentUspblock);
			} else {

				currentUnderspecificationFrameElements
						.addUspblock(currentUspblock);
			}

		} else if (qualName.equalsIgnoreCase("uspfes")) {

			currentUnderspecificationFrameElements = new UnderspecificationFrameElements();

			currentUnderspecification
					.setUspfes(currentUnderspecificationFrameElements);

		} else if (qualName.equalsIgnoreCase("uspframes")) {

			isUspframes = true;

			currentUnderspecificationFrames = new UnderspecificationFrames();

			currentUnderspecification
					.setUspframes(currentUnderspecificationFrames);

		} else if (qualName.equalsIgnoreCase("uspitem")) {

			currentUspitem = new Uspitem(new Id(atts.getValue("idref")));

			currentUspblock.addUspitem(currentUspitem);

		} else if (qualName.equalsIgnoreCase("value")) {

			currentValue = new Value(atts.getValue("name"));

			if (isEdgelabel) {

				currentEdgelabel.addValue(currentValue);
			} else if (isSecedgelabel) {
				currentSecedgelabel.addValue(currentValue);
			} else if (isFeature) {
				currentFeature.addValue(currentValue);
			} else {
				System.err
						.println("parsing error: 'value' is in the wrong section");
			}

		} else if (qualName.equalsIgnoreCase("variable")) {

			currentVariable = new Variable(atts.getValue("name"), new Id(atts
					.getValue("idref")));

			currentMatch.addVariable(currentVariable);

		} else if (qualName.equalsIgnoreCase("wordtag")) {

			currentWordtag = new Wordtag(atts.getValue("name"));

			currentWordtags.setWordtag(currentWordtag);

		} else if (qualName.equalsIgnoreCase("wordtags")) {

			currentWordtags = new Wordtags();

			String xmlns = atts.getValue("xmlns");

			if (null != xmlns) {
				currentWordtags.setXmlns(xmlns);
			}

			if (null == currentHead.getWordtags()) {

				currentHead.setWordtags(currentWordtags);
			} else {
				currentSemantics.addWordtags(currentWordtags);
			}

		}
	}

	/**
	 * This overrides {@link org.xml.sax.helpers.DefaultHandler#endElement}.
	 * This method is called when the parser has found a closing element tag.
	 * 
	 * @param uri
	 *            a <code>String</code> with the namespace URI, empty if
	 *            parser factory is not namespace aware (default)
	 * @param localName
	 *            a <code>String</code> with the local name (without prefix),
	 *            empty if parser factory is not namespace aware (default)
	 * @param qualName
	 *            a <code>String</code> with the qualified (with prefix) name,
	 *            or the empty string if qualified names are not available
	 * @throws SAXException
	 *             if an error occurs, possibly wrapping another exception
	 */
	public void endElement(@SuppressWarnings("unused")
	String uri, @SuppressWarnings("unused")
	String localName, String qualName) throws SAXException {

		if (qualName.equalsIgnoreCase("action")) {

		} else if (qualName.equalsIgnoreCase("annotation")) {

		} else if (qualName.equalsIgnoreCase("body")) {

		} else if (qualName.equalsIgnoreCase("corpus")) {

		} else if (qualName.equalsIgnoreCase("corpus_id")) {

		} else if (qualName.equalsIgnoreCase("edge")) {

		} else if (qualName.equalsIgnoreCase("edgelabel")) {

			isEdgelabel = false;

		} else if (qualName.equalsIgnoreCase("element")) {

		} else if (qualName.equalsIgnoreCase("fe")) {

			isFrameElement = false;

		} else if (qualName.equalsIgnoreCase("feature")) {

			isFeature = false;

		} else if (qualName.equalsIgnoreCase("fenode")) {

		} else if (qualName.equalsIgnoreCase("flag")) {

		} else if (qualName.equalsIgnoreCase("flags")) {

		} else if (qualName.equalsIgnoreCase("format")) {

		} else if (qualName.equalsIgnoreCase("frame")) {

			isFrame = false;

		} else if (qualName.equalsIgnoreCase("frames")) {

		} else if (qualName.equalsIgnoreCase("global")) {

		} else if (qualName.equalsIgnoreCase("globals")) {

		} else if (qualName.equalsIgnoreCase("graph")) {

		} else if (qualName.equalsIgnoreCase("head")) {

		} else if (qualName.equalsIgnoreCase("history")) {

		} else if (qualName.equalsIgnoreCase("match")) {

		} else if (qualName.equalsIgnoreCase("matches")) {

		} else if (qualName.equalsIgnoreCase("meta")) {

		} else if (qualName.equalsIgnoreCase("step")) {

		} else if (qualName.equalsIgnoreCase("recipient")) {

		} else if (qualName.equalsIgnoreCase("nonterminals")) {

		} else if (qualName.equalsIgnoreCase("nt")) {

		} else if (qualName.equalsIgnoreCase("part")) {

		} else if (qualName.equalsIgnoreCase("s")) {

		} else if (qualName.equalsIgnoreCase("secedge")) {

		} else if (qualName.equalsIgnoreCase("secedgelabel")) {

			isSecedgelabel = false;

		} else if (qualName.equalsIgnoreCase("sem")) {

		} else if (qualName.equalsIgnoreCase("splitwords")) {

		} else if (qualName.equalsIgnoreCase("splitword")) {

		} else if (qualName.equalsIgnoreCase("step")) {

		} else if (qualName.equalsIgnoreCase("t")) {

		} else if (qualName.equalsIgnoreCase("target")) {

			isTarget = false;

		} else if (qualName.equalsIgnoreCase("terminals")) {

		} else if (qualName.equalsIgnoreCase("usp")) {

		} else if (qualName.equalsIgnoreCase("uspblock")) {

		} else if (qualName.equalsIgnoreCase("uspfes")) {

		} else if (qualName.equalsIgnoreCase("uspframes")) {

			isUspframes = false;

		} else if (qualName.equalsIgnoreCase("uspitem")) {

		} else if (qualName.equalsIgnoreCase("value")) {

		} else if (qualName.equalsIgnoreCase("variable")) {

		} else if (qualName.equalsIgnoreCase("wordtag")) {

		} else if (qualName.equalsIgnoreCase("wordtags")) {

		}
	}

	/**
	 * This method is called when some text is found in the XML file. It adds or
	 * assigns the text to the currently active element.
	 * 
	 */
	public void characters(char[] c, int start, int length) throws SAXException {

		String currentString = new String(c, start, length);

		if (activeElement.equalsIgnoreCase("author")) {
			String text = currentAuthor.getText();

			if (null != text) {
				text = text + currentString;
				currentAuthor.setText(text);
			} else {

				currentAuthor.setText(currentString);
			}

		}

		else if (activeElement.equalsIgnoreCase("corpus_id")) {

			String text = currentCorpusId.getId();

			if (null != text) {
				text = text + currentString;
				currentCorpusId.setId(text);
			} else {

				currentCorpusId.setId(currentString);
			}

		} else if (activeElement.equalsIgnoreCase("date")) {
			String text = currentDate.getText();

			if (null != text) {
				text = text + currentString;
				currentDate.setText(text);
			} else {

				currentDate.setText(currentString);
			}

		} else if (activeElement.equalsIgnoreCase("description")) {
			String text = currentDescription.getText();

			if (null != text) {
				text = text + currentString;
				currentDescription.setText(text);
			} else {

				currentDescription.setText(currentString);
			}

		}

		else if (activeElement.equalsIgnoreCase("flag")) {

			String text = currentFlag.getText();

			if (null != text) {
				text = text + currentString;
				currentFlag.setText(text);
			} else {

				currentFlag.setText(currentString);
			}

		}

		else if (activeElement.equalsIgnoreCase("format")) {

			String text = currentFormat.getFormat();

			if (null != text) {
				text = text + currentString;
				currentFormat.setFormat(text);
			} else {

				currentFormat.setFormat(currentString);
			}
		}

		else if (activeElement.equalsIgnoreCase("global")) {

			String text = currentGlobal.getText();

			if (null != text) {
				text = text + currentString;
				currentGlobal.setText(text);
			} else {

				currentGlobal.setText(currentString);
			}

		} else if (activeElement.equalsIgnoreCase("name")) {

			String text = currentName.getText();

			if (null != text) {
				text = text + currentString;
				currentName.setText(text);
			} else {

				currentName.setText(currentString);
			}

		} else if (activeElement.equalsIgnoreCase("recipient")) {

			String text = currentRecipient.getText();

			if (null != text) {
				text = text + currentString;
				currentRecipient.setText(text);
			} else {

				currentRecipient.setText(currentString);
			}

		} else if (activeElement.equalsIgnoreCase("step")) {

			String text = currentStep.getStep();

			if (null != text) {
				text = text + currentString;
				currentStep.setStep(text);
			} else {

				currentStep.setStep(currentString);
			}

		}

		else if (activeElement.equalsIgnoreCase("value")) {
			String text = currentValue.getText();

			if (null != text) {
				text = text + currentString;
				currentValue.setText(text);
			} else {

				currentValue.setText(currentString);
			}

		}

		else if (activeElement.equalsIgnoreCase("variable")) {

			String text = currentVariable.getText();

			if (null != text) {
				text = text + currentString;
				currentVariable.setText(text);
			} else {

				currentVariable.setText(currentString);
			}
		}
	}

	/**
	 * This returns the <code>Corpus</code> that has been read out of the XML
	 * file.
	 */
	public Corpus getCorpus() {
		return this.corpus;
	}

}
