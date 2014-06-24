package cscie97.asn4.ecommerce.collection;

import java.util.*;

/**
 * {@code StaticCollection} extends the {@code Collection} class and represents a static collection,
 * wherein contained products are stored by identifiers.
 */
public class StaticCollection extends Collection{

	// StaticCollection's products
	private Set<Product> products;
	
	/**
	 * Default StaticCollection Constructor
	 * 
	 * @param id StaticCollection identifier
	 * @param name StaticCollection name
	 * @param description StaticCollection description
	 */
	protected StaticCollection(String id, String name, String description){
		super(id, name, description);
		products = new HashSet<Product>();
	}
	
	@Override
	public String getType() {
		return "static_collection";
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<Product> getProducts() {
		return products;
	}
}

