package test.user;

import javax.persistence.Entity;

import org.salespointframework.core.users.AbstractEmployee;
import org.salespointframework.core.users.UserIdentifier;

@Entity
public class MyEmployee extends AbstractEmployee{
	
	@Deprecated
	public MyEmployee(){
		
	}
	
	public MyEmployee(UserIdentifier userId, String pw){
		super(userId, pw);
	}

}
