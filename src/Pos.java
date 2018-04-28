import salsa.corpora.elements.Terminal;


/**
 * @author Erik Hahn
 * Helper class for interpreting <a href="http://www.ims.uni-stuttgart.de/forschung/ressourcen/lexika/TagSets/stts-table.html">
 *     STTS</a>POS tags. Usage example:
 *
 * <pre>
 *     final Pos pos = new Pos("VVFIN");
 *     isFinalVerb = pos.getCoarse == PosCoarse.VERB && pos.getVerbMood = PosVerbMood.FINITE;
 * </pre>
 */
class Pos {
	private final String pos;

	/**
	 * @param pos A STTS pos tag
	 */
	Pos(String pos) {
		this.pos = pos;
	}

	/**
	 * @param word a {@link salsa.corpora.elements.Terminal} object where <code>word.getPos()</code> is a STTS tag
	 */
	Pos(Terminal word) {
		this(word.getPos());
	}

	/**
	 * @param word a {@link WordObj object where <code>word.getPos()</code> is a STTS tag}
	 */
	Pos(WordObj word) {
		this(word.getPos());
	}

	/**
	 * @return The coarse POS of the word this class was initialized with, i.e. verb/noun/article/etc..
	 * null if the POS is invalid or not yet implemented
	 */
	PosCoarse getCoarse() {
		switch (pos.charAt(0)) {
			case '$': return PosCoarse.PUNCTUATION;
			case 'V': return PosCoarse.VERB;
			// Unknown or not implemented
			default: return null;
		}
	}

	/**
	 * @return More specific information about a verb's POS.
	 * @throws java.lang.IllegalArgumentException if the given word is not a verb
	 */
	PosVerbMood getVerbMood() {
		if (getCoarse() != PosCoarse.VERB) {
			return null;
		} else {
			if (pos.endsWith("INF")) {
				return PosVerbMood.INFINITIVE;
			} else if (pos.endsWith("IMP")) {
				return PosVerbMood.IMPERATIVE;
			} else if (pos.endsWith("FIN")) {
				return PosVerbMood.FINITE;
			} else if (pos.endsWith("ZU")) {
				return PosVerbMood.ZU_INFINITIVE;
			} else if (pos.endsWith("PP")) {
				return PosVerbMood.PARTICIPLE;
			} else {
				throw new IllegalArgumentException();
			}
		}
	}
}
