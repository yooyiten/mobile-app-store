package cscie97.asn4.ecommerce.product;

/** 
 * {@code Device} represents the devices that a product is compatible with.
 */
public class Device {
	// The Device's id
	private final String device_id;
	
	// The Device's name
	private final String device_name;
	
	//The device's manufacturer
	private final String manufacturer;
	
	/**
	 * Default Device constructor
	 * 
	 * @param code Device's id
	 * @param name Device's name
	 * @param status Device's manufacturer
	 */
	protected Device(String id, String name, String manufacturer){
		this.device_id = id;
		this.device_name = name;
		this.manufacturer = manufacturer;
	}
	
	public String getDeviceId(){
		return this.device_id;
	}

	public String getDeviceName(){
		return this.device_name;
	}
	
	public String getManufacturer(){
		return this.manufacturer;
	}
}
