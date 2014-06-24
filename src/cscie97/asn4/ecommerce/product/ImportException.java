package cscie97.asn4.ecommerce.product;

/**
 * {@code ImportException} is an exception for errors encountered accessing or processing a CSV-formatted 
 * file for import (ex. file does not exist).
 */
public class ImportException extends Exception {
	// Line where import failed
	private String failedLine;
	
	// File where import failed
	private String failedFile;
	
	/*
	 * Default ImportException constructor
	 */
	public ImportException(){
		super();
	}
	
	/*
	 * ImportException constructor with details
	 */
	public ImportException(String msg, String line, String fileName){
		super(msg);
		this.setFailedLine(line);
		this.setFailedFile(fileName);
	}

	public void setFailedLine(String failedLine) {
		this.failedLine = failedLine;
	}

	public String getFailedLine() {
		if(failedLine == null){
			failedLine = "";
		}
		return failedLine;
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