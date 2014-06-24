package cscie97.asn4.ecommerce.collection;

/**
 * {@code CollectibleIterator} provides the public methods for iteration over collectible items.
 */
public interface CollectibleIterator {

	/**
	 * Sets the CollectibleIterator at the first item.
	 * @throws Exception if the Collection is empty
	 */
	public void first() throws Exception;

	/**
	 * Advances the CollectibleIterator to the next item.
	 * @throws Exception if the CollectibleIterator is at or past the end of the Collection
	 */
	public void next() throws Exception;
	
	/**
	 * Indicates whether CollectibleIterator has iterated over all the items.
	 * @return true if CollectibleIterator has completed iteration; false if not
	 */
	public boolean isDone();
	
	/**
	 * Returns the Collectible item at which the CollectibleIterator is positioned.
	 * 
	 * @return item at which the CollectibleIterator is positioned
	 * @throws Exception if Collection is empty or CollectibleIterator is past the end of
	 * the Collection
	 */
	public Collectible currentItem() throws Exception;	
}
	

