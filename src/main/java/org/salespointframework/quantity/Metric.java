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

	/**
	 * Returns the {@link Metric} for the given abbreviation.
	 * 
	 * @param abbreviation
	 * @return will never be {@literal null}.
	 * @throws IllegalArgumentException if no {@link Metric} can be found for the given abbreviation.
	 */
	public static Metric from(String abbreviation) {

		for (Metric metric : Metric.values()) {
			if (metric.getAbbreviation().equals(abbreviation)) {
				return metric;
			}
		}

		throw new IllegalArgumentException(String.format("Unsupported abbreviation %s!", abbreviation));
	}
}
