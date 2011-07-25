package test.user;

import javax.persistence.EntityManager;

import org.salespointframework.core.users.AbstractUserManager;

public class MyCustomerManager extends AbstractUserManager<MyCustomer>{

	public MyCustomerManager(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public Class<MyCustomer> getContentClass() {
		return MyCustomer.class;
	}

}
