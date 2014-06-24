package cscie97.asn4.ecommerce.product;

import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;
import cscie97.asn4.ecommerce.authentication.PermissionException;

/**
 * {@code Catalog} provides the public methods for a Catalog Service.
 */
public interface Catalog {

	/**
	 * Imports data from CSV-formatted file into the Catalog.
	 * 
	 * @param fileName file containing data to import into Catalog
	 * @param dataType type of data contained in import file
	 * @param accessToken token restricting access to import
	 * @throws ImportException when an error is encountered accessing or processing the import file
	 * @throws InvalidAccessTokenException 
	 * @throws PermissionException 
	 */
	public void importCSV(String fileName, String dataType, String accessToken) throws ImportException, PermissionException, InvalidAccessTokenException;
	
	/**
	 * Parses and triggers execution of searches read from CSV-formatted file.
	 * 
	 * @param fileName file containing searches
	 * @throws SearchEngineException when an error is encountered accessing or processing the search file.
	 */
	public void executeSearchFile(String fileName) throws SearchEngineException;
}
