package org.salespointframework.catalog;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * A product.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
public class Product extends AbstractEntity<ProductIdentifier>implements Comparable<Product> {

	private static final long serialVersionUID = 6645371648836029780L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID") ) //
	private ProductIdentifier productIdentifier = new ProductIdentifier();
	private String name;
	private @Lob Money price;
	private @ElementCollection Set<String> categories = new HashSet<String>();
	private Metric metric;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected Product() {}

	public Product(String name, Money price) {
		this(name, price, Metric.UNIT);
	}

	/**
	 * Creates a new {@link Product} with the given name, price and {@link Metric}.
	 * 
	 * @param name the name of the {@link Product}, must not be {@literal null} or empty.
	 * @param price the price of the {@link Product}, must not be {@literal null}.
	 * @param metric the {@link Metric} of the {@link Product}, must not be {@literal null}.
	 */
	public Product(String name, Money price, Metric metric) {

		Assert.hasText(name, "Name must not be null or empty!");
		Assert.notNull(price, "Price must not be null!");
		Assert.notNull(metric, "Metric must not be null!");

		this.name = name;
		this.price = price;
		this.metric = metric;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.AbstractEntity#getIdentifier()
	 */
	public final ProductIdentifier getIdentifier() {
		return productIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {

		Assert.hasText(name, "Name must not be null or empty!");
		this.name = name;

	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {

		Assert.notNull(price, "Price must not be null!");
		this.price = price;
	}

	public final Iterable<String> getCategories() {
		return Collections.unmodifiableSet(categories);
	}

	public final boolean addCategory(String category) {
		Assert.notNull(category, "category must not be null");
		return categories.add(category);
	}

	public final boolean removeCategory(String category) {
		Assert.notNull(category, "category must not be null");
		return categories.remove(category);
	}

	/**
	 * Returns whether the {@link Product} supports the given {@link Quantity}.
	 * 
	 * @param quantity
	 * @return
	 */
	public boolean supports(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		return quantity.isCompatibleWith(metric);
	}

	/**
	 * Verifies the given {@link Quantity} to match the one supported by the current {@link Product}.
	 * 
	 * @param quantity
	 * @throws MetricMismatchException in case the {@link Product} does not support the given {@link Quantity}.
	 */
	public void verify(Quantity quantity) {

		if (!supports(quantity)) {
			throw new MetricMismatchException(String.format("Product %s does not support quantity %s!", this, quantity));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Product other) {
		return this.name.compareTo(other.name);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
