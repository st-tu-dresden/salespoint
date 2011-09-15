package test.user;

import javax.persistence.Entity;

import org.salespointframework.core.users.PersistentUser;
import org.salespointframework.core.users.UserIdentifier;

@Entity
public class MyCustomer extends PersistentUser {
	
	
	@Deprecated
	public MyCustomer(){
	}
	
	
	public MyCustomer(UserIdentifier userId, String pw){
		super(userId, pw);
	}


}
