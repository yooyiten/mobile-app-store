package cscie97.asn4.ecommerce.authentication;

/**
 * {@code InvalidAccessTokenException} is thrown by AuthenticationServiceAPI
 * when an error is encountered when using an access token (ex. expired
 * access token is used for a restricted method, or access token doesn't
 * exist.
 */
public class InvalidAccessTokenException extends Exception{

	private String user;
	private String permission;
	
	/**
	 * Default InvalidAccessTokenException constructor
	 */
	public InvalidAccessTokenException(){
		super();
	}
	
	/**
	 * InvalidAccessTokenException constructor with details
	 * 
	 * @param msg	exception message
	 * @param user	user using the problematic access token
	 * @param perm	permission on which access token failed
	 */
	public InvalidAccessTokenException(String msg, String user, String perm){
		super(msg);
		this.setUser(user);
		this.setPermission(perm);
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
}
