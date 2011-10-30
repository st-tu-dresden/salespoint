package org.salespointframework.core.order;

// TODO comment
/**
 * An <code>OrderCompletionResult</code> is returned after you call Order.completeOrder()
 *  
 * @author Paul Henke
 * 
 */
public interface OrderCompletionResult
{
	public enum OrderCompletionStatus {
		SUCCESSFUL, SPLITORDER, FAILED
	}

	/**
	 * 
	 * @return the OrderCompletionStatus of the {@link Order}
	 */
	OrderCompletionStatus getStatus();

	/**
	 * Call if you don't want a split order 
	 * @return	<code>true</code> if rollback was successful, otherwise <code>false</code>
	 * 	
	 */
	boolean rollBack();

	Order<OrderLine> splitOrder();
}
