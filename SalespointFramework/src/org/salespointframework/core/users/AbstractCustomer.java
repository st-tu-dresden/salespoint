package org.salespointframework.core.users;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractCustomer extends AbstractUser implements Customer{
	
	@Deprecated
	protected AbstractCustomer() {
		super();
	}
	
	/**
	 * creates a new Customer
	 * @param userIdentifier (username) of the Customer 
	 * @param first password of the Customer
	 */
	public AbstractCustomer(UserIdentifier userIdentifier, String password) {
		super(userIdentifier, password);
	}



}
