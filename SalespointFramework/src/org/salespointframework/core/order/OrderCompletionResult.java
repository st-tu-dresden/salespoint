package org.salespointframework.core.order;


/**
 * An <code>OrderCompletionResult</code> is returned after you call Order.completeOrder()
 *  
 * @author Paul Henke
 * 
 */
public interface OrderCompletionResult
{
	@SuppressWarnings("javadoc")
	public enum OrderCompletionStatus {
		SUCCESSFUL, FAILED, /*FAILED_ITEMS_MISSING, */ /* SPLITORDER */
	}

	/**
	 * 
	 * @return the OrderCompletionStatus of the {@link Order}
	 */
	OrderCompletionStatus getStatus();

	
	// TODO später rollback, split
	
	/**
	 * Call if you don't want a split order 
	 * @return	<code>true</code> if rollback was successful, otherwise <code>false</code>
	 * 	
	 */
	// 
	/*
	boolean rollBack();
	*/
	/**
	 * Creates and returns a Splitorder
	 * @return a new {@link Order}
	 */
	/*
	Order<OrderLine> splitOrder();
	*/
	
	/**
	 * Returns all removed {@link ProductInstance}s
	 * @return an {@link Iterable} of {@link ProductInstance}
	 */
	/*
	Iterable<ProductInstance> getRemovedInstances();
	*/
}
