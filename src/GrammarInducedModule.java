
import salsa.corpora.elements.*;

import java.util.*;

/**
 * @author Erik Hahn
 * <p>
 * This class implements <em>Grammar Induced Sentiments</em>. They also differ
 * from those found implemented by {@link ClassicModule} in that the target of
 * the sentiment is not a (noun) phrase but the whole sentence or clause.
 * </p>
 */
public class GrammarInducedModule implements Module {

    private final ModalVerbChecker modalVerbChecker;
    private final boolean imperativeTrigger;
    private final boolean modalTrigger;
    private final boolean futureTrigger;
    private final boolean subjunctive2WuerdenTrigger;
    private final boolean findSources;
    private final boolean findTargets;

    /**
     * @param modalVerbLemmas A list of lemmas. If the finite verb of a clause
     * is contained in this list, the clause will be considered a subjective
     * expression (provided <code>modalTrigger=true</code>). If null, the
     * default value defined in
     * {@link GrammarInducedModule.ModalVerbChecker#DEFAULT_LEMMAS} is used.
     * @param imperativeTrigger If true, imperative clauses are considered
     * subjective expressions.
     * @param modalTrigger If true, clauses that contain one of the modal verbs
     * specified in <code>modalVerbLemmas</code> are considered subjective
     * expressions.
     * @param futureTrigger If true, clauses using the future tense are
     * considered subjective expressions.
     * @param subjunctive2WuerdenTrigger If true, clauses that contain a
     * subjunctive 2 sentence formed with "würden" trigger a SE. Subjunctive 1
     * and subjunctive 2 without "würden" are not considered.
     */
    GrammarInducedModule(Collection<String> modalVerbLemmas, boolean imperativeTrigger, boolean modalTrigger, boolean futureTrigger, boolean subjunctive2WuerdenTrigger, boolean findSources, boolean findTargets) {
        this.imperativeTrigger = imperativeTrigger;
        this.modalTrigger = modalTrigger;
        this.futureTrigger = futureTrigger;
        this.subjunctive2WuerdenTrigger = subjunctive2WuerdenTrigger;
        modalVerbChecker = new ModalVerbChecker(modalVerbLemmas);
        this.findSources = findSources;
        this.findTargets = findTargets;
    }

    @Override
    public Collection<Frame> findFrames(SentenceObj sentence) {
        /**
         * Collects the found frames
         */
        final Collection<Frame> frames = new ArrayList<Frame>();
        final FrameIds frameIds = new FrameIds(sentence, "grammar");

        /**
         * Iterate over all possible triggers. Currently that means iterating
         * over all the words of the sentence. There is one known case where a
         * multi-word expression should trigger a SE: the formal imperative as
         * in "Gehen Sie bitte weiter", where the imperative is formed out of
         * "Gehen" and "Sie". However, since the POS tagger doesn't detect that
         * "Gehen" is an imperative verb anyway we can ignore this case,
         * simplyfing the implementation.
         *
         * If {@link isTrigger} determines that a word does trigger a SE,
         * determine trigger and source, then create the {@link Frame}.
         */
        for (WordObj word : sentence.wordList) {
            if (isTrigger(word, sentence)) {
                // Create Frame object
                final Frame frame = new Frame("SubjectiveExpression", frameIds.next());
                final FrameElementIds feIds = new FrameElementIds(frame);

                // Set trigger
                {
                    final Target target = new Target();
                    target.addFenode(new Fenode(sentence.getTree().getTerminal(word).getId()));
                    frame.setTarget(target);
                }

                // Target
                if (findTargets) {
                    {
                        final FrameElement targetElement = new FrameElement(feIds.next(), "Target");
                        for (Object node : findTarget(word, sentence)) {
                            targetElement.addFenode(new Fenode(ConstituencyTree.getNodeId(node)));
                        }
                        frame.addFe(targetElement);
                    }
                }

                // Source
                if (findSources) {
                    {
                        final FrameElement sourceElement = new FrameElement(feIds.next(), "Source");
                        sourceElement.addFlag(new Flag("Sprecher"));
                        frame.addFe(sourceElement);
                    }
                }

                // Finish
                assert frame.getFes().size() == 2;
                assert frame.getTarget() != null;
                frames.add(frame);
            }
        }

        return frames;
    }

    /**
     * <p>
     * Determine the span of the target. We do this using the constituency tree.
     * Starting from the terminal node that represents <code>word</code>, we go
     * up through the tree until we hit an <em>S</em> node. This is the
     * <em>containing clause</em>.
     * </p>
     * <img src="doc-files/GrammarInducedModule-4.png" />
     * <p>
     * Then we remove certain subclauses of the containing clause. See (1) for
     * why this is necessary. Although the subclause appears as a child of the
     * containing clause in the syntactic tree, semantically it is not an
     * adjunct of the main clause or any part thereof. However, there are also
     * sentences like (2) were this is the case.
     * </p>
     * <p>
     * (1) <img src="doc-files/GrammarInducedModule-1.png"
     * alt="[^S Neues kann nicht begonnen werden, [^S obwohl es daf&uuml;r feste Pl&auml;ne gab]]"/><br/>
     * (2) <img src="doc-files/GrammarInducedModule-2.png"
     * alt="[^S In der Bundesverfassung soll neu stehen, [^S dass nachhaltige (...)]]"/>
     * </p>
     * <p>
     * Not all cases are as clear-cut as these ones. For now, we simply remove
     * all direct children of the containing clause that have the type S.
     * </p>
     * <p>
     * In many cases subclauses are erroneously not excluded because the parser
     * has wrongly put another node between the two S nodes (3). This could be
     * worked around by not just the direct children of the containing clause.
     * However, I do not know if and in which cases S nodes can be indirect
     * children of another S node and thus when these S nodes should be removed.
     * This area needs further work.
     * </p>
     * <p>
     * (3) <img src="doc-files/GrammarInducedModule-3.png"
     *     alt="[^S ... k&ouml;nnte das soziale Netzwerk dem H&auml;ndler mitteilen, [^CS ob und [^S wann der Kunde eine Werbung (...) gesehen hat.]]]"/>
     * </p>
     * <p>
     * Finally, we remove all punctuation that is a direct child of the
     * containing clause. This is consistent with the gold standard
     * (<code>anno[1-3].txt</code>).
     * </p>
     *
     * @param word The word that triggers the subjective expression
     * @param sentence The sentence containing <code>word</code>
     * @return A {@link Collection} of {@link salsa.corpora.elements.Terminal}
     * and {@link salsa.corpora.elements.Nonterminal} objects.
     */
    public List<Object> findTarget(WordObj word, SentenceObj sentence) {
        final ConstituencyTree tree = sentence.getTree();
        final Terminal wordNode = tree.getTerminal(word);

        final Nonterminal containingClause;
        if (tree.hasDominatingNode(wordNode, "S")) {
            containingClause = tree.getLowestDominatingNode(wordNode, "S");
        } else {
            // This isn't supposed to happen except in case of parsing errors
            containingClause = tree.getTrueRoot();
        }
        return tree.getMainClause(containingClause);
    }

    /**
     * Tests if a word triggers a subjective expression. Each of the following
     * tests is executed if it has been enabled when constructing the instance.
     *
     * <ul>
     * <li>The word is a modal verb</li>
     * <li>The word is a verb in imperative mood</li>
     * <li>The word is a "werden" and being used to form the future tense</li>
     * <li>The word is a form of "würden", used to form the subjunctive 2</li>
     * </ul>
     *
     * @param word The word to test
     * @param sentence The sentence containing <code>word</code>
     * @return True if one of the mentioned tests returns true
     */
    public boolean isTrigger(WordObj word, SentenceObj sentence) {
        return (modalTrigger && modalVerbChecker.isModalVerb(word))
                || (imperativeTrigger && (new Pos(word.getPos()).getVerbMood() == PosVerbMood.IMPERATIVE))
                || (futureTrigger && isFutureAuxiliary(sentence, word))
                || (subjunctive2WuerdenTrigger && isSubjunctive2Auxiliary(sentence, word));
    }

    /**
     * Used by {@link #isFutureAuxiliary}
     */
    private static final Collection<String> INDICATIVE_WERDEN_FORMS
            = Arrays.asList(/*ich*/"werde", /*du*/ "wirst", /*er/sie (sg.) /es*/ "wird", /*wir/sie (pl.)*/ "werden", /*ihr*/ "werdet");

    /**
     * The future tense is created by using the auxiliary verb "werden" (1).
     * However, "werden" can also be used to create passive forms (2) or occur
     * as a full verb (3). This method distinguishes these meanings.
     *
     * <pre>
     *     (1) Ich werde gehen
     *     (2) Der Karton wird getragen
     *     (3) Du wirst ja rot!
     * </pre>
     *
     * This method assumes that a word is "werden" used to form a future
     * sentence if two conditions are true:
     *
     * <ol>
     * <li>The word is an indicative finite present form of "werden". This is
     * tested by comparing its form to a hard-coded list</li>
     * <li>It has a child connected via an "aux" edge and this child is an
     * infinitive verb</li>
     * </ol>
     *
     *
     * @param sentence The sentence <code>word</code> is contained in
     * @param word The word to test
     * @return true if the word is "werden" used to form a future sentence
     */
    private boolean isFutureAuxiliary(SentenceObj sentence, WordObj word) {
        final WordObj mainVerb = sentence.getRawGraph().getChild(word, "aux");
        return mainVerb != null
                && new Pos(mainVerb).getVerbMood() == PosVerbMood.INFINITIVE
                && INDICATIVE_WERDEN_FORMS.contains(word.getName().toLowerCase());
    }

    /**
     * used by {@link #isSubjunctive2Auxiliary}
     */
    private static final Collection<String> WUERDEN_FORMS
            = Arrays.asList(/*ich;er/sie/es*/"würde", /*du*/ "würdest", /*wir/sie (pl.)*/ "würden", /*ihr*/ "würdet");

    /**
     * <p>
     * Checks if a verb is a form of "würden". It works similarily to
     * {@link #isFutureAuxiliary}.
     * </p>
     * <p>
     * TODO: This could be do via a proper morphology library
     * </p>
     *
     * @param sentence The sentence <code>word</code> is contained in
     * @param word The word to test
     * @return true if the word is a form of "würden"
     */
    private boolean isSubjunctive2Auxiliary(SentenceObj sentence, WordObj word) {
        final WordObj mainVerb = sentence.getRawGraph().getChild(word, "aux");
        return mainVerb != null
                && new Pos(mainVerb).getVerbMood() == PosVerbMood.INFINITIVE
                && WUERDEN_FORMS.contains(word.getName().toLowerCase());
    }

    private final static class ModalVerbChecker {

        private final static Collection<String> DEFAULT_LEMMAS;

        static {
            final Set<String> x = new HashSet<String>();
            x.add("können");
            x.add("sollen");
            x.add("dürfen");
            x.add("müssen");
            DEFAULT_LEMMAS = Collections.unmodifiableSet(x);
        }
        private final Collection<String> lemmas;

        private ModalVerbChecker(final Collection<String> lemmas) {
            if (lemmas == null) {
                this.lemmas = DEFAULT_LEMMAS;
            } else {
                this.lemmas = lemmas;
            }
        }

        public boolean isModalVerb(WordObj word) {
            return lemmas.contains(word.getLemma());
        }
    }
}
