package cscie97.asn4.ecommerce.product;

/**
 * {@code CatalogException} is an exception for errors encountered creating a Country, Device, or Content 
 * object (ex. data is incorrectly formatted).
 */
public class CatalogException extends Exception {
	// Type of data that was being created
	private String dataType;
	
	// Data that was being created
	private String data;
	
	/*
	 * Default CatalogException constructor
	 */
	public CatalogException(){
		super();
	}
	
	/*
	 * CatalogException constructor with details
	 */
	public CatalogException(String msg, String type, String data){
		super(msg);
		this.setDataType(type);
		this.setData(data);
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataType() {
		if(dataType == null){
			dataType = "";
		}
		return dataType;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		if(data == null){
			data = "";
		}
		return data;
	}
}