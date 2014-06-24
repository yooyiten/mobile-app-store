package cscie97.asn4.ecommerce.authentication;

/**
 * AccessToken State Enumeration
 * Defines possible AccessToken states: active, expired
 */
public enum AccessTokenState {
	active("active"), expired("expired");
	
	private String accessTokenState;
	
	/**
	 * Default private constructor.
	 * 
	 * @param tokenState AccessToken state
	 */
	private AccessTokenState(String tokenState){
		accessTokenState = tokenState;
	}

	public String getAccessTokenState() {
		return accessTokenState;
	}
}
