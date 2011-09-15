package test.user;

import javax.persistence.Entity;


import org.salespointframework.core.users.PersistentUser;
import org.salespointframework.core.users.UserIdentifier;

@Entity
public class MyEmployee extends PersistentUser {
	
	@Deprecated
	public MyEmployee(){
		
	}
	
	public MyEmployee(UserIdentifier userId, String pw){
		super(userId, pw);
	}

}
