package cscie97.asn4.ecommerce.collection;

/**
 * {@code CollectionIteratorException} is an exception for errors encountered iterating over a 
 * Collection (ex. Collection is empty or Iterator is past the end of the Collection).
 */
public class CollectionIteratorException extends Exception {
	// Collection being iterated
	private Collection coll;
	
	/*
	 * Default CollectionIteratorException constructor
	 */
	public CollectionIteratorException(){
		super();
	}
	
	/*
	 * CollectionIteratorException constructor with details
	 */
	public CollectionIteratorException(String msg, Collection coll){
		super(msg);
		this.setColl(coll);
	}

	public void setColl(Collection coll) {
		this.coll = coll;
	}

	public Collection getColl() {
		return coll;
	}


}
