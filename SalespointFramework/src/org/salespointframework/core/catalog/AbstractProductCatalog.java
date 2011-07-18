package org.salespointframework.core.catalog;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

public abstract class AbstractProductCatalog<T extends AbstractProductType> implements ProductCatalog<T>, ICanHasClass<T> {

	private EntityManager entityManager;
	
	public AbstractProductCatalog(EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
		
	}
	
	@Override
	public void addProductType(T productType) {
		Objects.requireNonNull(productType, "productType");
		entityManager.persist(productType);
	}
	
	@Override
	public void removeProductType(String productIdentifier) {
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		entityManager.remove(this.findProductTypeByProductIdentifier(productIdentifier));
	}

	@Override
	public T findProductTypeByProductIdentifier(String productIdentifier) {
		return entityManager.find(this.getContentClass(), productIdentifier);
	}

	// TODO sinnvoll?
	@Override
	public T findProductTypeByName(String name) {
		Objects.requireNonNull(name, "name");
		return null;
	}

	
	@Override
	public Iterable<T> getProductTypes() {
		Class<T> cc = this.getContentClass();
		TypedQuery<T> tquery = entityManager.createQuery("Select t from " + cc.getCanonicalName() + " t",cc);
		return SalespointIterable.from(tquery.getResultList());
	}
	
	//TODO
	@Override
	public Iterable<T> findProductTypesByCategory(String category) {
		Objects.requireNonNull(category, "category");
		
		Class<T> contentClass = this.getContentClass();
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(contentClass); 
		Root<T> root = cq.from(contentClass);

		
		
		TypedQuery<T> tquery = entityManager.createQuery("",this.getContentClass());
		return tquery.getResultList();
	}

	@Override
	public Iterable<CatalogEntry<T>> getCatalogEntries() {
		return null;
	}

	@Override
	public void clear() {
	
	}
}
