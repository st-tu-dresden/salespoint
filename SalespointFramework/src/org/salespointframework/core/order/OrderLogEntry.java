package org.salespointframework.core.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;


/**
 * @author Thomas Dedek
 *
 */
@SuppressWarnings("serial")
@Embeddable
public class OrderLogEntry implements Serializable, Comparable<OrderLogEntry>
{
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	private String description;
	private String action;

	@Deprecated
	protected OrderLogEntry()
	{
	}

	public OrderLogEntry(String action, String description)
	{
		this.description = Objects.requireNonNull(description, "description");
		this.action = Objects.requireNonNull(action, "action");
		this.dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return the dateCreated
	 */
	public DateTime getDateCreated()
	{
		return new DateTime(dateCreated);
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}

	@Override
	public int compareTo(OrderLogEntry logEntry)
	{
		return this.dateCreated.compareTo(logEntry.dateCreated);
	}
}
