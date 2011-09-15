package org.salespointframework.core.order;

/**
 * 
 * @author Paul
 *
 */
public interface OrderCompletionResult {

	public enum OrderCompletionStatus {
		SUCCESSFUL, SPLITORDER, FAILED
	}
	
	OrderCompletionStatus getStatus();
	
	boolean rollBack();
	
	Order<OrderLine, ChargeLine> splitOrder();

	Throwable getException();
	
}
