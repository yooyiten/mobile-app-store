package cscie97.asn4.ecommerce.authentication;

/**
 * {@code AuthenticationDataException} is thrown by AuthenticationServiceAPI
 * when an error is encountered creating objects from data imported into
 * the Authentication Service (ex. data is incorrectly formatted).
 */
public class AuthenticationDataException extends Exception{

	private String line;
	
	/**
	 * Default AuthenticationDataException constructor
	 */
	public AuthenticationDataException(){
		super();
	}
	
	/**
	 * AuthenticationDataException constructor with details
	 * 
	 * @param msg	exception message
	 * @param line	line where exception occurred
	 */
	public AuthenticationDataException(String msg, String line){
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
