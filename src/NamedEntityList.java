import java.util.ArrayList;

/**
 * All Named Entities of every single sentence separately.
 *
 */
public class NamedEntityList {
	
	private ArrayList<NamedEntity> namedEntities;

	public NamedEntityList() {
		this.namedEntities = new ArrayList<NamedEntity>();
	}
	
	public ArrayList<NamedEntity> getNamedEntities() {
		return namedEntities;
	}
	
	/**
	 * Given one or more words, check whether it is about a Named Entity or not.
	 * @param source The word that might be a Named Entity.
	 * @param startIndex The start index of the Named Entity.
	 * @param endIndex The end index of the Named Entity.
	 * @return True if words fit in scope of Named Entities, false if not or nothing in the list.
	 */
	public boolean isNamedEntity(String source, int startIndex, int endIndex) {
		
		if (this.namedEntities.isEmpty()){
			return false;
		}
		
		for (NamedEntity ne: this.namedEntities){
			if (ne.getName().equals(source) && ne.getStartIndex() == startIndex && ne.getEndIndex() == endIndex){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Given the head of a constituent, check whether it is about a Named Entity or not.
	 * @param head The head of the constituent that might be a Named Entity.
	 * @param startIndex The start index of the Named Entity.
	 * @param endIndex The end index of the Named Entity.
	 * @return True if words contain the head of the constituent and this fits in scope of Named Entity, false if not or nothing in the list.
	 */
	public boolean headIsNamedEntity(String head, int startIndex, int endIndex) {

		if (this.namedEntities.isEmpty()){
			return false;
		}
			
		for (NamedEntity ne: this.namedEntities){
			if (ne.getName().contains(head) && ne.getStartIndex() <= startIndex && ne.getEndIndex() >= endIndex){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Given the head of a constituent, check whether it is about a Named Entity or not.
	 * @param head The head of the constituent that might be a Named Entity.
	 * @return True if head of the constituent is contained in the words and this fits in scope of Named Entity, false if not or nothing in the list.
	 */
	public boolean headIsNamedEntity(WordObj head) {
		
		if (this.namedEntities.isEmpty()){
			return false;
		}
		
		// identify the scope of the Named Entity
		int startIndex = head.getPosition() - 1;
		int endIndex = startIndex + 1;
		
		for (NamedEntity ne: this.namedEntities){
			if (ne.getName().contains(head.getName()) && ne.getStartIndex() <= startIndex && ne.getEndIndex() >= endIndex){
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return "NamedEntityList [namedEntities=" + namedEntities + "]";
	}

}
