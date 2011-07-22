package org.salespointframework.core.users;

import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractCustomer extends AbstractUser implements Customer{
	
	@GeneratedValue
	private long customerId;
	
	@Deprecated
	protected AbstractCustomer() {
		super();
	}
	
	/**
	 * creates a new Customer
	 * @param userId (username) of the Customer 
	 * @param first password of the Customer
	 */
	public AbstractCustomer(String userId, String password) {
		super(userId, password);
	}

	@Override
	public long getCustomerId() {
		return customerId;
	}

}
