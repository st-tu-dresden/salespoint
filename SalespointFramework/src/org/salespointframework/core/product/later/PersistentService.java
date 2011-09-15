package org.salespointframework.core.product.later;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a ServiceInstance which provides basic
 * functionality
 * 
 */

@Entity
public class PersistentService extends PersistentProduct implements ServiceInstance
{
	// has embeddedid
	@JoinColumn(name = "SERVICETYPE_ID", referencedColumnName = "ID")
	private ServiceType serviceType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledStart;
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledEnd;
	private ServiceDeliveryStatus serviceDeliveryStatus;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentService()
	{
	}

	/**
	 * Parameterized constructor with
	 * 
	 * @param serviceType
	 *            The {@link ServiceType} of this ServiceInstance
	 * @param start
	 *            The start time of the ServiceInstance.
	 * @param end
	 *            The end time of the ServiceInstance.
	 * @throws IllegalArgumentException
	 *             if the start is after the end
	 * @throws IllegalArgumentException
	 *             if the start is before the start of the ServiceType
	 * @throws IllegalArgumentException
	 *             if the end is after the end of the ServiceType
	 */
	public PersistentService(ServiceType serviceType, DateTime start, DateTime end)
	{
		super(serviceType);
		this.serviceType = Objects.requireNonNull(serviceType, "serviceType");
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");

		if (start.isAfter(end))
		{
			throw new IllegalArgumentException("A serviceInstance cannot end before it starts.");
		}

		if (start.isBefore(serviceType.getStartOfPeriodOfOperation()))
		{
			throw new IllegalArgumentException("A serviceInstance cannot begin before the period of serviceType has begun.");
		}

		if (end.isAfter(serviceType.getEndOfPeriodOfOperation()))
		{
			throw new IllegalArgumentException("A serviceType cannot end after the period of serviceType was finished.");
		}

		this.scheduledStart = Objects.requireNonNull(start.toDate(), "start");
		this.scheduledEnd = Objects.requireNonNull(end.toDate(), "end");
		this.serviceDeliveryStatus = ServiceDeliveryStatus.SCHEDULED;
	}

	@Override
	public DateTime getStart()
	{
		return new DateTime(scheduledStart);
	}

	@Override
	public DateTime getEnd()
	{
		return new DateTime(scheduledEnd);
	}

	@Override
	public ServiceDeliveryStatus getServiceDeliveryStatus()
	{
		if (this.serviceDeliveryStatus == ServiceDeliveryStatus.CANCELLED)
		{
		} else
		{
			if (Shop.INSTANCE.getTime().getDateTime().isBefore(getStart()))
			{
				this.serviceDeliveryStatus = ServiceDeliveryStatus.SCHEDULED;
			} else
			{
				if (Shop.INSTANCE.getTime().getDateTime().isAfter(getEnd()))
				{
					this.serviceDeliveryStatus = ServiceDeliveryStatus.COMPLETED;
				}

				else
				{
					this.serviceDeliveryStatus = ServiceDeliveryStatus.EXECUTING;
				}
			}
		}
		return this.serviceDeliveryStatus;
	}

	@Override
	public ServiceDeliveryStatus getServiceDeliveryStatusOnTime(DateTime dateTime)
	{
		if (this.serviceDeliveryStatus == ServiceDeliveryStatus.CANCELLED)
		{
		} else
		{
			if (dateTime.isBefore(getStart()))
			{
				this.serviceDeliveryStatus = ServiceDeliveryStatus.SCHEDULED;
			} else
			{
				if (dateTime.isAfter(getEnd()))
				{
					this.serviceDeliveryStatus = ServiceDeliveryStatus.COMPLETED;
				}

				else
				{
					this.serviceDeliveryStatus = ServiceDeliveryStatus.EXECUTING;
				}
			}
		}
		return this.serviceDeliveryStatus;
	}

	@Override
	public void cancelServiceInstance()
	{
		this.scheduledEnd = Shop.INSTANCE.getTime().getDateTime().toDate();
		this.serviceDeliveryStatus = ServiceDeliveryStatus.CANCELLED;
	}

	@Override
	public ServiceType getServiceType()
	{
		return this.serviceType;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (!(other instanceof PersistentService))
		{
			return false;
		}
		return this.equals((PersistentService) other);
	}

	/**
	 * Determines if the given {@link ServiceInstance} is equal to this one or
	 * not. Two ServiceInstances are equal to each other, if their hash code is
	 * the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */

	public final boolean equals(PersistentService other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		return this.getSerialNumber().equals(other.getSerialNumber());
	}
}
