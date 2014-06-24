package cscie97.asn4.ecommerce.product;

/**
 * {@code SearchEngineException} is an exception for errors encountered accessing or processing a search 
 * file (ex. data is incorrectly formatted).
 */
public class SearchEngineException extends Exception {
	// Line of search that was being processed
	private String search;
	
	// The problematic search file
	private String failedFile;
	
	/*
	 * Default SearchEngineException constructor
	 */
	public SearchEngineException(){
		super();
	}
	
	/*
	 * SearchEngineException constructor with details
	 */
	public SearchEngineException(String msg, String search, String fileName){
		super(msg);
		this.setSearch(search);
		this.setFailedFile(fileName);
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearch() {
		if(search == null){
			search = "";
		}
		return search;
	}

	public void setFailedFile(String failedFile) {
		this.failedFile = failedFile;
	}

	public String getFailedFile() {
		if(failedFile == null){
			failedFile = "";
		}
		return failedFile;
	}
}
