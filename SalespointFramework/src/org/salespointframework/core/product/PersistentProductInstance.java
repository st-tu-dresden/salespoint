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
import org.salespointframework.util.Objects;

/**
 * TODO
 * @author Paul Henke
 *
 */
@Entity
public class PersistentProductInstance implements ProductInstance, Comparable<PersistentProductInstance>
{
	@EmbeddedId
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
	 * @param productType the {@link Product} of the PersistentProduct
	 * @param productFeatureIdentifiers an optional  of Tuples of {@link ProductFeature}s for the PersistentProduct		//TODO STIMMT NICHT MEHR wegen varargs
	 */
	public PersistentProductInstance(Product productType, ProductFeatureIdentifier... productFeatureIdentifiers)
	{
		Objects.requireNonNull(productFeatureIdentifiers, "productFeatureIdentifiers");
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getIdentifier();
		this.name = productType.getName();
		this.price = productType.getPrice(); // TODO CLONE?
		
		for(ProductFeatureIdentifier pfi : productFeatureIdentifiers) {
			ProductFeature productFeature = productType.getProductFeature(pfi);
			productFeatures.add(productFeature);
			// TODO preis mit einberechnen :D
			// klärungsbedarf bei %%%%%
		}
		
	}
	
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
	
	//TODO comment ins interface?
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
