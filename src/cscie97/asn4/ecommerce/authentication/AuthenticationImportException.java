package cscie97.asn4.ecommerce.authentication;

/**
 * {@code AuthenticationImportException} is thrown by AuthenticationServiceAPI
 * when an error is encountered importing data to be processed (ex. file has
 * incorrect number of fields).
 */
public class AuthenticationImportException extends Exception{
	
	private String fileName;
	private String line;
	
	/**
	 * Default AuthenticationImportException constructor
	 */
	public AuthenticationImportException(){
		super();
	}
	
	/**
	 * AuthenticationImportException constructor with details
	 * 
	 * @param msg	exception message
	 * @param file	file where exception occurred
	 * @param line	line where exception occurred
	 */
	public AuthenticationImportException(String msg, String file, String line){
		super(msg);
		this.setFileName(file);
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
