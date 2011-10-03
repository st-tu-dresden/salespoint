package test.user;

import javax.persistence.Entity;


import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
@Entity
public class Employee extends PersistentUser {
	
	@Deprecated
	public Employee(){
		
	}
	
	public Employee(UserIdentifier userId, String pw){
		super(userId, pw);
	}

}
