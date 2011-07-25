package test.user;

import javax.persistence.EntityManager;

import org.salespointframework.core.users.AbstractUserManager;

public class MyEmployeeManager extends AbstractUserManager<MyEmployee>{

	public MyEmployeeManager(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public Class<MyEmployee> getContentClass() {
		return MyEmployee.class;
	}

}
