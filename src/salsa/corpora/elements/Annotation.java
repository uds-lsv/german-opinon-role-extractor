package salsa.corpora.elements;

import java.util.ArrayList;

/**
 * Represents the 'annotation' section of a SalsaXML corpus.
 * 
 * @author Fabian Shirokov
 * 
 */
public class Annotation {

	private ArrayList<Feature> features;

	private Edgelabel edgelabel;

	private Secedgelabel secedgelabel;

	static String xmltag = "annotation";

	static String newline = System.getProperty("line.separator");

	/**
	 * Zero-argumented default constructor.
	 */
	public Annotation() {
		super();
		features = new ArrayList<Feature>();
	}

	/**
	 * Returns the <code>Edgelabel</code> of this <code>Annotation</code>.
	 * If no <code>Edgelabel</code> has been set, the <code>null</code> is
	 * returned.
	 * 
	 * @return the edgelabel
	 */
	public Edgelabel getEdgelabel() {
		return edgelabel;
	}

	/**
	 * Sets the <code>Edgelabel</code> of this <code>Annotation</code>. 
	 * 
	 * @param edgelabel
	 *            the edgelabel to set
	 */
	public void setEdgelabel(Edgelabel edgelabel) {
		this.edgelabel = edgelabel;
	}

	/**
	 * 
	 * Returns the <code>Secedgelabel</code> of this <code>Annotation</code>.
	 * If no <code>Secedgelabel</code> has been set, then <code>null</code> is 
	 * returned.
	 * 
	 * @return the secedgelabel
	 */
	public Secedgelabel getSecedgelabel() {
		return secedgelabel;
	}

	/**
	 * Sets the <code>Secedgelabel</code> of this <code>Annotation</code>.
	 * 
	 * @param secedgelabel
	 *            the secedgelabel to set
	 */
	public void setSecedgelabel(Secedgelabel secedgelabel) {
		this.secedgelabel = secedgelabel;
	}

	/**
	 * Returns the list of <code>Feature</code> elements.
	 * 
	 * @return the features
	 */
	public ArrayList<Feature> getFeatures() {
		return features;
	}

	/**
	 * Returns the name of the XML element of <code>Annotation</code>, i. e.
	 * 'annotation'.
	 * 
	 * @return the xmltag
	 */
	public static String getXmltag() {
		return xmltag;
	}

	/**
	 * Adds a new <code>Feature</code> to this <code>Annotation</code>.
	 * 
	 * @param newFeature
	 */
	public void addFeature(Feature newFeature) {
		this.features.add(newFeature);
	}
	

	/**
	 * Returns a recursively created XML representation of this <code>Annotation</code>.
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("<" + xmltag + ">" + newline);

		for (Feature currentFeature : features) {
			buffer.append("\t\t\t" + currentFeature.toString());
		}
		if (null != edgelabel) {
			buffer.append("\t\t\t" + edgelabel.toString());
		}
		if (null != secedgelabel) {
			buffer.append("\t\t\t" + secedgelabel.toString());
		}

		buffer.append("\t\t</" + xmltag + ">" + newline);

		return buffer.toString();
	}


}
