/**
 * Named Entity object contains the relevant information about a Named Entity. 
 *
 */
public class NamedEntity {
	
	private String name;
	private String tag;
	private int startIndex; 
	private int endIndex;
	
/**
 * Creates the structure of a Named Entity with its name, tag and its length, so its startIndex and endIndex.
 * @param name This is the concrete name of a Named Entity.
 * @param type This is the Named Entity tag.
 * @param startIndex This designates at which word in a sentence the Named Entity begins.
 * @param endIndex This designates at which word in a sentence the Named Entity ends.
 */
	public NamedEntity(String name, String type, int startIndex, int endIndex){
		
		this.name = name;
		this.tag = type;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String toString() {
		return "NamedEntity [name=" + name + ", tag=" + tag + ", startIndex="
				+ startIndex + ", endIndex=" + endIndex + "]";
	}

}
