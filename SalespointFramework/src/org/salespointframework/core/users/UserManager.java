package org.salespointframework.core.users;

import java.util.List;
import java.util.Map;

public class UserManager<T extends User> {
	
	private Map<User, List<UserCapability>> userCapabilities;
	private List<UserCapability> capabilities;
	
	public UserManager(){
		
	}
	
	public boolean addUser(User user){
		if(userCapabilities.containsKey(user.getUsername())){
			return false;
		}
		else{
			
		}
		return false;
	}
	
	
}
