package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

public class TransientProduct implements Product {

	@Override
	public ProductIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Money getPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addProductFeature(ProductFeature productFeature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeProductFeature(
			ProductFeatureIdentifier productFeatureIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProductFeature getProductFeature(
			ProductFeatureIdentifier productFeatureIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductFeature> getProductFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addCategory(String category) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCategory(String category) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<String> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

}
