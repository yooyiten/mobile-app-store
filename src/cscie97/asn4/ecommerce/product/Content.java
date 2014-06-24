package cscie97.asn4.ecommerce.product;

import java.util.*;

/**
 * {@code Content} is the abstract class for the different types of products that can be in the 
 * Product Catalog. 
 */
public abstract class Content {
	// The Content's ID
	private final String id;
	
	// The Content's name
	private final String name;
	
	// The Content's description
	private final String description;
	
	// The categories to which the Content belongs
	private final Set<String> categories;
	
	// The Content's author
	private final String author;
	
	// The Content's rating
	private final int rating;
	
	// The Content's price
	private final float price;
	
	// The Content's supported languages
	private final Set<String> languages;
	
	// The Content's image URL
	private final String image_url;
	
	// The countries that the Content can be distributed to
	private final Set<Country> countries;
	
	// The devices that the Content is compatible with
	private final Set<Device> devices;
	
	/**
	 * Default Content constructor
	 * 
	 * @param id Content's ID
	 * @param name Content's name
	 * @param desc Content's description
	 * @param cats Content's categories
	 * @param auth Content's author
	 * @param rating Content's rating
	 * @param price Content's price
	 * @param langs Content's supported languages
	 * @param url Content's image URL
	 * @param countries countries to which Content can be distributed
	 * @param devices Content's supported devices
	 */
    protected Content(String id, String name, String desc, Set<String> cats, String auth, int rating, 
    				  float price, Set<String> langs, String url, Set<Country> countries, 
    				  Set<Device> devices){
    	this.id = id;
    	this.name = name;
    	this.description = desc;
    	this.categories= cats;
    	this.author = auth;
    	this.rating = rating;
    	this.price = price;
    	this.languages = langs;
    	this.image_url = url;
    	this.countries = countries;
    	this.devices = devices;    	
    }
    
    @Override
    public String toString(){
    	String print = this.name + "\n";
    	print = print + "Description: " + this.description + "\n";
    	print = print + "Categories: ";
    	Iterator<String> iCat = this.categories.iterator();
    	while(iCat.hasNext()){
    		print = print + iCat.next() +"|";
    	}
    	print = print + "\n";
    	print = print + "Rating: " + this.rating + "/5\n";
    	print = print + "Price: " + this.price + " BitCoin(s)\n";
    	print = print + "Languages: ";
    	Iterator<String> iLang = this.languages.iterator();
    	while(iLang.hasNext()){
    		print = print + iLang.next() + "|";
    	}
    	print = print + "\n";
    	print = print + "Countries: ";
    	Iterator<Country> iCtry = this.countries.iterator();
    	while(iCtry.hasNext()){
    		print = print + iCtry.next().getCountryName() + "|";
    	}
    	print = print + "\n";
    	print = print + "Devices: ";
    	Iterator<Device> iDev = this.devices.iterator();
    	while(iDev.hasNext()){
    		print = print + iDev.next().getDeviceName() + "|";
    	}
    	print = print + "\n";
    	print = print + "Made by " + this.author + "\n";
    	return print;
    }
    
    public String getId(){
    	return this.id;
    }

	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}

	public Set<String> getCategories() {
		return this.categories;
	}

	public String getAuthor() {
		return this.author;
	}

	public int getRating() {
		return this.rating;
	}

	public float getPrice() {
		return this.price;
	}

	public Set<String> getLanguages() {
		return this.languages;
	}

	public String getImageUrl() {
		return this.image_url;
	}

	public Set<Country> getCountries() {
		return this.countries;
	}

	public Set<Device> getDevices() {
		return this.devices;
	}	
}
