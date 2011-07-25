package test.user;

import javax.persistence.Entity;

import org.salespointframework.core.users.AbstractCustomer;

@Entity
public class MyCustomer extends AbstractCustomer{
	
	
	@Deprecated
	public MyCustomer(){
	}
	
	
	public MyCustomer(String userId, String pw){
		super(userId, pw);
	}


}
