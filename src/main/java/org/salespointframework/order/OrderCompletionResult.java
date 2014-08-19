package org.salespointframework.order;

/**
 * An {@code OrderCompletionResult} is returned after you call {@link OrderManager#completeOrder(Order)}
 * 
 * @author Paul Henke
 */
public interface OrderCompletionResult {
	@SuppressWarnings("javadoc")
	public enum OrderCompletionStatus {
		SUCCESSFUL, FAILED, /*FAILED_ITEMS_MISSING, *//* SPLITORDER */
	}

	/**
	 * @return the OrderCompletionStatus of the {@link Order}
	 */
	OrderCompletionStatus getStatus();

	// TODO später rollback, split

	/**
	 * Call if you don't want a split order
	 * 
	 * @return {@literal true} if rollback was successful, otherwise {@literal false}
	 */
	//
	/*
	boolean rollBack();
	*/
	/**
	 * Creates and returns a Splitorder
	 * 
	 * @return a new {@link Order}
	 */
	/*
	Order<OrderLine> splitOrder();
	*/

	/**
	 * Returns all removed {@link ProductInstance}s
	 * 
	 * @return an {@link Iterable} of {@link ProductInstance}
	 */
	/*
	Iterable<ProductInstance> getRemovedInstances();
	*/
}
