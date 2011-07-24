package org.salespointframework.core.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.order.actions.OrderAction;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@Entity
public class Order {
	
	@Id
	@OneToOne(cascade = CascadeType.ALL)
	private OrderIdentifier orderIdentifier;
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private long id;

	// Do NOT fucking touch!
	@Column(name = "TimeStamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@SuppressWarnings("unused")
	private Date timeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	private String salesChannel;
	private String termsAndConditions;
	private List<OrderAction> orderActions;
	private List<OrderLine> orderLines;
	//private List<ChargeLine> chargeLines;
	private OrderStatus status;
	
	public Order(String salesChannel, String termsAndConditions) {
		orderIdentifier = new OrderIdentifier();
		dateCreated = new Date();
		this.salesChannel = Objects
				.requireNonNull(salesChannel, "salesChannel");
		this.termsAndConditions = Objects.requireNonNull(termsAndConditions,
				"termsAndConditions");
		orderActions = new ArrayList<OrderAction>();
		orderLines = new ArrayList<OrderLine>();
		status = OrderStatus.INITIALIZED;
	}

	public Order(String salesChannel) {
		this(Objects.requireNonNull(salesChannel, "salesChannel"), "");
	}
		
	public Order() {
		this("", "");
	}

	public String toString() {
		return orderIdentifier.toString();
	}

	/**
	 * @return the orderIdentifier
	 */
	public OrderIdentifier getOrderIdentifier() {
		return orderIdentifier;
	}

	/**
	 * @return the dateCreated
	 */
	public DateTime getDateCreated() {
		return new DateTime(dateCreated);
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @return the termsAndConditions
	 */
	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	/**
	 * @return the orderAction
	 */
	public Iterable<OrderAction> getOrderAction() {
		return SalespointIterable.from(orderActions);
	}

}
