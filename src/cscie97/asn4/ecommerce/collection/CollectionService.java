package cscie97.asn4.ecommerce.collection;

import java.util.*;

/**
 * {@code CollectionService} provides the public methods for the Collection Service's API.
 */
public interface CollectionService {

	/**
	 * Imports data from CSV-formatted file to be processed by Collection Service API.
	 * Delegates to createCollection, addContent, setCriteria, and searchCollections
	 * methods for the actions to be performed for the data.
	 * 
	 * @param accessToken access token restricting the method call
	 * @param fileName CSV-formatted file to import
	 * @throws Exception on error accessing or processing the file
	 */
	public void importCSV(String accessToken, String fileName) throws Exception;
	
	/**
	 * Creates a new Collection.
	 * 
	 * @param accessToken access token restricting the method call
	 * @param collectionData data for creating the new Collection
	 * @throws Exception on error parsing or validating the Collection data
	 */
	public void createCollection(String accessToken, String collectionData) throws Exception;
	
	/**
	 * Adds content to an existing Collection
	 * 
	 * @param accessToken access token restricting the method call
	 * @param contentData data to add to the Collection
	 * @throws Exception on error parsing or validating the content data
	 */
	public void addContent(String accessToken, String contentData) throws Exception;
	
	/**
	 * Adds product criteria to an existing Collection
	 * 
	 * @param accessToken access token restricting the method call
	 * @param criteriaData search criteria to add to the Collection
	 * @throws Exception on error parsing or validating the criteria data.
	 */
	public void setCriteria(String accessToken, String criteriaData) throws Exception;
	
	/**
	 * Searches the names and description of Collections for matches on provided criteria.
	 * 
	 * @param searchData text to search for in Collection names and descriptions
	 * @return Set of Collections matching the search criteria
	 * @throws Exception on error validating the search data.
	 */
	public Set<Collection> searchCollections(String searchData) throws Exception;
	
	/**
	 * Creates a new CollectibleIterator for the Collection
	 * 
	 * @param idColl Collection identifier
	 * @return CollectibleIterator for the Collection
	 * @throws Exception on error accessing Collection to iterate
	 */
	public CollectibleIterator createIterator(String idColl) throws Exception;	
}
