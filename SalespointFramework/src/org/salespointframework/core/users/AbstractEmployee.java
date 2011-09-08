package org.salespointframework.core.users;

import javax.persistence.MappedSuperclass;

import org.salespointframework.core.money.Money;

/**
 * This is an implementation of the Employee interface.
 * @author Christopher Bellmann
 *
 */
@MappedSuperclass
public abstract class AbstractEmployee extends PersistentUser implements Employee {
	private Money salary;
	
	
	@Deprecated
	public AbstractEmployee(){
		super();
	}
	
	
	/**
	 * creates a new Employee
	 * @param userId (username) of the Employee 
	 * @param first password of the Employee
	 */
	public AbstractEmployee(UserIdentifier userIdentifier, String password){
		super(userIdentifier, password);
	}
	
	@Override
	public Money getSalary(){
		return salary;
	}
	
	@Override
	public void changeSalary(Money salary){
		this.salary=salary;
	}

	
}
