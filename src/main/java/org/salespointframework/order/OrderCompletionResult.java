package org.salespointframework.order;

/**
 * An {@code OrderCompletionResult} is returned after you call {@link OrderManager#completeOrder(Order)}
 * 
 * @author Paul Henke
 */
public interface OrderCompletionResult {

	@SuppressWarnings("javadoc")
	public enum OrderCompletionStatus {
		SUCCESSFUL, FAILED;
	}

	/**
	 * @return the OrderCompletionStatus of the {@link Order}
	 */
	OrderCompletionStatus getStatus();
}
