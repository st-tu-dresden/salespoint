package org.salespointframework.core.users;

import org.salespointframework.core.money.Money;

/**
 * This is an implementation of the Employee interface.
 * @author Christopher Bellmann
 *
 */
public abstract class AbstractEmployee extends AbstractUser implements Employee {
	
	private Money salary;
	
	/**
	 * creates a new Employee
	 * @param username of the Employee
	 * @param first password of the Employee
	 */
	public AbstractEmployee(String username, String password){
		super(username, password);
	}
	
	
	public Money getSalary(){
		return salary;
	}
	
	
	public void changeSalery(Money salery){
		this.salary=salery;
	}

	
}
