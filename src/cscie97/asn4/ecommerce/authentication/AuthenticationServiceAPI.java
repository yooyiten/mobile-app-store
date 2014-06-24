package cscie97.asn4.ecommerce.authentication;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * {@code AuthenticationServiceAPI} is responsible for maintaining the
 * inventory of Services, Users, Roles, and Permissions in the Mobile
 * Application Store, and oversees the authentication of users and the 
 * methods they are permitted to execute.
 */
public class AuthenticationServiceAPI implements AuthenticationService{

	private Map<String, Service> serviceMap;
	private Map<String, User> userMap;
	private Map<String, Role> roleMap;
	private Map<String, Permission> permissionMap;
	private Map<String, Credential> credentialMap;
	private Map<String, AccessToken> tokenMap;
	private Map<String, String> sessionMap;
	private long timeoutSpan;
	private String delims;
	private Service superService;
	private User superUser;
	private Credential superCred;
	
	/**
	 * Private AuthenticationServiceAPI constructor
	 * 
	 * @throws AuthenticationException
	 */
	private AuthenticationServiceAPI() throws AuthenticationException{
		this.serviceMap = new TreeMap<String, Service>();
		this.userMap = new TreeMap<String, User>();
		this.roleMap = new TreeMap<String, Role>();
		this.permissionMap = new TreeMap<String, Permission>();
		this.credentialMap = new TreeMap<String, Credential>();
		this.tokenMap = new TreeMap<String, AccessToken>();
		this.sessionMap = new TreeMap<String, String>();
		this.timeoutSpan = 3600000;
		this.delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
		this.superService = new Service("super_service", "Super Service", "Super User Access");
		this.serviceMap.put(this.superService.getId(), this.superService);
		this.superUser = new User("super_user", "Super User");
		this.userMap.put(this.superUser.getId(), this.superUser);
		try {
			this.superCred = new Credential("super_user", "super", "999");
		} catch (UnsupportedEncodingException e) {
			throw new AuthenticationException("Unable to launch Authentication Service",
					  						  null);
		} catch (NoSuchAlgorithmException e) {
			throw new AuthenticationException("Unable to launch Authentication Service",
					  						  null);
		} catch (InvalidKeySpecException e) {
			throw new AuthenticationException("Unable to launch Authentication Service",
					  						  null);
		}
		this.credentialMap.put(this.superCred.getUserName(), this.superCred);
		this.superUser.setCredentials(this.superCred);
	}
	
	/**
	 * {@code AuthenticationServiceAPISingleton is loaded on the first execution
	 * of AuthenticationServiceAPI.getInstance() (Bill Pugh's technique).
	 * 
	 * Handles exceptions via method found at http://stackoverflow.com/questions/2284502/singleton-and-exception
	 */
	private static class AuthenticationServiceAPISingleton{
		public static final AuthenticationServiceAPI asa;
		static{
			try{
				asa = new AuthenticationServiceAPI();
			} catch(AuthenticationException e){
				throw new ExceptionInInitializerError(e);
			}
		}
	}

	/**
	 * Returns Singleton instance of AuthenticationServiceAPI
	 * 
	 * @return single static instance of AuthenticationServiceAPI
	 */
	public static AuthenticationServiceAPI getInstance(){
		return AuthenticationServiceAPISingleton.asa;
	}
	
	/**
	 * Creates an AccessToken for a given username
	 * 
	 * @param userName username of account to create AccessToken for
	 * @return created AccessToken
	 */
	private AccessToken generateToken(String userName){
		AccessToken at = new AccessToken(userName, this.timeoutSpan);
		return at;
	}

	/**
	 * Logs user into Mobile Application Store. AccessToken is generated and
	 * its guid returned upon successful authentication of user credentials.
	 * 
	 * @param userName	user name
	 * @param password	password
	 * @return	AccessToken guid
	 * @throws AuthenticationException on error authenticating credentials, i.e.
	 * 		   username is not in system or password is incorrect
	 */
	@Override
	public String login(String userName, String password)
			throws AuthenticationException {
		if(this.credentialMap.get(userName) == null){
			throw new AuthenticationException("Username does not exist in system",
											  userName);
		}
		try {
			if(!this.credentialMap.get(userName).checkPassword(password)){
				throw new AuthenticationException("Password is incorrect",
												  userName);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new AuthenticationException("System is unable to check password",
											  userName);
		} catch (InvalidKeySpecException e) {
			throw new AuthenticationException("System is unable to check password",
					  userName);
		}
		// if user has active session, refresh and notify that already logged in
		if(this.sessionMap.get(userName) != null &&
		   this.tokenMap.get(this.sessionMap.get(userName)).isValid()){
				System.out.println("You are already logged in.");
				return this.tokenMap.get(this.sessionMap.get(userName)).getGuid();
		}
		// else generate new access token and update token and session maps
		else{
			AccessToken at = generateToken(userName);
			this.tokenMap.put(at.getGuid(), at);
			this.sessionMap.put(userName, at.getGuid());
			return at.getGuid();
		}
	}

	/**
	 * Logs user out of Mobile Application Store. AccessToken is invalidated.
	 * 
	 * @param accessToken AccessToken guid
	 * @throws InvalidAccessTokenException on error evaluating access token, i.e.
	 * 		   access token doesn't exist
	 */	
	@Override
	public void logout(String accessToken) throws InvalidAccessTokenException {
		if(this.tokenMap.get(accessToken) == null){
			throw new InvalidAccessTokenException("Access token does not exist",
												  null, null);
		}
		else{
			this.sessionMap.remove(this.tokenMap.get(accessToken).getUserName());
			this.tokenMap.get(accessToken).deactivate();
		}		
	}

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
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 * @throws AuthenticationDataException if invoked factory method finds error
	 * 		   in data
	 */	
	@Override
	public void importCSV(String accessToken, String fileName)
			throws AuthenticationImportException, PermissionException,
				   InvalidAccessTokenException, AuthenticationDataException {
		try{
			String perm = "authentication_import";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}

		FileReader fr;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			throw new AuthenticationImportException("File was not found", 
				fileName, null);
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
        String[] parsed;
        String command;
        
        try {
			while((line = br.readLine()) != null){
			    // skip blank or comment lines
			    if((line.trim()).equals("") || line.trim().startsWith("#")){
			        continue;     
			    }
			
			    parsed = line.trim().split(this.delims);
			    command = parsed[0];
			    
			    if(command.equalsIgnoreCase("define_service")){
			    	addService(accessToken, line);
			    }
			    else if(command.equalsIgnoreCase("define_permission")){
			    	addPermission(accessToken, line);
			    }
			    else if(command.equalsIgnoreCase("define_role")){
			    	addRole(accessToken, line);
			    }
			    else if(command.equalsIgnoreCase("add_entitlement_to_role")){
			    	addRoleEntitlement(accessToken, line);
			    }
			    else if(command.equalsIgnoreCase("create_user")){
			    	addUser(accessToken, line);
			    }
			    else if(command.equalsIgnoreCase("add_credential")){
			    	addCredential(accessToken, line);
			    }
			    else if(command.equalsIgnoreCase("add_entitlement_to_user")){
			    	addUserEntitlement(accessToken, line);
			    }
			    else{
			    	throw new AuthenticationImportException("Unrecognized import command",
			    											fileName, line);
			    }
			}
		} 
		catch (IOException e) {
			throw new AuthenticationImportException(e.getMessage(), fileName, null);
		} catch (AuthenticationDataException e) {
			throw e;
		}
	}

	/**
	 * Creates a new Service.
	 * 
	 * @param accessToken AccessToken guid
	 * @param serviceData line of Service data to process
	 * @throws InvalidAccessTokenException if access token does not exist or is
	 * 		   expired
	 * @throws AuthenticationDataException on error parsing or validating
	 * 		   the Service data
	 * @throws PermissionException if user does not have the permission to
	 * 		   add Services
	 */
	@Override
	public void addService(String accessToken, String serviceData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException{
		try{
			String perm = "define_service";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String id;
		String name;	
		String description;
		
		parsed = serviceData.split(this.delims);
		if(parsed.length != 4){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  serviceData);
		}
		else{
			id = parsed[1].trim().toUpperCase();
			name = parsed[2].trim();
			description = parsed[3].trim();
		}
		if(!this.serviceMap.containsKey(id)){
			Service s = new Service(id, name, description);
			this.serviceMap.put(id, s);
			System.out.println("Service " + id + " was added!");
		}
		else{
			System.out.println("Service " + id + " is already in the system.");
		}	
	}

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
	@Override
	public void addUser(String accessToken, String userData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException {
		try{
			String perm = "create_user";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String id;
		String name;	
		
		parsed = userData.split(this.delims);
		if(parsed.length != 3){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  userData);
		}
		else{
			id = parsed[1].trim().toUpperCase();
			name = parsed[2].trim();
		}
		if(!this.userMap.containsKey(id)){
			User u = new User(id, name);
			this.userMap.put(id, u);
			System.out.println("User " + id + " was added!");
		}
		else{
			System.out.println("User " + id + " is already in the system.");
		}
	}

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
	@Override
	public void addRole(String accessToken, String roleData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException {
		try{
			String perm = "define_role";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String id;
		String name;	
		String description;
		
		parsed = roleData.split(this.delims);
		if(parsed.length != 4){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  roleData);
		}
		else{
			id = parsed[1].trim().toUpperCase();
			name = parsed[2].trim();
			description = parsed[3].trim();
		}
		if(!this.roleMap.containsKey(id)){
			Role r = new Role(id, name, description);
			this.roleMap.put(id, r);
			System.out.println("Role " + id + " was added!");
		}
		else{
			System.out.println("Role " + id + " is already in the system.");
		}	
	}

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
	@Override
	public void addPermission(String accessToken, String permissionData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException {
		try{
			String perm = "define_permission";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String id;
		String name;	
		String description;
		String serviceId;
		
		parsed = permissionData.split(this.delims);
		if(parsed.length != 5){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  permissionData);
		}
		else{
			serviceId = parsed[1].trim().toUpperCase();
			id = parsed[2].trim().toUpperCase();
			name = parsed[3].trim();
			description = parsed[4].trim();
		}
		if(!this.serviceMap.containsKey(serviceId)){
			throw new AuthenticationDataException("Service " + serviceId +
				" does not exist", permissionData);
		}
		if(!this.permissionMap.containsKey(id)){
			Permission p = new Permission(id, name, description, serviceId);
			this.permissionMap.put(id, p);
			this.serviceMap.get(serviceId).setPermissions(p);
			System.out.println("Permission " + id + " was added!");
		}
		else{
			System.out.println("Permission " + id + " is already in the system.");
		}
	}

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
	@Override
	public void addCredential(String accessToken, String credentialData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException {
		try{
			String perm = "add_credential_to_user";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String userId;
		String userName;	
		String password;
		
		parsed = credentialData.split(this.delims);
		if(parsed.length != 4){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  credentialData);
		}
		else{
			userId = parsed[1].trim().toUpperCase();
			userName = parsed[2].trim();
			password = parsed[3].trim();
		}
		if(!this.userMap.containsKey(userId)){
			throw new AuthenticationDataException("User " + userId +
				"does not exist", credentialData);
		}
		if(this.credentialMap.containsKey(userName) &&
		   !this.credentialMap.get(userName).getUserId().equals(userId)){
			throw new AuthenticationDataException("This credential exists " +
				"for another user", credentialData);
		}
		else if(!this.credentialMap.containsKey(userName)){
			try {
				Credential c;
				c = new Credential(userId, userName, password);
				this.credentialMap.put(userName, c);
				this.userMap.get(userId).setCredentials(c);
				System.out.println("Credential " + userName + " was added!");
			} catch (UnsupportedEncodingException e) {
				throw new AuthenticationDataException("System is unable to " +
					"securely store password", credentialData);
			} catch (NoSuchAlgorithmException e) {
				throw new AuthenticationDataException("System is unable to " +
						"securely store password", credentialData);
			} catch (InvalidKeySpecException e) {
				throw new AuthenticationDataException("System is unable to " +
						"securely store password", credentialData);
			}
		}
		else{
			System.out.println("Credential " + userName + " is already in the system.");
		}
	}

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
	@Override
	public void addRoleEntitlement(String accessToken, String entitlementData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException {
		try{
			String perm = "add_entitlement_to_role";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String roleId;
		String entitlementId;
		
		parsed = entitlementData.split(this.delims);
		if(parsed.length != 3){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  entitlementData);
		}
		else{
			roleId = parsed[1].trim().toUpperCase();
			entitlementId = parsed[2].trim().toUpperCase();
		}
		if(!this.roleMap.containsKey(roleId)){
			throw new AuthenticationDataException("Role " + roleId +
				"does not exist", entitlementData);
		}
		if(!this.roleMap.containsKey(entitlementId) &&
		   !this.permissionMap.containsKey(entitlementId)){
			throw new AuthenticationDataException("Entitlement " + 
				entitlementId + " does not exist", entitlementData);
		}
		else if(this.roleMap.containsKey(entitlementId)){
			this.roleMap.get(roleId).setSubRoles(this.roleMap.get(entitlementId));
		}
		else{
			this.roleMap.get(roleId).setPermissions(this.permissionMap.get(entitlementId));
		}
		System.out.println("Entitlement " + entitlementId + " was added to role " +
				   roleId + "!");
	}

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
	@Override
	public void addUserEntitlement(String accessToken, String entitlementData)
			throws AuthenticationDataException, PermissionException, InvalidAccessTokenException {
		try{
			String perm = "add_entitlement_to_user";
			boolean hasAccess = verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", this.tokenMap.get(accessToken).getUserName(), perm);
			}		
			
		} catch(InvalidAccessTokenException e){
			throw e;
		}
		String[] parsed;
		String userId;
		String entitlementId;
		
		parsed = entitlementData.split(this.delims);
		if(parsed.length != 3){
			throw new AuthenticationDataException("Incorrect number of fields", 
												  entitlementData);
		}
		else{
			userId = parsed[1].trim().toUpperCase();
			entitlementId = parsed[2].trim().toUpperCase();
		}
		if(!this.userMap.containsKey(userId)){
			throw new AuthenticationDataException("User " + userId +
				"does not exist", entitlementData);
		}
		if(!this.roleMap.containsKey(entitlementId) &&
		   !this.permissionMap.containsKey(entitlementId)){
			throw new AuthenticationDataException("Entitlement " + 
				entitlementId + " does not exist", entitlementData);
		}
		else if(this.roleMap.containsKey(entitlementId)){
			this.userMap.get(userId).setRoles(this.roleMap.get(entitlementId));
		}
		else{
			this.userMap.get(userId).setPermissions(this.permissionMap.get(entitlementId));
		}
		System.out.println("Entitlement " + entitlementId + " was added to user " +
						   userId + "!");
	}

	/**
	 * Displays the inventory of the Authentication Service.
	 */
	@Override
	public void showInventory() {
		InventoryVisitor it = new InventoryVisitor();
		System.out.println("Service Inventory\n----------");
		for(Service s : this.serviceMap.values()){
			s.acceptVisitor(it);
		}
		System.out.println();
		System.out.println("User Inventory\n----------");	
		for(User u : this.userMap.values()){
			u.acceptVisitor(it);
		}
		System.out.println();
		System.out.println("Role Inventory\n----------");	
		for(Role r : this.roleMap.values()){
			r.acceptVisitor(it);
		}
		System.out.println();
		System.out.println("Permission Inventory\n----------");	
		for(Permission p : this.permissionMap.values()){
			p.acceptVisitor(it);
		}
		System.out.println();
	}

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
	@Override
	public boolean verifyAccess(String accessToken, String permission) 
		throws InvalidAccessTokenException {
		AccessToken token = this.tokenMap.get(accessToken);
		if(token == null){
			throw new InvalidAccessTokenException("Access token does not exist", 
												  null, permission);		
		}
		if(!token.isValid()){
			throw new InvalidAccessTokenException("Access token is expired",
												  token.getUserName(), permission);
		}
		if(this.tokenMap.get(accessToken).getUserName().equalsIgnoreCase("super")){
			return true;
		}
		else{
			AccessVisitor at = new AccessVisitor(permission);
			String userId = this.credentialMap.get(token.getUserName()).getUserId();
			this.userMap.get(userId).acceptVisitor(at);
			return at.isPermissionFound();
		}
	}
}
