package org.salespointframework.core.quantity;

/**
 * 
 * @author Paul Henke
 * 
 */
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
