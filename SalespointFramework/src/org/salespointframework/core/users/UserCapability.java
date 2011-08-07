package org.salespointframework.core.users;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A UserCapability is identified by a name and nothing else.
 * You connect your Capabilities to the User in your {@link AbstractUserManager}.
 * 
 * @author Christopher Bellmann
 *
 */
@Entity
public class UserCapability {
	
	@Id
	private String name;
	
	@Deprecated
	public UserCapability(){};
	
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
	
	public int hashCode(){
		return name.hashCode();
	}
	
	
	@Override
	public boolean equals(Object o){
		if (o instanceof UserCapability){
			return equals((UserCapability)o);
		}
		return false;
	}
	
	
	public boolean equals(UserCapability uc){
		if (this.name.equals(uc.name)) return true;
		return false;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
