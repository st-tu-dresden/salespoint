package org.salespointframework.quantity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An entity that embeds {@link Quantity} and repeats its attribute names,
 * {@code amount} and {@code metric}.
 * 
 * @author Martin Morgenstern
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class RepeatedNameEntity {
	@Id
	@GeneratedValue
	private long id;

	@Getter
	@Setter
	private int amount;

	@Getter
	@Setter
	private String metric;

	@Getter
	@Setter
	private Quantity quantity;
}
