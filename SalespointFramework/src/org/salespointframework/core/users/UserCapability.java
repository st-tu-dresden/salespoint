package org.salespointframework.core.users;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A UserCapability is identified by a name and nothing else.
 * You connect your Capabilities to the User in your {@link UserManager}.
 * @see UserManager
 * @author Christopher Bellmann
 *
 */
@Entity
public class UserCapability {
	
	@Id
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
