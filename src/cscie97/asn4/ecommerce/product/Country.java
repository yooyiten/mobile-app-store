package cscie97.asn4.ecommerce.product;

/**
 * {@code Country} represents countries that a product can be distributed to.
 */
public class Country {
	// The Country's code
	private final String country_code;
	
	// The Country's name
	private final String country_name;
	
	// The Country's export status
	private final String export_status;
	
	/**
	 * Default Country constructor
	 * 
	 * @param code Country's code
	 * @param name Country's name
	 * @param status Country's export status
	 */
	protected Country(String code, String name, String status){
		this.country_code = code;
		this.country_name = name;
		this.export_status = status;
	}
	
	public String getCountryCode(){
		return this.country_code;
	}

	public String getCountryName(){
		return this.country_name;
	}
	
	public String getCountryStatus(){
		return this.export_status;
	}
}
