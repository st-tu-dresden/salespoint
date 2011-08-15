package org.salespointframework.core.catalog;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

// TODO
public abstract class AbstractProductCatalog<T extends AbstractProductType>
		implements ProductCatalog<T>, ICanHasClass<T> {

	private final EntityManager entityManager;

	
	public AbstractProductCatalog() {
		entityManager = null;
	}
	
	public AbstractProductCatalog(final EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager,
				"entityManager");

	}

	@Override
	public void addProductType(final T productType) {
		Objects.requireNonNull(productType, "productType");
		EntityManager em = foobar();
		em.persist(productType);
		beginCommit(em);
	}

	@Override
	public void removeProductType(final ProductIdentifier productIdentifier) {
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = foobar();
		em.remove(this
				.findProductTypeByProductIdentifier(productIdentifier));
		beginCommit(em);
	}

	@Override
	public T findProductTypeByProductIdentifier(
			final ProductIdentifier productIdentifier) {
		EntityManager em = foobar();
		return em.find(this.getContentClass(), productIdentifier);
	}

	// TODO sinnvoll?
	// klingt erstmal gut, aber mal sehen, ob man beim coden einfach so an einen
	// ProductType String herankommt
	@Override
	public T findProductTypeByName(final String name) {
		Objects.requireNonNull(name, "name");
		EntityManager em = foobar();
		return null;
	}

	@Override
	public Iterable<T> getProductTypes() {
		Class<T> cc = this.getContentClass();
		EntityManager em = foobar();
		TypedQuery<T> tquery = em.createQuery(
				"Select t from " + cc.getSimpleName() + " t", cc);
		return Iterables.from(tquery.getResultList());
	}

	// TODO
	@Override
	public Iterable<T> findProductTypesByCategory(final String category) {
		Objects.requireNonNull(category, "category");

		EntityManager em = foobar();
		
		Class<T> contentClass = this.getContentClass();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(contentClass);
		Root<T> root = cq.from(contentClass);

		TypedQuery<T> tquery = em.createQuery("",
				this.getContentClass());
		return tquery.getResultList();
	}

	@Override
	public Iterable<CatalogEntry<T>> getCatalogEntries() {
		EntityManager em = foobar();
		return null;
	}

	@Override
	public void clear() {
		EntityManager em = foobar();
	}
	
	private final void beginCommit(final EntityManager em) {
		if(entityManager == null) {
			em.getTransaction().begin();
			em.getTransaction().commit();
		}
	}
	
	// ?? Operator in C#, gibts hier leider nicht, deswegen die Methode
	private final EntityManager foobar() {
		return entityManager != null ? entityManager : Database.INSTANCE.getEntityManagerFactory().createEntityManager();
	}
}
