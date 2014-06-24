package cscie97.asn4.ecommerce.collection;

import cscie97.asn4.ecommerce.product.*;

/**
 * {@code Product} is a wrapper class that represents the products that collections can contain.
 */
public class Product implements Collectible {

	// Product identifier
	private final String id;
	
	/**
	 * Default Product constructor
	 * 
	 * @param id Product identifier
	 */
	protected Product(String id){
		this.id = id;
	}
	
	@Override
	public String getType() {
		return "product";
	}
	
	/**
	 * Interacts with the Product Service API to return the Content object that the Product is
	 * wrapping.
	 * 
	 * @return Content object having the product identifier
	 */
	public Content getProduct(){
    	ProductCatalog pc = ProductCatalog.getInstance();
    	return pc.getProduct(this.id);
	}

	public String getId() {
		return id;
	}
}
