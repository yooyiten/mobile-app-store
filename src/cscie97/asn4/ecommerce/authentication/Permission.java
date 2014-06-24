package cscie97.asn4.ecommerce.authentication;

/**
 * {@code Permission} extends the {@code Inventory} class and represents a
 * defined permission of the Mobile Application Store. Permissions correspond
 * to the public restricted methods offered by the store APIs.
 */
public class Permission extends Inventory{

		private final String description;
		private final String serviceId;
		
		/**
		 * Default Permission constructor
		 * 
		 * @param id			Permission id
		 * @param name			Permission name
		 * @param description	Permission description
		 * @param serviceId		Id of the Permission's associated service
		 */
		protected Permission(String id, String name, String description, 
							 String serviceId){
			super(id, name);
			this.description = description;
			this.serviceId = serviceId;
		}
		
		/**
		 * Accepts a Visitor and invokes its visit method.
		 * 
		 * @param v 	the Visitor
		 */
		@Override
		public void acceptVisitor(Visitor v){
			v.visit(this);
		}

		public String getDescription() {
			return description;
		}

		public String getServiceId() {
			return serviceId;
		}		
}
