package org.salespointframework.core.order.actions;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.order.actions.PaymentAction;

/**
 * Entity implementation class for Entity: MakeRefundAction
 *
 * @author hannesweisbach
 */
@Entity

public class MakeRefundAction extends PaymentAction implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public MakeRefundAction() {
		super();
	}
   
}
