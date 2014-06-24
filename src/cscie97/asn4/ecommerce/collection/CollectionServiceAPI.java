package cscie97.asn4.ecommerce.collection;

import java.io.*;
import java.util.*;

import cscie97.asn4.ecommerce.product.*;
import cscie97.asn4.ecommerce.authentication.*;

/**
 * {@code CollectionServiceAPI} is responsible for creating and updating Collections, and providing
 * access to Collection search and iteration.
 */
public class CollectionServiceAPI implements CollectionService {

    // The active set of valid Collections
    private Map<String, Collection> collectionMap;
    // The active set of valid Products
    private Map<String, Product> productMap;
    // The lookup map for text search criteria
    private Map<String, Set<Collection>> searchTextMap;
    
    /**
     * Private CollectionServiceAPI constructor
     */
    private CollectionServiceAPI(){
        this.collectionMap = new TreeMap<String, Collection>();
        this.productMap = new TreeMap<String, Product>();
        this.searchTextMap = new TreeMap<String, Set<Collection>>();
    }
    
    /**
     * {@code CollectionServiceAPISingleton} is loaded on the first execution of
     * CollectionServiceAPI.getInstance() (Bill Pugh's technique).
     *
     */
    private static class CollectionServiceAPISingleton{
        public static final CollectionServiceAPI csa = new CollectionServiceAPI();
    }
   
    /**
     * Creates singleton instance of CollectionServiceAPI
     *
     * @return single static instance of CollectionServiceAPI
     */
    public static CollectionServiceAPI getInstance(){
        return CollectionServiceAPISingleton.csa;
    }       
    
    /**
     * Updates the searchTextMap with the name and description of the Collection.
     * 
     * @param c Collection to add to searchTextMap
     */
    private void updateSearchTextMap(Collection c){

    	String delims = " +"; // whitespace regex
    	String punc = "\\p{P}"; // punctuation regex
    	
    	// remove punctuation from collection name and description, parse on whitespace,
    	// and create a set from the two arrays so we that we only check and add unique words
    	String[]textName = c.getName().toLowerCase().replaceAll(punc, "").split(delims);
    	String[] textDesc = c.getDescription().toLowerCase().replaceAll(punc, "").split(delims);

    	Set<String> textCombined = new HashSet<String>();
    	for(String t:textName){
    		textCombined.add(t);
    	}
    	for(String t:textDesc){
    		textCombined.add(t);
    	}

    	for(String str: textCombined){
        	// add the collection to the searchTextMap for each word
        	Set<Collection> colls = new HashSet<Collection>();
    		String word = str;
    		if(!this.searchTextMap.containsKey(word) || this.searchTextMap.get(word) == null){
    			colls.add(c);
        		this.searchTextMap.put(word, colls);		
    		}
    		else{
    			this.searchTextMap.get(word).add(c);
    		}
    	}
    }
    
    /**
     * Adds a child Product to a Collection
     * 
     * @param collId ID of collection to add content to
     * @param contId ID of product to add to collection
     * @throws CollectibleException if collection is not static or product doesn't exist
     */
    private void addProduct(String collId, String contId) throws CollectibleException{
    	ProductCatalog pc = ProductCatalog.getInstance();
    	
    	if(this.collectionMap.get(collId).getType() != "static_collection"){
    		throw new CollectibleException("Error encountered adding product to Collection: " +
    									   "Collection " + collId + " is not static.", null);
    	}
    	else{
			Set<Product> setProd = new HashSet<Product>();
    		// if product is already in map, update the collection's product list
			if(this.productMap.containsKey(contId)){ 
				if(((StaticCollection) this.collectionMap.get(collId)).getProducts() != null){					
					setProd = ((StaticCollection) this.collectionMap.get(collId)).getProducts();
				}
				setProd.add(this.productMap.get(contId));
				((StaticCollection) this.collectionMap.get(collId)).setProducts(setProd);
				System.out.println("Product " + contId + " has been added to Collection " + collId);
			}
			// if product is not in map but exists in ProductCatalog, create new product
			// and add to productMap and collection's product list
			else if(!this.productMap.containsKey(contId) && pc.getProduct(contId) != null){
				Product p = new Product(contId);
				this.productMap.put(contId, p);
				
				if(((StaticCollection) this.collectionMap.get(collId)).getProducts() != null){
					setProd = ((StaticCollection) this.collectionMap.get(collId)).getProducts();
				}
				setProd.add(p);
				((StaticCollection) this.collectionMap.get(collId)).setProducts(setProd);
				System.out.println("Product " + contId + " has been created and added to " +
								   "Collection " + collId);
			}
			else{
				System.out.println(contId);
				throw new CollectibleException("Error encountered adding content to Collection: " +
											   "Product " + contId + " does not exist in system", null);
			} 		
    	}
    }
    
    /**
     * Adds a child collection to a collection.
     * 
     * @param collId Collection to add the child Collection to
     * @param contId Child collection to be added
     * @throws CollectibleException  if child collection doesn't exist
     */
    private void addChildColl(String collId, String contId) throws CollectibleException{
    	if(this.collectionMap.containsKey(contId)){
    		Set<Collection> setColl = new HashSet<Collection>();
    		if(this.collectionMap.get(collId).getChildren() != null){
    			setColl = this.collectionMap.get(collId).getChildren();
    		}
			setColl.add(this.collectionMap.get(contId));
    		this.collectionMap.get(collId).setChildren(setColl);
    		System.out.println("Child collection " + contId + " has been added to " +
    						   "collection " + collId);
    	}
    	else{
			throw new CollectibleException("Error encountered adding child Collection: " +
					"child Collection " + collId + " does not exist in system", null);   		
    	}
    }
    
    /**
     * Recursively builds a depth-first list of child Collections and Products.
     * 
     * @param collId Collection to build from
     * @param members list of Collectibles
     * @return built list of Collectibles
     */
    private List<Collectible> buildMemberList(String collId, List<Collectible> members){
    	Collection coll = this.collectionMap.get(collId);
    	
    	if(members != null && members.contains(coll)){
    		return members; // avoid repeat collections in members; creates cycle
    	}
    	
    	members.add(coll); // add collection to member list
    	Set<Product> prods = new HashSet<Product>();
    	if(coll.getType().equals("static_collection")){
    		prods = ((StaticCollection) coll).getProducts();
    	}
    	else{
    		Set<Content> cont = ((DynamicCollection) coll).getProducts();
    		// add products to productMap if not already there
    		for(Content c : cont){
    			String pid = c.getId();
    			Product p = new Product(pid);
    			this.productMap.put(pid, p);
    			prods.add(p);
    		} 		
    	}
    	for(Product p : prods){ // add collection's products to member list
    		members.add(p);
    	}
    	// add the child Collections and products
    	Set<Collection> children = coll.getChildren();
    	if(children == null){
    		return members;
    	}
    	else{
    		for(Collection c : children){
    			members = buildMemberList(c.getId(), members);
    		}
    	}
    	return members;
    }
    
    /**
     * Prints Collection search results.
     * 
     * @param results Collection search results
     */
    private void displayResults(Set<Collection> results){
    	if(results == null){
    		System.out.println("There were no search results.");
    	}
    	else{
    		System.out.println("Results:\n");
    		for(Collection r : results){
    			try {
					CollectionIterator rIter = createIterator(r.getId());
					while(!rIter.isDone()){
						Collectible curr = rIter.currentItem();
						if(curr.getType().equals("product")){
							Content c = ((Product) curr).getProduct();
							System.out.println("\tProduct ID: " + ((Product) curr).getId());
							System.out.println("\tName: " + c.getName());
							System.out.println("\tDescription: " + c.getDescription());
							System.out.println("\tType: " + c.getClass());
							System.out.println();
						}
						else{
							System.out.println("Collection ID: " + ((Collection) curr).getId());	
							System.out.println("Name: " + ((Collection) curr).getName());
							System.out.println("Description: " + ((Collection) curr).getDescription());
							System.out.println("Type: " + ((Collection) curr).getType());
							System.out.println();
						}
						rIter.next();
					}					
				} catch (CollectibleException ce) {
					System.out.println(ce.getMessage());
					System.exit(1);
				} catch (CollectionIteratorException cie){
					System.out.println(cie.getMessage());
					System.exit(1);
				}
    		}
    	}
    }
    
	@Override
	public void importCSV(String accessToken, String fileName) 
		throws CollectionImportException, PermissionException, InvalidAccessTokenException {
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "collection_import";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}        
		try{
			FileReader fr;
			fr = new FileReader(fileName);
	        BufferedReader br = new BufferedReader(fr);
	       
	        String line;
	        String delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
	        String[] parsed;
	        String command;
	       
	        try {
				while((line = br.readLine()) != null){
				    // skip blank or comment lines
				    if((line.trim()).equals("") || line.trim().startsWith("#")){
				        continue;
				    }
				    parsed = line.trim().split(delims);
				    command = parsed[0].toLowerCase();
				    
				    // look at command and delegate to appropriate method
				    if(command.equals("define_collection")){
				    	createCollection(accessToken, line);
				    }
				    else if(command.equals("add_collection_content")){
				    	addContent(accessToken, line);
				    }
				    else if(command.equals("set_dynamic_criteria")){
				    	setCriteria(accessToken, line);
				    }
				    else if(command.equals("search_collection")){
				    	searchCollections(line);
				    }
				    else{
				    	throw new CollectionImportException("Collection data import error: unrecognized " +
				    										"command in file " + fileName + " on line " +
				    										line + ".", line, fileName);
				    }  
				}
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
				System.exit(1);
			} catch (CollectibleException ce) {
				throw new CollectionImportException(ce.getMessage(), ce.getLine(), fileName);
			} catch (CollectionSearchException cse) {
				throw new CollectionImportException(cse.getFileName(), cse.getLine(), fileName);
			}
        }
        catch(FileNotFoundException fe){
			System.out.println(fe.getMessage());
			System.exit(1);
        }
	}

	@Override
	public void createCollection(String accessToken, String collectionData)
			throws CollectibleException, PermissionException, InvalidAccessTokenException {
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "create_collection";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}   
		String delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
		String[] parsed;
		String type;
		String id;
		String name;
		String desc;

		parsed = collectionData.split(delims);
		if(parsed.length != 5){
			throw new CollectibleException("Error encountered creating Collection: Incorrect number of " +
										   "fields (5) in line \"" + collectionData + "\"", collectionData);
		}
		else{
			type = parsed[1].toLowerCase().trim();
			id = parsed[2].toLowerCase().trim();
			name = parsed[3].trim();
			desc = parsed[4].trim();
		}
		
		if(!this.collectionMap.containsKey(id)){ // check whether the collection exists
			// add collection and update the searchTextMap
			if(type.equals("static")){
				StaticCollection sc = new StaticCollection(id, name, desc);
				this.collectionMap.put(id, sc);
				updateSearchTextMap(sc);
				System.out.println("Collection " + id + " was added!");
				
			}
			else if (type.equals("dynamic")){
				DynamicCollection dc = new DynamicCollection(id, name, desc);
				this.collectionMap.put(id, dc);
				updateSearchTextMap(dc);
				System.out.println("Collection " + id + " was added!");
			}
			else{
				throw new CollectibleException("Error encountered creating Collection: Invalid type of " +
											   type + " in line \"" + collectionData + "\"", collectionData);			
			}			
		}
		else{
			System.out.println("Collection " + id + " is already in the system.");
		}
	}	

	@Override
	public void addContent(String accessToken, String contentData)
			throws CollectibleException, PermissionException, InvalidAccessTokenException {
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "add_content";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}   		
		String delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
		String[] parsed;
		String collId;
		String type;
		String contId;

		parsed = contentData.split(delims);
		if(parsed.length != 4){
			throw new CollectibleException("Error encountered adding content to Collection: Incorrect " +
										   "number of fields (4) in line \"" + contentData +
										   "\"", contentData);
		}
		else{
			collId = parsed[1].toLowerCase().trim();
			type = parsed[2].toLowerCase().trim();
			contId = parsed[3].toLowerCase().trim();
		}
		
		// call methods to add product or collection
		if(this.collectionMap.containsKey(collId)){ // check whether the collection exists
			if(type.equals("product")){
				try{
					addProduct(collId, contId);					
				}
				catch(CollectibleException ce){
					System.out.println(ce.getMessage());
					System.exit(1);
				}
			}
			else if (type.equals("collection")){
				try{
					addChildColl(collId, contId);
				}
				catch(CollectibleException ce){
					System.out.println(ce.getMessage());
					System.exit(1);
				}
			}
			else{
				throw new CollectibleException("Error encountered adding content to Collection: Invalid " +
											   "content type " + type + "in line \"" + contentData + 
											   "\"", contentData);		
			}
		}
		else{
			throw new CollectibleException("Error encountered adding content to Collection: " +
										   "Collection in line \"" + contentData + "\" does not exist in system", 
										   contentData);
		}
	}

	@Override
	public void setCriteria(String accessToken, String criteriaData)
			throws CollectibleException, PermissionException, InvalidAccessTokenException {
		try{
	    	AuthenticationServiceAPI as = AuthenticationServiceAPI.getInstance();
			String perm = "add_content";
			boolean hasAccess = as.verifyAccess(accessToken, perm);
			
			if(!hasAccess){
				throw new PermissionException("User is not authorized to perform " +
					"this function", null, perm);
			}			
		} catch(InvalidAccessTokenException e){
			throw e;
		}   
        String delims = "(?<!\\\\),"; // negative lookbehind regex for comma not preceded by backslash
        String parsed[];
        String collId;
        
        parsed = criteriaData.split(delims, -1); //we want to keep trailing blanks
		if(parsed.length != 10){
			throw new CollectibleException("Error encountered adding criteria to Collection: Incorrect " +
										   "number of fields (10) in line \"" + criteriaData +
										   "\"", criteriaData);
		}
		// check if collection exists and is dynamic
		else{
			collId = parsed[1].toLowerCase().trim();
	    	if(!this.collectionMap.containsKey(collId)){
	    		throw new CollectibleException("Error encountered adding criteria to Collection: " +
	    									   "Collection " + collId + " is not in system.", criteriaData);
	    	}
	    	else if(this.collectionMap.get(collId).getType() != "dynamic_collection"){
	    		throw new CollectibleException("Error encountered adding criteria to Collection: " +
						   "Collection " + collId + " is not dynamic.", criteriaData);	    		
	    	}
	    	else{
	    		String criteria[] = Arrays.copyOfRange(parsed, 2, 10);
	    		((DynamicCollection) this.collectionMap.get(collId)).setCriteria(criteria);
	    		System.out.println("Search criteria has been added to collection " +
	    						   collId);
	    	}
		} 
	}

	@Override
	public Set<Collection> searchCollections(String searchData)
			throws CollectionSearchException {
    	String lineDelims = ",";
    	String wordDelims = " +"; // whitespace regex
    	String[] parsed;
    	String[] words;
    	Set<Collection> results = new HashSet<Collection>();
    	
    	parsed = searchData.split(lineDelims, -1); // we want to keep trailing blanks
		if(parsed.length != 2){
			throw new CollectionSearchException("Error encountered searching Collections: Incorrect " +
										   		"number of fields (2) in line \"" +
										   		searchData + "\"", searchData, null);
		}
		else{
			System.out.println("\nprocessing " + searchData + "\n");
			words = parsed[1].toLowerCase().trim().split(wordDelims); 
	    	if(words[0].equals("")){ // blank criteria returns all collections
	    		for(Object c : this.collectionMap.values()){
	    			results.add((Collection) c);
	    		}
	    	}
	    	else{
	    		for(int i = 0; i < words.length; i++){
	    			String w = words[i];
	    			if(this.searchTextMap.get(w) != null){
	    				results.addAll(this.searchTextMap.get(w));
	    			}
	    		}
	    	}
		}    
		displayResults(results);
		return results;
	}

	@Override
	public CollectionIterator createIterator(String idColl) throws CollectibleException{
		if(this.collectionMap.get(idColl) == null){
			throw new CollectibleException("Cannot create CollectionIterator for " +
										   "Collection " + idColl + ". Collection does not exist.", null);
		}
		else{
			// build the member list with recursive helper function
			List<Collectible> members = new ArrayList<Collectible>();
			members = buildMemberList(idColl, members);		
			// create the new CollectionIterator with the member list
			CollectionIterator ci = new CollectionIterator(members);		
			// return the new CollectionIterator
			return ci;
		}
	}
}
