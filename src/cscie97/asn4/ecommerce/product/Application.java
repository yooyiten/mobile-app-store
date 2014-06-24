package cscie97.asn4.ecommerce.product;

import java.util.*;

/**
 * {@code Application} extends the {@code Content} class and represents an application product in the 
 * Product Catalog.
 */
public class Application extends Content{
	// The Application's size
	private final byte size;
	
	/**
	 * Default Application constructor
	 * 
	 * @param id Application's id
	 * @param name Application's name
	 * @param desc Application's description
	 * @param cats Application's categories
	 * @param auth Application's author
	 * @param rating Application's rating
	 * @param price Application's price
	 * @param langs Application's supported languages
	 * @param url Application's image URL
	 * @param countries countries to which Application can be distributed
	 * @param size Application's size
	 */
	protected Application(String id, String name, String desc, Set<String> cats, String auth, int rating, 
						  float price, Set<String> langs, String url, Set<Country> countries, 
						  Set<Device> devices, byte size){
		super(id, name, desc, cats, auth, rating, price, langs, url, countries, devices);
		this.size = size;
	}

	public byte getSize() {
		return size;
	}
}
