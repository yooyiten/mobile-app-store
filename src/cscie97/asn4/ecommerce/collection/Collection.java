package cscie97.asn4.ecommerce.collection;

import java.util.*;


/**
 * {@code Collection} is the abstract class for the different types of collections that the
 * Mobile Application Store supports.
 */
public abstract class Collection implements Collectible {

	// Collection identifier
	private final String id;
	// Collection name
	private final String name;
	// Collection description
	private final String description;
	// Collection's child Collections
	private Set<Collection> children;
	
	/**
	 * Default Collection constructor
	 * 
	 * @param id Product identifier
	 */
	protected Collection(String id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
		children = new HashSet<Collection>();
	}
	
	@Override
	public String getType() {
		return "collection";
	}
	
	/**
	 * Returns an Iterator for the Collection.
	 * 
	 * @return Collection Iterator
	 */
	public CollectionIterator getIterator(){
		CollectionServiceAPI csa = CollectionServiceAPI.getInstance();
		CollectionIterator ci = null;
		try {
			ci = csa.createIterator(this.id);

		} catch (CollectibleException ce) {
			System.out.println(ce.getMessage());
			System.exit(1);
		}
		return ci;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setChildren(Set<Collection> children) {
		this.children = children;
	}

	public Set<Collection> getChildren() {
		return children;
	}	
}