package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;

import org.salespointframework.util.Objects;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or
 * will be made.
 * 
 */
//@Entity
public abstract class PaymentMethod implements Serializable {
/*
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
*/
	private static final long serialVersionUID = 1L;

	private String desc;

	/**
	 * Constructor which takes a String, describing the
	 * <code>PaymentMethod</code> in human-readable form.
	 * 
	 * @param desc
	 *            Description of the <code>PaymentMethod</code> in
	 *            human-readable form. Must be non-null.
	 */
	public PaymentMethod(String desc) {
		this.desc = Objects.requireNonNull(desc, "desc");
	}

	public String toString() {
		return desc;
	}
}
