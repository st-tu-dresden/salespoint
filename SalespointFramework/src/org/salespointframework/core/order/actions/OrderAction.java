package org.salespointframework.core.order.actions;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;

/**
 * Entity implementation class for Entity: OrderAction
 * 
 * @author hannesweisbach
 */
@Entity
public class OrderAction implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private static final long serialVersionUID = 1L;

	// FIXME implement classes
	// private OrderIdentifier orderIdentifier;
	private DateTime dateAuthorized;
	private boolean processed;

	// private UserIdentifier authorization;

	public OrderAction() {
	}

}
