package org.salespointframework.core.users;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserCapabilityList extends ArrayList<UserCapability>{

	
	public UserCapabilityList(){
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private long id;
		
		

}
