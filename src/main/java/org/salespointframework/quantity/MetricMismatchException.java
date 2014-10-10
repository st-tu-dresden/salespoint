package org.salespointframework.quantity;

/**
 * @author Paul Henke
 */
@SuppressWarnings("serial")
public class MetricMismatchException extends RuntimeException {

	public MetricMismatchException(String text) {
		super(text);
	}

	public MetricMismatchException(Metric m1, Metric m2) {
		super("Metric 1 (" + m1.name() + ") does not match Metric 2 (" + m2.name() + ")");
	}

	public MetricMismatchException(String text, Metric m1, Metric m2) {
		super(text + "\n" + "Metric 1 (" + m1.name() + ") does not match Metric 2 (" + m2.name() + ")");
	}
}
