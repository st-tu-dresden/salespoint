package org.salespointframework.core.shop;

import java.util.Objects;

import org.salespointframework.core.time.DefaultTime;
import org.salespointframework.core.time.Time;

/**
 * Shop Singleton
 * @author Paul Henke
 * 
 */
public enum Shop {
	
	/**
	 *  A singleton instance of the Shop.
	 */
	INSTANCE;

	private Time time = new DefaultTime();

	/**
	 * Gets the global {@link Time}
	 * @return a Time instance
	 */
	public Time getTime()
	{
		return time;
	}

	/**
	 * Sets the global {@link Time}
	 * @param time the Time to be set
	 * @throws NullPointerException if time is null
	 */
	public void setTime(Time time)
	{
		this.time = Objects.requireNonNull(time, "time must not be null");
	}
}
