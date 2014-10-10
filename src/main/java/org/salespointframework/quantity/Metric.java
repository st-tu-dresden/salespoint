package org.salespointframework.quantity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.util.Assert;

/**
 * All available metrics.
 *
 * @author Oliver Gierke
 */
@Getter
@RequiredArgsConstructor
public enum Metric {

	METER("m"), KILOGRAM("kg"), LITER("l"), UNIT("");

	private final String abbreviation;

	/**
	 * Returns whether the given {@link Metric} is
	 * 
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public boolean isCompatibleWith(Metric metric) {

		Assert.notNull(metric, "Metric must not be null!");

		return this.equals(metric);
	}
}
