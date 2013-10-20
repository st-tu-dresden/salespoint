package org.salespointframework.user;

import javax.persistence.Entity;

import org.salespointframework.core.user.User;
import org.salespointframework.core.useraccount.UserAccountIdentifier;

// FIXME
@SuppressWarnings("javadoc")
@Entity
public class Customer extends User {
	
	
	@Deprecated
	public Customer(){
	}
	
	
	public Customer(UserAccountIdentifier userId, String pw){
		super(userId, pw);
	}


}
