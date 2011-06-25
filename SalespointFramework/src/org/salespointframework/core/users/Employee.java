package org.salespointframework.core.users;

import org.salespointframework.core.money.Money;

/**
 * An Employee is a spezial User. The Employee should have a salary.
 * This salary can be used in the Accountancy.
 * @author Christopher Bellmann
 *
 */
public interface Employee extends User{
	
	/**
	 * Set the salary of the Employee to the given parameter
	 * @param salery new salary
	 */
	public void changeSalary(Money salary);
	
	public Money getSalary();

}
