package org.salespointframework.core.users;

import org.salespointframework.core.money.Money;

/**
 * An Employee is a special user. The employee has a salary.
 * This salary can be used in the Accountancy.
 * @author Christopher Bellmann
 *
 */
public interface Employee extends User{
	
	/**
	 * Set the salary of the employee to the given parameter
	 * @param salary new salary
	 */
	public void changeSalary(Money salary);
	
	public Money getSalary();

}
