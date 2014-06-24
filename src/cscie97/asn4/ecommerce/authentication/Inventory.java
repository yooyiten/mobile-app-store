package cscie97.asn4.ecommerce.authentication;

/**
 * {@code Inventory} is the abstract class for the different types of items
 * that compose the inventory of the Authentication Service.
 */
public abstract class Inventory implements Visitable{
	
	private final String id;
	private final String name;
	
	/**
	 * Default Inventory constructor
	 * 
	 * @param id	Inventory item id
	 * @param name	Inventory item name
	 */
	protected Inventory(String id, String name){
		this.id = id;
		this.name= name;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
