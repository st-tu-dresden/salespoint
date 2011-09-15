package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
@Embeddable
public final class ProductFeature
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

	public static ProductFeature create(String featureType, String value)
	{
		return new ProductFeature(featureType, value, Money.ZERO, 0);
	}

	public static ProductFeature create(String featureType, String value, Money price)
	{
		return new ProductFeature(featureType, value, price, 0);
	}

	public static ProductFeature create(String featureType, String value, double percent)
	{
		return new ProductFeature(featureType, value, Money.ZERO, percent);
	}

	public final String getFeatureType()
	{
		return featureType;
	}

	public final String getValue()
	{
		return value;
	}

	public final Money getPrice()
	{
		return price;
	}

	public final double getPercent()
	{
		return percent;
	}

	@Override
	public final boolean equals(final Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (!(other instanceof ProductFeature))
		{
			return false;
		}
		return this.equals((ProductFeature) other);
	}

	public final boolean equals(final ProductFeature other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		return this.featureType.equals(other.featureType) && this.value.equals(other.value);
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
}
