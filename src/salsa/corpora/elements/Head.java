package salsa.corpora.elements;

/**
 * Represents the 'head' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Head {

	private Meta meta;
	
	private Frames frames;
	
	private Wordtags wordtags;
	
	private Flags flags;
	
	private Annotation annotation;
	
	static String newline = System.getProperty("line.separator");

	static String xmltag = "head";
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Head() {
		super();
	}
	

	/**
	 * Returns the <code>Meta</code>.
	 * @return the meta
	 */
	public Meta getMeta() {
		return meta;
	}


	/**
	 * Sets the <code>Meta</code>.
	 * @param meta the meta to set
	 */
	public void setMeta(Meta meta) {
		this.meta = meta;
	}


	/**
	 * Returns the <code>Frames</code>.
	 * @return the frames
	 */
	public Frames getFrames() {
		return frames;
	}


	/**
	 * Sets the <code>Frames</code>.
	 * @param frames the frames to set
	 */
	public void setFrames(Frames frames) {
		this.frames = frames;
	}


	/**
	 * Returns the <code>Wordtags</code>.
	 * @return the wordtags
	 */
	public Wordtags getWordtags() {
		return wordtags;
	}


	/**
	 * Sets the <code>Wordtags</code>.
	 * @param wordtags the wordtags to set
	 */
	public void setWordtags(Wordtags wordtags) {
		this.wordtags = wordtags;
	}


	/**
	 * Returns the <code>Flags</code>.
	 * @return the flags
	 */
	public Flags getFlags() {
		return flags;
	}


	/**
	 * Sets the <code>Flags</code>.
	 * @param flags the flags to set
	 */
	public void setFlags(Flags flags) {
		this.flags = flags;
	}


	/**
	 * Returns the <code>Annotation</code>.
	 * @return the annotation
	 */
	public Annotation getAnnotation() {
		return annotation;
	}


	/**
	 * Sets the <code>Annotation</code>.
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}


	/**
	 * Returns the name of the XML element of <code>Head</code>, i. e. 'head'.
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}


	/**
	 * Returns a recursively created XML representation of this <code>Head</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);
		
		if (null != meta) {
			buffer.append("\t\t" + meta.toString());
		}
		if (null != frames) {
			buffer.append("\t\t" + frames.toString());
		}
		if (null != wordtags) {
			buffer.append("\t\t" + wordtags.toString());
		}
		if (null != flags) {
			buffer.append("\t\t" + flags.toString());
		}
		if (null != annotation) {
			buffer.append("\t\t" + annotation.toString());
		}
		
		buffer.append("\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}

}
