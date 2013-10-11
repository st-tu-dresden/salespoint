package org.salespointframework.core.shop;

import java.util.Objects;

import org.salespointframework.core.time.DefaultTime;
import org.salespointframework.core.time.Time;
import org.salespointframework.core.user.TransientUserManager;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserManager;

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
	private UserManager<? extends User> usermanager;

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

	/**
	 * Gets the global {@link UserManager}
	 * @return an UserManager instance
	 */
	public UserManager<? extends User> getUserManager()
	{
		return usermanager;
	}

	/**
	 * Sets the global {@link UserManager}
	 * @param userManager the UserManager to be set
	 * @throws NullPointerException if userManager is null
	 */
	public void setUserManager(UserManager<? extends User> userManager)
	{
		this.usermanager = Objects.requireNonNull(userManager, "userManager must not be null");
	}

	
	/**
	 * Initializes the Shop with all <code>Transient</code> classes.
	 */
	public void initializeTransient()
	{
		Shop shop = Shop.INSTANCE;
		
		shop.setUserManager(new TransientUserManager());
	}
}
