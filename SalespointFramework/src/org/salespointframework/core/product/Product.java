package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

// TODO comment
/**
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
	 * Adds a {@link ProductFeature}
	 * @param productFeature the {@link ProductFeature} to be added
	 * @return true if the ProductType did not already contain the {@link ProductFeature}, otherwise false
	 * @throws ArgumentNullException if productFeature is null
	 */
	boolean addProductFeature(ProductFeature productFeature);
	
	// TODO comment
	boolean removeProductFeature(ProductFeatureIdentifier productFeatureIdentifier);
	
	
	// TODO comment
	ProductFeature getProductFeature(ProductFeatureIdentifier productFeatureIdentifier);

	/**
	 * 
	 * @return an Iterable of all {@link ProductFeature}s of this Product
	 */
	Iterable<ProductFeature> getProductFeatures();

	/**
	 * Adds a category to this Product
	 * @param category the category to be added
	 * @return true if this ProductType did not already contain the category, otherwise false
	 * @throws ArgumentNullException if category is null
	 */
	boolean addCategory(String category);
	
	/**
	 * Removes a category from this Product
	 * @param category the name of the category to be removed
	 * @return if the ProductType contained the category, otherwise false
	 * @throws ArgumentNullException if category is null
	 */
	boolean removeCategory(String category);
	
	/**
	 * 
	 * @return an Iterable with all categories of this Product
	 */
	Iterable<String> getCategories();
}
