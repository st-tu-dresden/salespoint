package org.salespointframework.core.products;

public abstract class ProductInstance<T extends ProductType> {
	private T productType;
	
	public T getProductType() { 
		return productType;
	}
	
	
	public ProductInstance(T productType) {
		this.productType = productType;
	}
	
	 
}
