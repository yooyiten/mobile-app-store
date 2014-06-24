package cscie97.asn4.ecommerce.authentication;

/**
 * {@code AccessVisitor} serves as a Visitor that manages the search for
 * a specified permission.
 */
public class AccessVisitor implements Visitor{
	
	private String permission;
	private boolean permissionFound;
	
	/**
	 * Default AccessVisitor constructor
	 */
	protected AccessVisitor(String permission){
		this.permission = permission;
		setPermissionFound(false);
	}
	
	/**
	 * Looks for a Permission within a Service.
	 * 
	 * @param service	Service to visit
	 */
	@Override
	public void visit(Service service){
		for(Permission p : service.getPermissions()){
			p.acceptVisitor(this);
		}
	}

	/**
	 * Looks for a Permission within a User's Roles and Permissions
	 * 
	 * @param user	User to visit
	 */
	@Override
	public void visit(User user){
		for(Role r : user.getRoles()){
			r.acceptVisitor(this);
		}
		for(Permission p: user.getPermissions()){
			p.acceptVisitor(this);
		}	
	}

	/**
	 * Looks for a Permission within a Role's sub-Roles and Permissions.
	 * 
	 * @param role	Role to visit
	 */
	@Override
	public void visit(Role role){	
		for(Role r : role.getSubRoles()){
			r.acceptVisitor(this);
		}
		for(Permission p: role.getPermissions()){
			p.acceptVisitor(this);
		}		
	}

	/**
	 * Checks if a visited Permission matches a specified Permission.
	 * 
	 * @param permission	Permission to visit
	 */
	@Override
	public void visit(Permission permission){
		if(permission.getId().equalsIgnoreCase(this.permission)){
			this.setPermissionFound(true);
		}
	}

	public void setPermissionFound(boolean permissionFound) {
		this.permissionFound = permissionFound;
	}

	public boolean isPermissionFound() {
		return permissionFound;
	}
}
