package test.user;

import javax.persistence.Entity;

import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
@Entity
public class Customer extends PersistentUser {
	
	
	@Deprecated
	public Customer(){
	}
	
	
	public Customer(UserIdentifier userId, String pw){
		super(userId, pw);
	}


}
