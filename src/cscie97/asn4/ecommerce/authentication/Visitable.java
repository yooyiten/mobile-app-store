package cscie97.asn4.ecommerce.authentication;

/**
 * {@code Visitable} indicates that an object can accept a Visitor.
 */
public interface Visitable {
	
	/**
	 * Accepts a Visitor and invokes its visit method.
	 * 
	 * @param v 	the Visitor
	 */
	public void acceptVisitor(Visitor v);
}
