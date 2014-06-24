package cscie97.asn4.ecommerce.collection;

import java.util.*;

public class CollectionIterator implements CollectibleIterator {

	// CollectionIterator placeholder
	private int location;
	// List of items for iteration
	private List<Collectible> members;
	
	/**
	 * Default CollectionIterator Constructor
	 * 
	 * @param members list of Collectibles for iteration
	 */
	protected CollectionIterator(List<Collectible> members){
		location = 0;
		this.setMembers(members); 
	}
	
	@Override
	public void first() throws CollectionIteratorException{
		if(this.members.isEmpty()){
			throw new CollectionIteratorException("There are no Collections to iterate.", null);
		}
		else{
			this.location = 0;
		}
	}

	@Override
	public void next() throws CollectionIteratorException{	
		if(this.location >= this.members.size()){
			throw new CollectionIteratorException("The CollectionIterator has finished " +
												  "iterating over all the items.", null);
		}
		else{
			this.location += 1;
		}
	}

	@Override
	public boolean isDone(){
		if(this.members.isEmpty() || (this.location >= this.members.size())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public Collectible currentItem() throws CollectionIteratorException{
		if(this.members.isEmpty()){
			throw new CollectionIteratorException("There are no Collections to iterate.", null);
		}
		else if(this.location >= (this.members.size())){
			throw new CollectionIteratorException("The CollectionIterator has finished " +
												  "iterating over all the items.", null);			
		}
		else{
			return members.get(this.location);
		}
	}

	public void setMembers(List<Collectible> members) {
		this.members = members;
	}


	public List<Collectible> getMembers() {
		return members;
	}
}
