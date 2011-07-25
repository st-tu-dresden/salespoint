package test.inventory;

import javax.persistence.EntityManager;

import org.salespointframework.core.inventory.AbstractInventory;

import test.product.KeksInstance;

public class KeksInventar extends AbstractInventory<KeksInstance> {

	public KeksInventar(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public Class<KeksInstance> getContentClass() {
		return KeksInstance.class;
	}

}
