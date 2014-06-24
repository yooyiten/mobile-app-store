package cscie97.asn4.ecommerce.collection;

import cscie97.asn4.ecommerce.product.*;

import java.util.*;

/**
 * {@code DynamicCollection} extends the {@code Collection} class and represents a dynamic collection, 
 * wherein contained products are referenced by search criteria.
 */
public class DynamicCollection extends Collection{

	// DynamicCollection's product criteria
	private String[] criteria;
	
	/**
	 * Default DynamicCollection Constructor
	 * 
	 * @param id DynamicCollection identifier
	 * @param name DynamicCollection name
	 * @param description DynamicCollection description
	 */
	protected DynamicCollection(String id, String name, String description){
		super(id, name, description);
	}

	@Override
	public String getType() {
		return "dynamic_collection";
	}
	
	/**
	 * Interacts with the Product Service API to return the set of Content objects that match the
	 * DynamicCollection's search criteria.
	 * 
	 * @return Set of Content objects matching the search criteria
	 */
	public Set<Content> getProducts(){
		Set<Content> products = new HashSet<Content>();
    	ProductCatalog pc = ProductCatalog.getInstance();
    	try {
			products = pc.executeSearch(this.criteria);
		} catch (SearchEngineException see) {
			System.out.println("Error encountered querying Product API with search criteria");
			see.getMessage();
			System.exit(1);
		}
		return products;
	}
	
	public void setCriteria(String[] criteria) {
		this.criteria = criteria;
	}

	public String[] getCriteria() {
		return criteria;
	}

}
