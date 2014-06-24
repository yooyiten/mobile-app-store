package cscie97.asn4.test;

import cscie97.asn4.ecommerce.collection.*;
import cscie97.asn4.ecommerce.product.*;
import cscie97.asn4.ecommerce.authentication.*;


/**
 * {@code TestDriver} tests the Mobile Application Store APIs
 */
public class TestDriver {

	/**
	 * Accepts command line parameters for files containing Authentication, 
	 * Country, Device, Content, and Collection data.
	 * 
	 * Calls the methods to create the AuthenticationServiceAPI Singleton; to
	 * log in as the admin super user; to import the Authentication data.
	 * 
	 * Calls the methods to create the ProductCatalog Singleton and the 
	 * CollectionServiceAPI Singleton. Uses the users created by the 
	 * AuthenticationServiceAPI to test the integration of the Authentication
	 * Service with the Product and Collection Services.
	 */
	public static void main(String args[]){
		if(args.length != 5){
			System.err.println("You must provide the Authentication, Country, " +
							   "Device, Content, and Collection CSV files in " +
							   "order to test the functionality of the " +
							   "Mobile Application API Store.");
			System.exit(1);
		}	
		
		String fileAuthentication = args[0];
		String fileCountry = args[1];
		String fileDevice = args[2];
		String fileContent = args[3];
		String fileCollection = args[4];		
		
		// instantiate the ProductCatalog
		ProductCatalog pc = ProductCatalog.getInstance();
		// instantiate the CollectionServiceAPI
		CollectionServiceAPI csa = CollectionServiceAPI.getInstance();
		// instantiate the AuthenticationServiceAPI
		AuthenticationServiceAPI asa = AuthenticationServiceAPI.getInstance();
		try {
			// login as the super user
			System.out.println("Logging in...");
			String token1 = asa.login("super", "999");
			// import the Authentication data
			System.out.println("Importing Authentication Data...");
			asa.importCSV(token1, fileAuthentication);
			// show the Authentication Service inventory
			asa.showInventory();
			// logout the super user
			System.out.println("Logging out...");
			asa.logout(token1);		
			
			// login as a product admin
			System.out.println("Logging in...");
			String token2 = asa.login("sam", "secret");
			// import the Country data
			System.out.println("Importing Country data into ProductCatalog...");
			pc.importCSV(fileCountry, "country", token2);
			// import the Devices data
			System.out.println("Importing Device data into ProductCatalog...");
			pc.importCSV(fileDevice, "device", token2);
			System.out.println("Logging out...");
			asa.logout(token2);
			
			// login as a product developer
			System.out.println("Logging in...");
			String token3 = asa.login("joe", "1234");
			// import the Content data
			System.out.println("Importing Content data into ProductCatalog...");
			pc.importCSV(fileContent, "content", token3);
			System.out.println("Logging out...");
			asa.logout(token3);
			
			// login as a collection admin
			System.out.println("Logging in...");
			String token4 = asa.login("lucy", "4567");
			// import the Collection data
			System.out.println("Importing Collection data...");
			csa.importCSV(token4, fileCollection);
			System.out.println("Logging out...");
			asa.logout(token4);
			
		} catch (AuthenticationException e) {
			System.out.println("Error logging in with user " + e.getUserName() + ":");
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (InvalidAccessTokenException e) {
			if(e.getUser() != null){
				System.out.println("Error for user " + e.getUser() + ":");
			}
			else{
				System.out.println("Error for unknown user:");
			}
			System.out.println(e.getMessage());
		} catch (AuthenticationImportException e) {
			System.out.println("Error importing file " + e.getFileName() + 
							   " on line " + e.getLine());
			System.out.println(e.getMessage());
		} catch (PermissionException e) {
			System.out.println("Error for user " + e.getUser() + " on action " +
							   e.getPermission());
			System.out.println(e.getMessage());
		} catch (AuthenticationDataException e) {
			System.out.println("Error processing data for line: " + e.getLine());
			System.out.println(e.getMessage());
		} catch (ImportException e) {
			System.out.println("Error importing file " + e.getFailedFile() + 
							   " on line " + e.getFailedLine());
		    System.out.println(e.getMessage());
		} catch (CollectionImportException e) {
			System.out.println("Error importing file " + e.getFileName() +
							   " on line " + e.getLine());
			System.out.println(e.getMessage());
		}
	}	
}