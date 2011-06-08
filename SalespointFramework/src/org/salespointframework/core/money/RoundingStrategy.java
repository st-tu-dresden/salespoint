package org.salespointframework.core.money;

import java.math.BigDecimal;

public interface RoundingStrategy {
	public BigDecimal round(BigDecimal amount);
}
