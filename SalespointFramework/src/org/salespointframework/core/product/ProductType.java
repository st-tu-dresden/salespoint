package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

/**
 * TODO
 * @author Paul Henke
 * 
 */
public interface ProductType
{
	/**
	 * 
	 * @return the unique {@link ProductTypeIdentifier} of the ProductType
	 */
	ProductTypeIdentifier getIdentifier();
	
	/**
	 * 
	 * @return the name of the ProductType
	 */
	String getName();
	
	/**
	 * 
	 * @return the price of the ProductType
	 */
	Money getPrice();

	/**
	 * Adds a {@link ProductFeature}
	 * @param productFeature the {@link ProductFeature} to be added
	 * @return true if the ProductType did not already contain the {@link ProductFeature}, otherwise false
	 */
	boolean addProductFeature(ProductFeature productFeature);
	
	// TODO comment
	boolean removeProductFeature(ProductFeatureIdentifier productFeatureIdentifier);
	
	
	// TODO comment
	ProductFeature getProductFeature(ProductFeatureIdentifier productFeatureIdentifier);

	/**
	 * 
	 * @return an Iterable of all {@link ProductFeature}s of this ProductType
	 */
	Iterable<ProductFeature> getProductFeatures();

	/**
	 * Adds a category to this ProductType
	 * @param category the category to be added
	 * @return true if this ProductType did not already contain the category, otherwise false
	 */
	boolean addCategory(String category);
	
	/**
	 * Removes a category from this ProductType
	 * @param category the name of the category to be removed
	 * @return if the ProductType contained the category, otherwise false
	 */
	boolean removeCategory(String category);
	
	/**
	 * 
	 * @return an Iterable with all categories of this ProductType
	 */
	Iterable<String> getCategories();
}
