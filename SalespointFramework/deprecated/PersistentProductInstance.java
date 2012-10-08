package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;
import java.util.Objects;


/**
 * A persistent implementation of the {@link ProductInstance} interface.
 * This class is immutable.
 * @author Paul Henke
 *
 */
@Entity
public class PersistentProductInstance implements ProductInstance, Comparable<PersistentProductInstance>
{
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "SERIALNUMBER_ID"))
	private SerialNumber serialNumber = new SerialNumber();

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
	private ProductIdentifier productIdentifier;

	private String name;
	private Money price;

	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentProductInstance()
	{
	}
	
	
	/**
	 * Creates a new PersistentProduct with a specified {@link ProductFeature}set
	 * @param product the {@link Product} of the PersistentProduct
	 * @param productFeatureIdentifiers a sequence of {@link ProductFeatureIdentifier}s
	 */
	/*
	
	public PersistentProductInstance(Product product, ProductFeatureIdentifier... productFeatureIdentifiers)
	{
		Objects.requireNonNull(productFeatureIdentifiers, "productFeatureIdentifiers must not be null");
		this.productIdentifier = Objects.requireNonNull(product, "product must not be null").getIdentifier();
		this.name = product.getName();
		this.price = product.getPrice(); 
		
		Money showMeTheMoney = Money.ZERO;
		
		for(ProductFeatureIdentifier pfi : productFeatureIdentifiers) {
			ProductFeature productFeature = product.getProductFeature(pfi);
			if(productFeature == null) {
				throw new IllegalArgumentException("No ProductFeature found for Id: "+ pfi);
			}
			productFeatures.add(productFeature);
			
			showMeTheMoney = showMeTheMoney.add(productFeature.getPrice());
			
			// TODO Hannes, das ist unschön mit dem new Money(percent)
			this.price = this.price.multiply(new Money(productFeature.getPercent()));
		}
		this.price = this.price.add(showMeTheMoney);
	}
	*/
	
	@Override
	public final ProductIdentifier getProductIdentifier()
	{
		return productIdentifier;
	}

	@Override
	public Money getPrice()
	{
		return price;
	}

	@Override
	public final SerialNumber getSerialNumber()
	{
		return serialNumber;
	}

	@Override
	public final Iterable<ProductFeature> getProductFeatures()
	{
		return Iterables.of(productFeatures);
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof PersistentProductInstance)
		{
			return this.serialNumber.equals(((PersistentProductInstance)other).serialNumber);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return serialNumber.hashCode();
	}

	@Override
	public String toString()
	{
		return name;
	}
	
	//TODO ins interface?
	public String getName() 
	{
		return name;
	}

	@Override
	public int compareTo(PersistentProductInstance other)
	{
		return this.name.compareTo(other.name);
	}
}
