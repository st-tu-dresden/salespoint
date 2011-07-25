package org.salespointframework.core.order.actions;

import java.io.Serializable;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Objects;

/**
 * The class <code>OrderAction</code> is a super class for all actions which can
 * be applied to an <code>Order</code>. <code>OrderAction</code>s initiate
 * transitions between states of an <code>Order</code>.
 * 
 * @author hannesweisbach
 */
@Entity
public class OrderAction implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private static final long serialVersionUID = 1L;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="ORDER_ID"))
	private OrderIdentifier orderIdentifier;

	private DateTime dateAuthorized;
	private boolean processed = false;

	@Embedded
	@AttributeOverride(name="id", column=@Column(name="USER_ID"))
	private UserIdentifier authorization;

	
	/**
	 * Parameterless constructor as required by JPA. Don't use it directly.
	 */
	@Deprecated
	protected OrderAction() {
	}

	/**
	 * Instantiate a new <code>OrderAction</code>. The parameter
	 * <code>orderIdentifier</code> is mandatory, as it identifies the
	 * <code>Order</code> to which this action should be applied to.
	 * <code>UserIdentifier</code> is an optional parameter (may be
	 * <code>null</code>), if <code>processed</code> is <code>false</code>. If
	 * <code>processed</code> is set to <code>true</code> ,
	 * <code>userIdentifier</code> has to be <code>non-null</code>. If so, the
	 * date of the authorization is set to the current time.
	 * 
	 * @param orderIdentifier
	 *            Identifier of the <code>Order</code> to which this action
	 *            belongs to.
	 * @param processed
	 *            designates whether this action has already been processed or
	 *            not
	 * @param userIdentifier
	 *            identifies the party by whom the processing of this action was
	 *            authorized
	 */
	public OrderAction(OrderIdentifier orderIdentifier, boolean processed,
			UserIdentifier userIdentifier) {
		this.orderIdentifier = Objects.requireNonNull(orderIdentifier,
				"orderIdentifier");
		if (processed) {
			dateAuthorized = new DateTime();
			this.processed = true;

			this.authorization = Objects.requireNonNull(userIdentifier,
					"userIdentifier");
		} else if (userIdentifier != null) {
			this.authorization = userIdentifier;
		}
	}

	/**
	 * Instantiate a non-processed OrderAction. This is equal to
	 * <code>OrderAction(orderIdentifier, false, null)</code>
	 * 
	 * @param orderIdentifier
	 *            Identifier of the <code>Order</code> to which this action
	 *            should be applied to.
	 */
	public OrderAction(OrderIdentifier orderIdentifier) {
		this(orderIdentifier, false, null);
	}

	/**
	 * Set an unprocessed action to processed. If the action is already
	 * processed, a call to this method does nothing. Otherwise, the
	 * <code>userIdentifier</code> identifies the party which authorizes the
	 * processing of this action. The date of the authorization is set to the
	 * current time.
	 * 
	 * @param userIdentifier
	 *            The user who authorized the processing of the action.
	 */
	public void processAction(UserIdentifier userIdentifier) {
		if (!processed) {
			this.authorization = Objects.requireNonNull(userIdentifier,
					"userIdentifier");
			this.dateAuthorized = new DateTime();
			this.processed = true;
		}
	}

	/**
	 * Returns the <code>OrderIdentifier</code> associated with this
	 * <code>OrderAction</code>.
	 * 
	 * @return the orderIdentifier
	 */
	public OrderIdentifier getOrderIdentifier() {
		return orderIdentifier;
	}

	/**
	 * Returns the date, when this <code>OrderAction</code> was authorized, or
	 * <code>null</code>.
	 * 
	 * @return the dateAuthorized
	 */
	public DateTime getDateAuthorized() {
		return dateAuthorized;
	}

	/**
	 * Returns whether this <code>OrderAction</code> was already processed.
	 * 
	 * @return the processed
	 */
	public boolean isProcessed() {
		return processed;
	}

	/**
	 * Returns the UserIdentifier of the party which authorized the processing
	 * of this <code>OrderAction</code> or null.
	 * 
	 * @return the authorization
	 */
	public UserIdentifier getAuthorization() {
		return authorization;
	}
}
