package cscie97.asn4.ecommerce.authentication;

/**
 * {@code AuthenticationService} provides the public methods for the 
 * AuthenticationServiceAPI.
 */
public interface AuthenticationService {

	/**
	 * Logs user into Mobile Application Store. AccessToken is generated and
	 * its guid returned upon successful authentication of user credentials.
	 * 
	 * @param userName	user name
	 * @param password	password
	 * @return	AccessToken guid
	 * @throws AuthenticationException on error authenticating credentials, i.e.
	 * 		   username is not in system or user already has an active session
	 */
	public String login(String userName, String password) throws AuthenticationException;
	
	/**
	 * Logs user out of Mobile Application Store. AccessToken is invalidated.
	 * 
	 * @param accessToken AccessToken guid
	 * @throws InvalidAccessTokenException on error evaluating access token, i.e.
	 * 		   access token doesn't exist
	 */
	public void logout(String accessToken) throws InvalidAccessTokenException;
	
	/**
	 * Imports data from CSV-formatted file for processing by the Authentication
	 * Service. Delegates to other methods for creating objects from the data.
	 * 
	 * @param accessToken AccessToken guid
	 * @param fileName name of file to be imported
	 * @throws AuthenticationImportException on error accessing or processing
	 * 		   the file, i.e. file doesn't exist or has incorrect number of fields
	 * @throws PermissionException if user does not have permission to import 
	 * 		   CSV files
	 * @throws InvalidAccessTokenException if access token does not exist
	 */
	public void importCSV(String accessToken, String fileName) 
		throws AuthenticationImportException, PermissionException, 
			   InvalidAccessTokenException, AuthenticationDataException;
	
	/**
	 * Creates a new Service.
	 * 
	 * @param accessToken AccessToken guid
	 * @param serviceData line of Service data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the Service data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add Services
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addService(String accessToken, String serviceData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Creates a new User.
	 * 
	 * @param accessToken AccessToken guid
	 * @param userData line of User data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the User data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add Users
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addUser(String accessToken, String userData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Creates a new Role.
	 * 
	 * @param accessToken AccessToken guid
	 * @param roleData line of Role data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the Role data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add Roles
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addRole(String accessToken, String roleData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Creates a new Permission
	 * 
	 * @param accessToken AccessToken guid
	 * @param permissionData line of User data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the Permission data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add Permissions
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addPermission(String accessToken, String permissionData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Creates and adds Credential to an existing User
	 * 
	 * @param accessToken AccessToken guid
	 * @param credentialData line of Credential data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the Credential data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add Credentials
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addCredential(String accessToken, String credentialData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Adds an existing entitlement (Role or Permission) to an existing Role.
	 * 
	 * @param accessToken AccessToken guid
	 * @param entitlementData line of entitlement data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the entitlement data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add entitlements to Roles
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addRoleEntitlement(String accessToken, String entitlementData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Adds an existing entitlement (Role or Permission) to an existing User.
	 * 
	 * @param accessToken AccessToken guid
	 * @param entitlementData line of entitlement data to process
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the entitlement data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add entitlements to Users
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public void addUserEntitlement(String accessToken, String entitlementData)
		throws AuthenticationDataException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Displays the inventory of the Authentication Service.
	 */
	public void showInventory();
	
	/**
	 * Verifies whether a User has the given permission and an active access
	 * token for executing a restricted method.
	 * 
	 * @param accessToken AccessToken guid
	 * @param permission permission to check access for
	 * @return true if user has access, false if not
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 */
	public boolean verifyAccess(String accessToken, String permission)
		throws InvalidAccessTokenException;
}
