package cscie97.asn4.ecommerce.collection;

/**
 * {@code CollectibleException} is an exception for errors encountered creating a Collection or
 * Product (ex. data is incorrectly formatted).
 */
public class CollectibleException extends Exception {
	// line containing Collectible data where exception occurred
	private String line;
	
	/*
	 * Default CollectibleException constructor
	 */
	public CollectibleException(){
		super();
	}
	
	/*
	 * CollectibleException constructor with details
	 */
	public CollectibleException(String msg, String line){
		super(msg);
		this.setLine(line);
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}
}
