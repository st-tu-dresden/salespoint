package org.salespointframework.core.order;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class OrderEntry {
	@Id
	@OneToOne(cascade=CascadeType.ALL)
	private OrderIdentifier orderIdentifier;
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private long id;

	// Do NOT fucking touch!
	@Column(name = "TimeStamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	public OrderEntry() {
		orderIdentifier = new OrderIdentifier();
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

}
