package org.salespointframework.core.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.money.Money;

/**
 * This is an implementation of the Employee interface.
 * @author Christopher Bellmann
 *
 */
@Entity
public abstract class AbstractEmployee extends AbstractUser implements Employee {
	
	@Id
	@GeneratedValue
	@SuppressWarnings("unused")
	private int id;
	
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
	
	
	public Money getSalary(){
		return salary;
	}
	
	
	public void changeSalery(Money salery){
		this.salary=salery;
	}

	
}
