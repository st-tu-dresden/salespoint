package org.salespointframework.core.users;

public class AbstractCustomer extends AbstractUser implements Customer{

	private long customerId;
	
	public AbstractCustomer(String username, String password) {
		super(username, password);
	}

	@Override
	public long getCustomerId() {
		return customerId;
	}

}
