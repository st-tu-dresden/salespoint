package org.salespointframework.core.order.paul;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderLineIdentifier;
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

	/* too bad, doesnt work, class can only be @Entity xor @Embedabble 
	@Embedded
	private PersistentProductType productType;
	*/
	private ProductIdentifier productIdentifier;
	
	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();
	
	private int numberOrdered;
	
	@Deprecated
	protected PersistentOrderLine() {
		
	}
	
	public PersistentOrderLine(PersistentProductType productType) {
		this(productType, Iterables.<ProductFeature>empty(), 1);
	}
	
	public PersistentOrderLine(PersistentProductType productType, int numberOrdered) {
		this(productType, Iterables.<ProductFeature>empty(), numberOrdered);
	}
	
	public PersistentOrderLine(PersistentProductType productType, Iterable<ProductFeature> productFeatures) {
		this(productType, productFeatures, 1);
	}
	
	public PersistentOrderLine(PersistentProductType productType, Iterable<ProductFeature> productFeatures, int numberOrdered) {
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getProductIdentifier();
		Objects.requireNonNull(productFeatures, "productFeatures");
		this.productFeatures = Iterables.toSet(productFeatures);	// has to be evaluated, lazyness not always good
		if(numberOrdered <= 0) throw new IllegalArgumentException("numberOrdered must be greater than 0");
		this.numberOrdered = numberOrdered;
		orderLineIdentifier = new OrderLineIdentifier();
	}

	@Override
	public OrderLineIdentifier getOrderLineIdentifier() {
		return orderLineIdentifier;
	}

	@Override
	public Money getPrice() {
		Money price = getProductType().getPrice();
		for(ProductFeature pf : productFeatures) {
			price = price.add_(pf.getPrice());
		}
		return price;
	}

	// HACK :(
	@Override
	public ProductType getProductType() {
		return Database.INSTANCE.getEntityManagerFactory().createEntityManager().find(PersistentProductType.class, productIdentifier);
	}

	@Override
	public int getNumberOrdered() {
		return numberOrdered;
	}

	@Override
	public Iterable<ProductFeature> getProductFeatures() {
		return Iterables.from(productFeatures);
	}
}
