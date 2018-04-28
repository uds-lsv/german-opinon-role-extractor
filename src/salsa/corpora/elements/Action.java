package salsa.corpora.elements;

import java.util.ArrayList;

public class Action {

	private String date;
	
	private String time;
	
	private String user;
	
	private String type;
	
	private Step step;
	
	private ArrayList<Recipient> recipients;


	static String newline = System.getProperty("line.separator");

	static String xmltag = "action";
	

	/**
	 * 
	 * Default constructor that takes the date, time, user and type as arguments.
	 * @param date
	 * @param time
	 * @param user
	 * @param type
	 */
	public Action(String date, String time, String user, String type) {
		super();
		this.date = date;
		this.time = time;
		this.user = user;
		this.type = type;
		recipients = new ArrayList<Recipient>();
	}
	
	/**
	 * Returns the <code>Step</code> of this <code>Action</code>.
	 * @return
	 */
	public Step getStep() {
		return this.step;
	}
	
	/**
	 * Sets the <code>Step</code> of this <code>Action</code>. 
	 * @param step
	 */
	public void setStep(Step step) {
		this.step = step;
	}

	/**
	 * Returns the list of <code>Recipient</code> elements.
	 */
	public ArrayList<Recipient> getRecipients() {
		return recipients;
	}
	
	/**
	 * Adds a new <code>Recipient</code> to this <code>Action</code>.
	 */
	public void addRecipient(Recipient newRecipient) {
		this.recipients.add(newRecipient);
	}
	
	/**
	 * Returns the XML element name of <code>Action</code>, i. e. 'action'.
	 */
	static public String getXmltag() {
		return xmltag;
	}

	/**
	 * Returns a recursively created XML representation of this <code>Action</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + " date=\"" + date + "\" time=\"" + time + "\"" +
				" user=\"" + user + "\" type=\"" + type+ "\">" + newline);
		if (null != step){
			buffer.append("\t\t\t\t\t" + step.toString());
		}
		for (Recipient currentRecipient : recipients){
			buffer.append("\t\t\t\t\t" + currentRecipient.toString());
		}
		buffer.append("\t\t\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}


}
