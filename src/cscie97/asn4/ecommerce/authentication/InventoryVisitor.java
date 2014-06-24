package cscie97.asn4.ecommerce.authentication;

/**
 * {@code InventoryVisitor} serves as a Visitor that manages the display of
 * the Authentication Service API's inventory items.
 */
public class InventoryVisitor implements Visitor{
	
	/**
	 * Default InventoryVisitor constructor
	 */
	protected InventoryVisitor(){
	}

	/**
	 * Prints the id, name, description, and permissions of the Service.
	 * 
	 * @param service	Service to visit
	 */
	@Override
	public void visit(Service service){
		System.out.println("ID: " + service.getId());
		System.out.println("Name: " + service.getName());
		System.out.println("Description: " + service.getDescription());
		System.out.println("Permissions:");
		for(Permission p : service.getPermissions()){
			System.out.println("\t" + p.getId());
		}
		System.out.println();	
	}

	/**
	 * Prints the id, name, credentials, roles, and permissions of the User.
	 * 
	 * @param user	User to visit
	 */
	@Override
	public void visit(User user){
		System.out.println("ID: " + user.getId());
		System.out.println("Name: " + user.getName());
		System.out.println("Credentials: ");
		for(Credential c : user.getCredentials()){
			System.out.println("\tUser Name: " + c.getUserName());
		}
		System.out.println("Roles: ");
		for(Role r : user.getRoles()){
			System.out.println("\t" + r.getId());
		}
		System.out.println("Permissions: ");
		for(Permission p: user.getPermissions()){
			System.out.println("\t" + p.getId());
		}	
		System.out.println();
	}

	/**
	 * Prints the id, name, description, sub-roles, and permissions of the Role.
	 * 
	 * @param role	Role to visit
	 */
	@Override
	public void visit(Role role){
		System.out.println("ID: " + role.getId());
		System.out.println("Name: " + role.getName());
		System.out.println("Description: " + role.getDescription());
		System.out.println("Sub-Roles: ");
		for(Role r : role.getSubRoles()){
			System.out.println("\t" + r.getId());
		}
		System.out.println("Permissions: ");
		for(Permission p: role.getPermissions()){
			System.out.println("\t" + p.getId());
		}		
		System.out.println();
	}

	/**
	 * Prints the id, name, description, and associated service of the Permission.
	 * 
	 * @param permission	Permission to visit
	 */
	@Override
	public void visit(Permission permission){
		System.out.println("ID: " + permission.getId());
		System.out.println("Name: " + permission.getName());
		System.out.println("Description: " + permission.getDescription());
		System.out.println("Service: " + permission.getServiceId());
		System.out.println();
	}
}
