package org.salespointframework.user;

import javax.persistence.Entity;

import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
@Entity
public class Customer extends User {
	
	
	@Deprecated
	public Customer(){
	}
	
	
	public Customer(UserIdentifier userId, String pw){
		super(userId, pw);
	}


}
