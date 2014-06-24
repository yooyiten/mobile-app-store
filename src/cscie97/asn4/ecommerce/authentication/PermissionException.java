package cscie97.asn4.ecommerce.authentication;

/**
 * {@code PermissionException} is thrown by AuthenticationServiceAPI when an
 * error is encountered checking a user's permission to execute a method (ex. 
 * user does not have the permission).
 */
public class PermissionException extends Exception{

	private String user;
	private String permission;
	
	/**
	 * Default PermissionException constructor
	 */
	public PermissionException(){
		super();
	}
	
	/**
	 * PermissionException constructor with details
	 * 
	 * @param msg	exception message
	 * @param user	user for whom permission check failed
	 * @param perm  permission for which check failed
	 */
	public PermissionException(String msg, String user, String perm){
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
