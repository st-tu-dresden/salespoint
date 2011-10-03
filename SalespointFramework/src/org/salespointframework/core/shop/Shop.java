package org.salespointframework.core.shop;

import javax.naming.TimeLimitExceededException;

import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.calendar.Calendar;
import org.salespointframework.core.calendar.CalendarEntry;
import org.salespointframework.core.calendar.PersistentCalendar;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.time.DefaultTime;
import org.salespointframework.core.time.Time;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserManager;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Objects;

/**
 * Shop Singleton
 * TODO
 * @author Paul Henke
 * 
 */
public enum Shop {
	/**
	 *  A singleton instance of the Shop.
	 */
	INSTANCE;

	private Time time = new DefaultTime();
	private Accountancy<? extends AccountancyEntry> accountancy;
	private Calendar<? extends CalendarEntry> calendar;
	private OrderManager<? extends Order<? extends OrderLine>, ? extends OrderLine> ordermanager;
	private UserManager<? extends User> usermanager;
	private Inventory<? extends Product> inventory;
	private Catalog<? extends ProductType> catalog;

	/**
	 * Gets the global {@link Accountancy}
	 * @return an Accountancy instance
	 */
	public Accountancy<? extends AccountancyEntry> getAccountancy()
	{
		return accountancy;
	}

	/**
	 * Sets the global {@link Accountancy}
	 * @param accountancy the Accountancy to be set
	 * @throws ArgumentNullException if accountancy is null
	 */
	public void setAccountancy(Accountancy<? extends AccountancyEntry> accountancy)
	{
		this.accountancy = Objects.requireNonNull(accountancy, "accountancy");
	}

	/**
	 * Gets the global {@link OrderManager}
	 * @return an OrderManager instance
	 */
	public OrderManager<? extends Order<? extends OrderLine>, ? extends OrderLine> getOrderManager()
	{
		return ordermanager;
	}

	/**
	 * Sets the global {@link OrderManager}
	 * @param ordermanager the OrderManager to be set
	 * @throws ArgumentNullException if ordermanager is null
	 */
	public void setOrderManager(OrderManager<? extends Order<? extends OrderLine>, ? extends OrderLine> ordermanager)
	{
		this.ordermanager = Objects.requireNonNull(ordermanager, "ordermanager");
	}

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
	 * @throws ArgumentNullException if time is null
	 */
	public void setTime(Time time)
	{
		this.time = Objects.requireNonNull(time, "time");
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
	 * @throws ArgumentNullException if userManager is null
	 */
	public void setUserManager(UserManager<? extends User> userManager)
	{
		this.usermanager = Objects.requireNonNull(userManager, "userManager");
	}

	/**
	 * Gets the global {@link Inventory}
	 * @return an Inventory instance
	 */
	public Inventory<? extends Product> getInventory()
	{
		return inventory;
	}

	/**
	 * Sets the global {@link Inventory}
	 * @param inventory the Inventory to be set
	 * @throws ArgumentNullException if inventory is null
	 */
	public void setInventory(Inventory<? extends Product> inventory)
	{
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	/**
	 * Gets the global {@link Calendar}
	 * @return a Calendar instance
	 */
	public Calendar<? extends CalendarEntry> getCalendar()
	{
		return calendar;
	}

	/**
	 * Sets the global {@link Calendar}
	 * @param calendar the Calendar to be set
	 * @throws ArgumentNullException if calendar is null
	 */
	public void setCalendar(Calendar<? extends CalendarEntry> calendar)
	{
		this.calendar = Objects.requireNonNull(calendar, "calendar");
	}

	/**
	 * Gets the global {@link Catalog}
	 * @return a Catalog instance
	 */
	public Catalog<? extends ProductType> getCatalog()
	{
		return catalog;
	}

	/**
	 * Sets the global {@link Catalog}
	 * @param catalog the Catalog to be set
	 * @throws ArgumentNullException if catalog is null
	 */
	public void setCatalog(Catalog<? extends ProductType> catalog)
	{
		this.catalog = catalog;
	}
	
	// TODO alle aufzählen?
	/**
	 * Initializes the Shop with all <code>Persistent</code> classes.
	 */
	public static void initializeShop()
	{
		Shop shop = Shop.INSTANCE;

		shop.setAccountancy(new PersistentAccountancy());
		shop.setCalendar(new PersistentCalendar());
		shop.setInventory(new PersistentInventory());
		shop.setCatalog(new PersistentCatalog());
		shop.setOrderManager(new PersistentOrderManager());
		shop.setUserManager(new PersistentUserManager());
	}
}
