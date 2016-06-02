package org.salespointframework.payment;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or will be made.
 * 
 * @author Hannes Weisbach
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class PaymentMethod implements Serializable {

	/**
	 * Description of the <code>PaymentMethod</code> in human-readable form. Is not {@literal null}.
	 */
	private final @NonNull String description;
}
