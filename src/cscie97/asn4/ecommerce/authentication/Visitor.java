package cscie97.asn4.ecommerce.authentication;

/**
 * {@code Visitor} is used for the Authentication Service's Visitors and
 * makes use of an overloaded visit method to which Inventory objects are
 * passed. The Visitor pattern is used by the Authentication Service in 
 * order to decouple from the Inventory objects the operations related to
 * displaying their details and verifying access.
 */
public interface Visitor {
	
	/**
	 * Calls the Visitor's visit method on a Service.
	 * 
	 * @param service	Service object
	 */
	public void visit(Service service);
	
	/**
	 * Calls the Visitor's visit method on a User.
	 * 
	 * @param user		User object
	 */
	public void visit(User user);
	
	/**
	 * Calls the Visitor's visit method on a Role.
	 * 
	 * @param role	Role object
	 */
	public void visit(Role role);
	
	/**
	 * Calls the Visitor's visit method on a Permission.
	 * 
	 * @param permission	Permission object
	 */
	public void visit(Permission permission);

}
