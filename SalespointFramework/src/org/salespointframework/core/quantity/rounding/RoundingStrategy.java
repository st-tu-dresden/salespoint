package org.salespointframework.core.quantity.rounding;

import java.math.BigDecimal;

public interface RoundingStrategy {
	public BigDecimal round(BigDecimal amount);
}
