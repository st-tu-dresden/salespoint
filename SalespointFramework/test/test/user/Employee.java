package test.user;

import javax.persistence.Entity;


import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
@Entity
public class Employee extends PersistentUser {
	
	private String name;
	private String lastname;
	
	@Deprecated
	public Employee(){
		
	}
	
	public Employee(UserIdentifier userId, String pw){
		super(userId, pw);
	}
	
	public Employee(UserIdentifier userId, String pw, String name, String lastname){
		super(userId, pw);
		this.name=name;
		this.lastname=lastname;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getLastname(){
		return lastname;
	}
	
	public void setLastname(String lastname){
		this.lastname=lastname;
	}

}
