package org.salespointframework.accountancy.payment;

import java.io.Serializable;
import java.util.Objects;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or
 * will be made.
 * 
 * @author Hannes Weisbach
 */
@SuppressWarnings("serial")
public abstract class PaymentMethod implements Serializable {

	/**
	 * Description of the <code>PaymentMethod</code> in human-readable form. Is
	 * not null.
	 */
	private final String description;

	/**
	 * Constructor which takes a String, describing the
	 * <code>PaymentMethod</code> in human-readable form.
	 * 
	 * @param description
	 *            Description of the <code>PaymentMethod</code> in
	 *            human-readable form. Must be non-null.
	 */
	public PaymentMethod(String description) {
		this.description = Objects.requireNonNull(description, "description must not be null");
	}

	/**
	 * The string representation of this <code>PaymentMethod</code>. It's the
	 * description that was given to the constructor.
	 * 
	 * @return The description of this <code>PaymentMethod</code>.
	 */
	@Override
	public String toString() {
		return description;
	}
}
