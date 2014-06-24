package cscie97.asn4.ecommerce.authentication;

import java.util.*;

/**
 * {@code Role} extends the {@code Inventory} class and represents a defined 
 * role in the Mobile Application Store. A Role is a grouping of sub-roles and
 * permissions and is a means of assigning permissions to users in a 
 * standardized fashion.
 */
public class Role extends Inventory{

	private final String description;
	private Set<Role> subRoles;
	private Set<Permission> permissions;
	
	/**
	 * Default Role constructor
	 * 
	 * @param id			Role id
	 * @param name			Role name
	 * @param description	Role description
	 */
	protected Role(String id, String name, String description){
		super(id, name);
		this.description = description;
		this.subRoles = new HashSet<Role>();
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

	public String getDescription() {
		return description;
	}

	public void setSubRoles(Role r) {
		this.subRoles.add(r);
	}

	public Set<Role> getSubRoles() {
		return subRoles;
	}

	public void setPermissions(Permission p) {
		this.permissions.add(p);
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}	
}
