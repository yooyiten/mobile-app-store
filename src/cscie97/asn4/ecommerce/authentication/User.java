package cscie97.asn4.ecommerce.authentication;

import java.util.*;

/**
 * {@code User} extends the {@code Inventory} class and represents a registered
 * user of the Mobile Application Store. A User may have zero or more roles and
 * permissions, which indicate which restricted methods she can execute, and is
 * able to log into the store using assigned credentials.
 */
public class User extends Inventory{

	private Set<Credential> credentials;
	private Set<Role> roles;
	private Set<Permission> permissions;
	
	/**
	 * Default User constructor
	 * 
	 * @param id	user id
	 * @param name	user name
	 */
	protected User(String id, String name){
		super(id, name);
		this.credentials = new HashSet<Credential>();
		this.roles = new HashSet<Role>();
		this.permissions = new HashSet<Permission>();
	}
	
	/**
	 * Accepts a Visitor and invokes its visit method.
	 * 
	 * @param v 	the Visitor
	 */
	@Override
	public void acceptVisitor(Visitor v){
		v.visit(this);
	}

	public void setCredentials(Credential credential) {
		this.credentials.add(credential);
	}

	public Set<Credential> getCredentials() {
		return credentials;
	}

	public void setRoles(Role role) {
		this.roles.add(role);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setPermissions(Permission permission) {
		this.permissions.add(permission);
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}
}
