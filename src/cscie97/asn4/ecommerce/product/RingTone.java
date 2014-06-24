package cscie97.asn4.ecommerce.product;

import java.util.*;

/**
 * {@code RingTone} extends the {@code Content} class and represents a ringtone product in the Product
 * Catalog.
 */
public class RingTone extends Content{
	/**
	 * Default RingTone constructor
	 * 
	 * @param id RingTone's ID
	 * @param name RingTone's name
	 * @param desc RingTone's description
	 * @param cats RingTone's categories
	 * @param auth RingTone's author
	 * @param rating RingTone's rating
	 * @param price RingTone's price
	 * @param langs RingTone's supported languages
	 * @param url RingTone's image URL
	 * @param countries countries to which RingTone can be distributed
	 */
	protected RingTone(String id, String name, String desc, Set<String> cats, String auth, int rating, 
					   float price, Set<String> langs, String url, Set<Country> countries, 
					   Set<Device> devices){
		super(id, name, desc, cats, auth, rating, price, langs, url, countries, devices);
	}
}
