package cscie97.asn4.ecommerce.authentication;

/**
 * {@code AuthenticationException} is an exception for errors encountered
 * authenticating a user's credentials upon login (ex. username is not in the
 * system or password is incorrect
 */
public class AuthenticationException extends Exception{

	private String userName;
	
	/**
	 * Default AuthenticationException constructor
	 */
	public AuthenticationException(){
		super();
	}
	
	/**
	 * AuthenticationException constructor with details
	 */
	public AuthenticationException(String msg, String user){
		super(msg);
		this.setUserName(user);
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
