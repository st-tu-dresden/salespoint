package org.salespointframework.core.users;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class AbstractCustomer extends AbstractUser implements Customer{

	@Id
	@GeneratedValue
	private long customerId;
	
	@Deprecated
	protected AbstractCustomer() {
		super();
	}

	
	public AbstractCustomer(String username, String password) {
		super(username, password);
	}

	@Override
	public long getCustomerId() {
		return customerId;
	}

}
