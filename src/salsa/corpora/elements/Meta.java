package salsa.corpora.elements;

/**
 * Represents the 'meta' section in a SalsaXML corpus.
 * @author Fabian Shirokov
 *
 */
public class Meta {
	
	private CorpusId corpus_id;
	
	private History history;
	
	private Format format;
	
	private Name name;
	
	private Author author;
	
	private Date date;
	
	private Description description;
	
	static String xmltag = "meta";
	
	static String newline = System.getProperty("line.separator");
	
	/**
	 * Zero-argumented default constructor.
	 */
	public Meta() {
		super();
	}
	
	
	
	/**
	 * Returns the <code>CorpusId</code>.
	 * @return the corpus_id
	 */
	public CorpusId getCorpus_id() {
		return corpus_id;
	}



	/**
	 * Returns the <code>Name</code>.
	 * @return the name
	 */
	public Name getName() {
		return name;
	}



	/**
	 * Sets the <code>Name</code>.
	 * @param name the name to set
	 */
	public void setName(Name name) {
		this.name = name;
	}



	/**
	 * Returns the <code>Author</code>.
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}



	/**
	 * Sets the <code>Author</code>.
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}



	/**
	 * Returns the <code>Date</code>.
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}



	/**
	 * Sets the <code>Date</code>.
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}



	/**
	 * Returns the <code>Description</code>.
	 * @return the description
	 */
	public Description getDescription() {
		return description;
	}



	/**
	 * Sets the <code>Description</code>.
	 * @param description the description to set
	 */
	public void setDescription(Description description) {
		this.description = description;
	}



	/**
	 * Sets the <code>CorpusId</code>.
	 * @param corpus_id the corpus_id to set
	 */
	public void setCorpus_id(CorpusId corpus_id) {
		this.corpus_id = corpus_id;
	}



	/**
	 * Returns the <code>History</code>.
	 * @return the history
	 */
	public History getHistory() {
		return history;
	}



	/**
	 * Sets the <code>History</code>.
	 * @param history the history to set
	 */
	public void setHistory(History history) {
		this.history = history;
	}



	/**
	 * Returns the <code>Format</code>.
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}



	/**
	 * Sets the <code>Format</code>.
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format;
	}



	/**
	 * Returns the name of the XML element of <code>Meta</code>, i. e. 'meta'. 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}



	/**
	 * Returns a recursively created XML representation of this <code>Meta</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("<" + xmltag + ">" + newline);
		
		if (null != corpus_id) {
			buffer.append("\t\t\t" + corpus_id.toString());
		}
		if (null != history) {
			buffer.append("\t\t\t" + history.toString());
		}
		if (null != format) {
			buffer.append("\t\t\t" + format.toString());
		}
		if (null != name) {
			buffer.append("\t\t\t" + name.toString());
		}
		if (null != author) {
			buffer.append("\t\t\t" + author.toString());
		}
		if (null != date) {
			buffer.append("\t\t\t" + date.toString());
		}
		if (null != description) {
			buffer.append("\t\t\t" + description.toString());
		}
		
		buffer.append("\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}


}
