package test.catalog;

import javax.persistence.EntityManager;

import org.salespointframework.core.catalog.AbstractProductCatalog;

import test.product.KeksProduct;

public class KeksCatalog extends AbstractProductCatalog<KeksProduct> {

	public KeksCatalog(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public Class<KeksProduct> getContentClass() {
		return KeksProduct.class;
	}

}
