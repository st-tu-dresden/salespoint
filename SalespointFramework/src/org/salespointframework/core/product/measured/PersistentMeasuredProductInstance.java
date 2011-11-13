package org.salespointframework.core.product.measured;

import java.math.BigDecimal;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductInstance;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a MeasuredProductInstance which
 * provides basic functionality
 * 
 */

@Entity
public class PersistentMeasuredProductInstance extends PersistentProductInstance implements MeasuredProductInstance
{
	private Quantity quantity;
	private Money unitPrice;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentMeasuredProductInstance()
	{
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param product
	 *            The productType of this MeasuredProductInstance
	 * @param quantity
	 *            The quantity of this MeasuredProductInstance
	 */
	//TODO for later: Quantity to <T extends Quantity>
	public PersistentMeasuredProductInstance(MeasuredProduct product, Quantity quantity)
	{
		super(product);
		Objects.requireNonNull(product, "product");
		this.unitPrice = product.getUnitPrice();
		this.quantity = Objects.requireNonNull(quantity, "quantity");
		product.reduceQuantityOnHand(this.quantity);
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param product
	 *            The productType of this MeasuredProductInstance
	 * @param amount
	 *            The amount of the quantity, which will be used for this
	 *            MeasuredProductInstance as Integer Value.
	 */
	public PersistentMeasuredProductInstance(MeasuredProduct product, int amount)
	{
		super(product);
		Objects.requireNonNull(product, "product");
		this.unitPrice = product.getUnitPrice();
		this.quantity = new Quantity(amount, product.getQuantityOnHand().getMetric(), product.getQuantityOnHand().getRoundingStrategy());
		product.reduceQuantityOnHand(quantity);
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param product
	 *            The productType of this MeasuredProductInstance
	 * @param amount
	 *            The amount of the quantity, which will be used for this
	 *            MeasuredProductInstance as BigDecimal Value.
	 */
	public PersistentMeasuredProductInstance(MeasuredProduct product, BigDecimal amount)
	{
		super(product);
		Objects.requireNonNull(product, "product");
		this.unitPrice = product.getUnitPrice();
		this.quantity = new Quantity(amount, product.getQuantityOnHand().getMetric(), product.getQuantityOnHand().getRoundingStrategy());
		product.reduceQuantityOnHand(quantity);
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param product
	 *            The productType of this MeasuredProductInstance
	 * @param amount
	 *            The amount of the quantity, which will be used for this
	 *            MeasuredProductInstance as Long Value.
	 */
	public PersistentMeasuredProductInstance(MeasuredProduct product, long amount)
	{
		super(product);
		Objects.requireNonNull(product, "product");
		this.unitPrice = product.getUnitPrice();
		this.quantity = new Quantity(amount, product.getQuantityOnHand().getMetric(), product.getQuantityOnHand().getRoundingStrategy());
		product.reduceQuantityOnHand(quantity);
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param product
	 *            The productType of this MeasuredProductInstance
	 * @param amount
	 *            The amount of the quantity, which will be used for this
	 *            MeasuredProductInstance as Float Value.
	 */
	public PersistentMeasuredProductInstance(MeasuredProduct product, float amount)
	{
		super(product);
		Objects.requireNonNull(product, "product");
		this.unitPrice = product.getUnitPrice();
		this.quantity = new Quantity(amount, product.getQuantityOnHand().getMetric(), product.getQuantityOnHand().getRoundingStrategy());
		product.reduceQuantityOnHand(quantity);
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param product
	 *            The productType of this MeasuredProductInstance
	 * @param amount
	 *            The amount of the quantity, which will be used for this
	 *            MeasuredProductInstance as Double Value.
	 */
	public PersistentMeasuredProductInstance(MeasuredProduct product, double amount)
	{
		super(product);
		Objects.requireNonNull(product, "product");
		this.unitPrice = product.getUnitPrice();
		this.quantity = new Quantity(amount, product.getQuantityOnHand().getMetric(), product.getQuantityOnHand().getRoundingStrategy());
		product.reduceQuantityOnHand(quantity);
	}

	@Override
	public Quantity getQuantity()
	{
		return quantity;
	}

	@Override
	public Money getPrice()
	{
		// return (Money) productType.getUnitPrice().multiply(quantity);
		return quantity.multiply(this.unitPrice);
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
		if (!(other instanceof PersistentMeasuredProductInstance))
		{
			return false;
		}
		return this.equals((PersistentMeasuredProductInstance) other);
	}

	/**
	 * Determines if the given {@link MeasuredProductInstance} is equal to this one or
	 * not. Two MeasuredProductInstances are equal to each other, if their hash
	 * code is the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */

	public boolean equals(PersistentMeasuredProductInstance other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		return this.getSerialNumber().equals(other.getSerialNumber());
	}

}
