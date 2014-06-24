package cscie97.asn4.ecommerce.product;

import java.util.*;

/**
 * {@code Wallpaper} extends the {@code Content} class and represents a wallpaper product in the Product
 * Catalog.
 */
public class Wallpaper extends Content{
	/**
	 * Default Wallpaper constructor
	 * 
	 * @param id Wallpaper's id
	 * @param name Wallpaper's name
	 * @param desc Wallpaper's description
	 * @param cats Wallpaper's categories
	 * @param auth Wallpaper's author
	 * @param rating Wallpaper's rating
	 * @param price Wallpaper's price
	 * @param langs Wallpaper's supported languages
	 * @param url Wallpaper's image URL
	 * @param countries countries to which Wallpaper can be distributed
	 */
	protected Wallpaper(String id, String name, String desc, Set<String> cats, String auth, int rating, 
						float price, Set<String> langs, String url, Set<Country> countries, 
						Set<Device> devices){
		super(id, name, desc, cats, auth, rating, price, langs, url, countries, devices);
	}
}
