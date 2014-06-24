package cscie97.asn4.ecommerce.collection;

/**
 * {@code CollectionImportException} is an exception for errors encountered accessing or processing a 
 * CSV-formatted Collection file for import (ex. file has incorrect number of fields).
 */
public class CollectionImportException extends Exception {
	// line where exception occurred
	private String line;
	// file which caused the exception
	private String fileName;
	
	/*
	 * Default CollectionImportException constructor
	 */
	public CollectionImportException(){
		super();
	}
	
	/*
	 * CollectionImportException constructor with details
	 */
	public CollectionImportException(String msg, String line, String fileName){
		super(msg);
		this.line = line;
		this.fileName = fileName;

	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}