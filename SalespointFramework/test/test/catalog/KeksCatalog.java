package test.catalog;

import javax.persistence.EntityManager;

import org.salespointframework.core.catalog.AbstractProductCatalog;

import test.product.Keks;

public class KeksCatalog extends AbstractProductCatalog<Keks> {

	public KeksCatalog(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public Class<Keks> getClassPLZ() {
		return Keks.class;
	}

}
