import salsa.corpora.noelement.Id;

/**
 * @author Erik Hahn
 * Generate the unique IDs for {@link salsa.corpora.elements.Frame} objects that are required by the SALSA API
 * and that will end up in the XML file. It does this by combining the ID of the sentence for which frames
 * will be created, a prefix unique to the module (generally a subclass of {@link Module}), and a running count.
 *
 * As long as the combination of sentence and prefix is unique, the generated ids will also be.
*/
class FrameIds {
    private final SentenceObj sentence;
    private final String prefix;
    private int frameCount = 0;

	/**
	 *
	 * @param sentence The sentence that the frames for which this instance will generate IDs will be attached to.
	 * @param prefix  Prefix unique to the module that is using this class. The prefix should only contain
	 *                characters in [a-z0-9_]. This isn't checked but these are the only ones that I know
	 *                not to crash SALTO or the evaluation tool.
	 */
    public FrameIds(SentenceObj sentence, String prefix) {
        this.sentence = sentence;
        this.prefix = prefix;
    }

	/**
	 *
	 * @return The next id
	 */
    public Id next() {
        frameCount++;
        return new Id(sentence.id.getId() + "_" + prefix + "_f" + frameCount);
    }
}
