package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;


/**
 * The Product interface
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
	 * Adds a {@link ProductFeature}
	 * @param productFeature the {@link ProductFeature} to be added
	 * @return true if the Product did not already contain the {@link ProductFeature}, otherwise false
	 * @throws NullPointerException if productFeature is null
	 */
	//boolean addProductFeature(ProductFeature productFeature);
	
	/**
	 * Removes a {@link ProductFeature}
	 * @param productFeatureIdentifier the {@link ProductFeatureIdentifier} of the {@link ProductFeature}
	 * @return true if the ProductFeature is removed, false otherwise 
	 * @throws NullPointerException if productFeatureIdentifier is null
	 */
	//boolean removeProductFeature(ProductFeatureIdentifier productFeatureIdentifier);
	
	
	/**
	 * Gets the {@link ProductFeature} for a specifig {@link ProductFeatureIdentifier}
	 * @param productFeatureIdentifier the {@link ProductFeatureIdentifier} of the {@link ProductFeature}
	 * @return a {@link ProductFeature}
	 * @throws NullPointerException if productFeatureIdentifier is null
	 */
	//ProductFeature getProductFeature(ProductFeatureIdentifier productFeatureIdentifier);

	/**
	 * 
	 * @return an Iterable of all {@link ProductFeature}s of this Product
	 */
	//Iterable<ProductFeature> getProductFeatures();

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

	Metric getMetric();
}
