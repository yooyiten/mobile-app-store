package cscie97.asn4.ecommerce.authentication;

import java.util.*;

/**
 * {@code AccessToken} represents a token which can be used by a User to verify
 * her identity and permissions to execute a restricted method. An AccessToken
 * is generated for a User upon successful login, and expires due to logging
 * out or inactivity.
 */
public class AccessToken {

	private final String guid;
	private long lastUsed;
	private AccessTokenState state;
	private final String userName;
	private final long timeoutSpan;
	
	/**
	 * Default AccessToken constructor.
	 * 
	 * @param user		user to create AccessToken for
	 * @param timeout	timeout period for the AccessToken
	 */
	protected AccessToken(String user, long timeout){
		guid = UUID.randomUUID().toString();
		lastUsed = System.currentTimeMillis();
		state = AccessTokenState.active;
		userName = user;
		timeoutSpan = timeout;
	}
	
	/**
	 * Returns whether the AccessToken is valid or not. Updates the
	 * state and lastUsed properties of the AccessToken accordingly.
	 * 
	 * @return true if AccessToken is valid; false if not
	 */
	public boolean isValid(){
		if(this.state == AccessTokenState.expired){
			return false;
		}
		else if(this.state.equals(AccessTokenState.active) && 
		   (this.lastUsed + this.timeoutSpan) < System.currentTimeMillis()){
			this.state = AccessTokenState.expired;
			return false;
		}
		else{
			this.lastUsed = System.currentTimeMillis();
			return true;
		}
	}
	
	/**
	 * Deactivates an AccessToken.
	 */
	public void deactivate(){
		if(this.lastUsed + this.timeoutSpan > System.currentTimeMillis()){
			this.lastUsed = System.currentTimeMillis();
		}
		this.state = AccessTokenState.expired;
	}

	public String getGuid() {
		return guid;
	}

	public String getUserName() {
		return userName;
	}
}
