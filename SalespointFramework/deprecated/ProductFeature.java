package org.salespointframework.core.product;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.salespointframework.core.money.Money;
import java.util.Objects;

// TODO comment
/**
 * 
 * This class is immutable.
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class ProductFeature implements Serializable, Comparable<ProductFeature>
{
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCTFEATURE_ID"))
	private final ProductFeatureIdentifier productFeatureIdentifier = new ProductFeatureIdentifier();
	
	private final String featureType;
	private final String value;
	private final Money price;
	private final double percent;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ProductFeature()
	{
		featureType = null;
		value = null;
		price = null;
		percent = 0;
	}
	
	private ProductFeature(String featureType, String value, Money price, double percent)
	{
		this.featureType = Objects.requireNonNull(featureType, "featureType must not be null");
		this.value = Objects.requireNonNull(value, "value must not be null");
		this.price = Objects.requireNonNull(price, "price must not be null");
		this.percent = percent;
	}

	
	/**
	 * Creates a new ProductFeature
	 * @param featureType the featuretype of this ProductFeature
	 * @param value the value of this ProductFeature
	 * @return a new ProductFeature with the price and money value of 0 //TODO besser schreiben?
	 * @throws NullPointerException if featureType or value are null
	 */
	public static ProductFeature create(String featureType, String value)
	{
		return new ProductFeature(featureType, value, Money.ZERO, 0);
	}

	/**
	 * Creates a new ProductFeature
	 * @param featureType the featuretype of this ProductFeature
	 * @param value the value of this ProductFeature
	 * @param price the price of this ProductFeature
	 * @return a new ProductFeature
	 * @throws NullPointerException if featureType, value or price are null
	 */
	public static ProductFeature create(String featureType, String value, Money price)
	{
		return new ProductFeature(featureType, value, price, 0);
	}

	/**
	 * Creates a new ProductFeature
	 * @param featureType the featuretype of this ProductFeature
	 * @param value the value of this ProductFeature
	 * @param percent a value between 0 and 1
	 * @return a new ProductFeature
	 * @throws NullPointerException if featureType or value are null
	 */
	public static ProductFeature create(String featureType, String value, double percent)
	{
		return new ProductFeature(featureType, value, Money.ZERO, percent);
	}

	/**
	 * 
	 * @return the {@link ProductFeatureIdentifier} of this ProductFeature
	 */
	public final ProductFeatureIdentifier getIdentifier() {
		return productFeatureIdentifier;
	}
	
	/**
	 * 
	 * @return the FeatureType of this ProductFeature
	 */
	public final String getFeatureType()
	{
		return featureType;
	}

	// TODO naming, value heißt woanders Money statt String
	/**
	 * 
	 * @return the Value of this ProductFeature
	 */
	public final String getValue()
	{
		return value;
	}

	/**
	 * 
	 * @return the Price of the ProductFeature
	 */
	public final Money getPrice()
	{
		return price;
	}

	/**
	 * 
	 * @return the percentage of the price
	 */
	public final double getPercent()
	{
		return percent;
	}

	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof ProductFeature)
		{
			return this.productFeatureIdentifier.equals(((ProductFeature) other).productFeatureIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return Objects.hash(featureType, value);
	}

	@Override
	public final String toString()
	{
		return featureType + " | " + value;
	}

	@Override
	public int compareTo(ProductFeature other)
	{
		return this.featureType.compareTo(other.featureType);
	}
}
