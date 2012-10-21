package org.salespointframework.core.quantity;

/**
 * 
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
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
