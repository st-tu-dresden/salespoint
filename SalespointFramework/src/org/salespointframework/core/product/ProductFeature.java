package org.salespointframework.core.product;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * TODO
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class ProductFeature implements Serializable,Comparable<ProductFeature>
{
	private String featureType;
	private String value;
	private Money price;
	private double percent;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ProductFeature()
	{
	}

	// TODO ::::::: Check
	private ProductFeature(String featureType, String value, Money price, double percent)
	{
		this.featureType = Objects.requireNonNull(featureType, "featureType");
		this.value = Objects.requireNonNull(value, "value");
		this.price = Objects.requireNonNull(price, "price");
		this.percent = Objects.requireNonNull(percent, "percent");
	}

	
	/**
	 * @param featureType the featuretype of this ProductFeature
	 * @param value the value of this ProductFeature
	 * @return a new ProductFeature with the price value of 0 //TODO besser schreiben?
	 */
	public static ProductFeature create(String featureType, String value)
	{
		return new ProductFeature(featureType, value, Money.ZERO, 0);
	}

	/**
	 * 
	 * @param featureType the featuretype of this ProductFeature
	 * @param value the value of this ProductFeature
	 * @param price the price of this ProductFeature
	 * @return a new ProductFeature
	 */
	public static ProductFeature create(String featureType, String value, Money price)
	{
		return new ProductFeature(featureType, value, price, 0);
	}

	public static ProductFeature create(String featureType, String value, double percent)
	{
		return new ProductFeature(featureType, value, Money.ZERO, percent);
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

	//TODO
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
			ProductFeature rehto = (ProductFeature) other;
			return this.featureType.equals(rehto.featureType) && this.value.equals(rehto.value);
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
