package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;


/**
 * The Product interface
 * 
 * @author Paul Henke
 * 
 */
public interface Product
{
	/**
	 * 
	 * @return the unique {@link ProductIdentifier} of the Product
	 */
	ProductIdentifier getIdentifier();
	
	/**
	 * 
	 * @return the name of the Product
	 */
	String getName();
	
	/**
	 * 
	 * @return the price of the Product
	 */
	Money getPrice();
	
	/**
	 * Sets the name of the Product
	 * @param name the new name 
	 * @throws NullPointerException if name is null
	 */
	void setName(String name);
	
	/**
	 * Sets the price of the Product
	 * @param price the new price
	 * * @throws NullPointerException if price is null
	 */
	void setPrice(Money price);

	/**
	 * Adds a category to this Product
	 * @param category the category to be added
	 * @return true if this Product did not already contain the category, otherwise false
	 * @throws NullPointerException if category is null
	 */
	boolean addCategory(String category);
	
	/**
	 * Removes a category from this Product
	 * @param category the name of the category to be removed
	 * @return if the Product contained the category, otherwise false
	 * @throws NullPointerException if category is null
	 */
	boolean removeCategory(String category);
	
	/**
	 * 
	 * @return an Iterable with all categories of this Product
	 */
	Iterable<String> getCategories();

	/**
	 * 
	 * @return the {@link Metric} of the Product
	 */
	Metric getMetric();
}
