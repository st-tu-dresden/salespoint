package org.salespointframework.core.quantity;

public class MetricMismatchException extends RuntimeException
{
	public MetricMismatchException() {
		super();
	}

	public MetricMismatchException(String text)
	{
		super(text);
	}
}
