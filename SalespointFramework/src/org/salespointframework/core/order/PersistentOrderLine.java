package org.salespointframework.core.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

@Entity
public class PersistentOrderLine implements OrderLine {
	
	@EmbeddedId
	private OrderLineIdentifier orderLineIdentifier;

	@Embedded
	@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
	private ProductIdentifier productIdentifier;
	
	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();
	
	private int numberOrdered;
	
	private Money price;

	private String productName;
	
    /**
     * Parameterless constructor required for JPA. Do not use.
     */
	@Deprecated
	protected PersistentOrderLine() {
		
	}
	
	public PersistentOrderLine(ProductIdentifier productIdentifier) {
		this(productIdentifier, Iterables.<ProductFeature>empty(), 1);
	}
	
	public PersistentOrderLine(ProductIdentifier productIdentifier, int numberOrdered) {
		this(productIdentifier, Iterables.<ProductFeature>empty(), numberOrdered);
	}
	
	public PersistentOrderLine(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures) {
		this(productIdentifier, productFeatures, 1);
	}
	
	public PersistentOrderLine(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures, int numberOrdered) {
		
		Objects.requireNonNull(productFeatures, "productFeatures");
		ProductType productType = Database.INSTANCE.getEntityManagerFactory().createEntityManager().find(PersistentProductType.class, productIdentifier);
		if(productType == null) throw new RuntimeException("ProductType is unknown");
		this.productIdentifier = productType.getProductIdentifier();
		this.productName = productType.getName();
		this.productFeatures = Iterables.toSet(productFeatures);	// has to be evaluated, lazyness not always good
		if(numberOrdered <= 0) throw new IllegalArgumentException("numberOrdered must be greater than 0");
		this.numberOrdered = numberOrdered;
		orderLineIdentifier = new OrderLineIdentifier();
		
		Money price = productType.getPrice();
		for(ProductFeature pf : this.productFeatures) {
			price = price.add_(pf.getPrice());
		}
		this.price = price;
	}

	@Override
	public final OrderLineIdentifier getOrderLineIdentifier() {
		return orderLineIdentifier;
	}

	@Override
	public final Money getPrice() {
		return price;
	}

	@Override
	public final ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}
	
	public final String getProductName() {
		return productName;
	}

	@Override
	public final int getNumberOrdered() {
		return numberOrdered;
	}

	@Override
	public final Iterable<ProductFeature> getProductFeatures() {
		return Iterables.from(productFeatures);
	}
}
