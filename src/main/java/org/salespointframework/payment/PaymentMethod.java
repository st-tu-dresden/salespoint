package org.salespointframework.payment;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or will be made.
 * 
 * @author Hannes Weisbach
 */
@SuppressWarnings("serial")
public abstract class PaymentMethod implements Serializable {

	/**
	 * Description of the <code>PaymentMethod</code> in human-readable form. Is not null.
	 */
	private final String description;

	/**
	 * Constructor which takes a String, describing the <code>PaymentMethod</code> in human-readable form.
	 * 
	 * @param description Description of the <code>PaymentMethod</code> in human-readable form. Must be non-null.
	 */
	public PaymentMethod(String description) {

		Assert.notNull(description, "Description must not be null");
		this.description = description;
	}

	/**
	 * The string representation of this <code>PaymentMethod</code>. It's the description that was given to the
	 * constructor.
	 * 
	 * @return The description of this <code>PaymentMethod</code>.
	 */
	@Override
	public String toString() {
		return description;
	}
}
