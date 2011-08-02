package test.user;

import javax.persistence.Entity;

import org.salespointframework.core.users.AbstractCustomer;
import org.salespointframework.core.users.UserIdentifier;

@Entity
public class MyCustomer extends AbstractCustomer{
	
	
	@Deprecated
	public MyCustomer(){
	}
	
	
	public MyCustomer(UserIdentifier userId, String pw){
		super(userId, pw);
	}


}
