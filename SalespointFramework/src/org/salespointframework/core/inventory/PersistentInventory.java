package org.salespointframework.core.inventory;

import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.product.features.ProductFeatureSet;

public class PersistentInventory {

	void add(ProductType productType, int count) {
			
	}
	
	void add(ProductType productType, ProductFeatureSet productFeatureSet, int count) {
		PersistentProduct pt = new PersistentProduct(productType);
	}
	
	void remove(ProductType productType) {
		
	}
	
	void remove(ProductType productType, ProductFeatureSet productFeatureSet) {
		
	}
	
	
	int getCount(ProductType productType) {
		return 0;
	}
	
	int getCount(ProductType productType, ProductFeatureSet productFeatureSet) {
		return 0;
	}
}
