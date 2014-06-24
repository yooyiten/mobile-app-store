package cscie97.asn4.ecommerce.collection;

/**
 * {@code CollectionSearchException} is an exception for errors encountered accessing or processing
 * a search file (ex. search criteria is incorrectly formatted).
 */
public class CollectionSearchException extends Exception {
	// search file where exception occurred
	private String fileName;
	// line containing search data where exception occurred
	private String line;
	
	/*
	 * Default CollectionSearchException constructor
	 */
	public CollectionSearchException(){
		super();
	}
	
	/*
	 * CollectionSearchException constructor with details
	 */
	public CollectionSearchException(String msg, String line, String fileName){
		super(msg);
		this.setFileName(fileName);
		this.setLine(line);
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}
}
