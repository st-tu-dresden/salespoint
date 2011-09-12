package org.salespointframework.core.order.paul;


/**
 * 
 * @author Paul
 *
 */
public interface OrderCompletionResult {
//
	public enum OrderCompletionStatus {
		SUCCESSFUL, FAILED
	}
	
	OrderCompletionStatus getOrderCompletionStatus();
	
	void rollBack();
	
	Order<OrderLine, ChargeLine> splitOrder();
}
