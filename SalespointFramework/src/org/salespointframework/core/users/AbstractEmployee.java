package org.salespointframework.core.users;

import javax.persistence.MappedSuperclass;

import org.salespointframework.core.money.Money;

/**
 * This is an implementation of the Employee interface.
 * @author Christopher Bellmann
 *
 */
@MappedSuperclass
public abstract class AbstractEmployee extends AbstractUser implements Employee {
	private Money salary;
	
	
	@Deprecated
	public AbstractEmployee(){
		super();
	}
	
	
	/**
	 * creates a new Employee
	 * @param username of the Employee
	 * @param first password of the Employee
	 */
	public AbstractEmployee(String username, String password){
		super(username, password);
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
