package test.spielwiese;

import javax.persistence.EntityManager;

import org.salespointframework.core.catalog.AbstractProductCatalog;

public class KeksCatalog extends AbstractProductCatalog<Keks> {

	public KeksCatalog(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public Class<Keks> getClassPLZ() {
		return Keks.class;
	}

}
