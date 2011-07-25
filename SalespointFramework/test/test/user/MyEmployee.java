package test.user;

import javax.persistence.Entity;

import org.salespointframework.core.users.AbstractEmployee;

@Entity
public class MyEmployee extends AbstractEmployee{
	
	@Deprecated
	public MyEmployee(){
		
	}
	
	public MyEmployee(String userId, String pw){
		super(userId, pw);
	}

}
