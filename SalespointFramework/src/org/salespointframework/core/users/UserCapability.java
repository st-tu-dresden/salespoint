package org.salespointframework.core.users;

/**
 * A UserCapability is identified by a name and nothing else.
 * You connect your Capabilities to the User in your UserManager.
 * See UserManager for details.
 * @author Christopher Bellmann
 *
 */
public class UserCapability {
	
	private String name;
	
	/**
	 * Creates a new UserCapability
	 * @param name name you want the give the Capability
	 */
	public UserCapability(String name){
		this.name=name;
	}
	
	/**
	 * @return the name of the Capability
	 */
	public String getName(){
		return name;
	}
}
