package org.salespointframework.core.inventory;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.product.SerialNumber;

/**
 * 
 * @author Paul
 *
 * @param <TProduct>
 * @param <TProductType>
 */
public interface __Inventory__<TProduct extends Product, TProductType extends ProductType> {

	void add(TProduct product);
	
	void remove(SerialNumber serialNumber);
	
	boolean contains(SerialNumber serialNumber);
	
	<E extends TProduct> E get(Class<E> clazz, SerialNumber serialNumber);

	<E extends TProduct> Iterable<E> findProducts(Class<E> clazz);

	<E extends TProduct> Iterable<E> findProductByProductType(Class<E> clazz, TProductType productType);
	
	<E extends TProduct> Iterable<E> findProductByProductTypeAndFeatures(Class<E> clazz, TProductType productType, Iterable<ProductFeature> productFeatures);
}
