package cscie97.asn4.ecommerce.authentication;

import java.util.*;

/**
 * {@code Service} extends the {@code Inventory} class and represents a 
 * service within the Mobile Application Store. It is considered part of the 
 * inventory of the Authentication Service API. A Service may have zero or
 * more permissions, which should correspond to its restricted public methods.
 */
public class Service extends Inventory{
	
	private final String description;
	private Set<Permission> permissions;
	
	/**
	 * Default Service constructor
	 * 
	 * @param id			Service id
	 * @param name			Service name
	 * @param description	Service description
	 */
	protected Service(String id, String name, String description){
		super(id, name);
		this.description = description;
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

	public void setPermissions(Permission permission) {
		this.permissions.add(permission);
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}
}
