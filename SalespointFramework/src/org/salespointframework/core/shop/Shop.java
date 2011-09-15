package org.salespointframework.core.shop;

import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.core.calendar.Calendar;
import org.salespointframework.core.calendar.CalendarEntry;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.time.DefaultTime;
import org.salespointframework.core.time.Time;
import org.salespointframework.core.users.User;
import org.salespointframework.core.users.UserManager;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Paul Henke
 * 
 */
public enum Shop {
	INSTANCE;

	private Time time = new DefaultTime();
	private Accountancy<? extends AccountancyEntry> accountancy;
	private Calendar<? extends CalendarEntry> calendar;
	private OrderManager<? extends Order<? extends OrderLine, ? extends ChargeLine>, ? extends OrderLine, ? extends ChargeLine> ordermanager;
	private UserManager<? extends User> usermanager;
	private Inventory<? extends Product> inventory;

	public Accountancy<? extends AccountancyEntry> getAccountancy()
	{
		return accountancy;
	}

	public void setAccountancy(final Accountancy<? extends AccountancyEntry> accountancy)
	{
		this.accountancy = Objects.requireNonNull(accountancy, "accountancy");
	}

	public OrderManager<? extends Order<? extends OrderLine, ? extends ChargeLine>, ? extends OrderLine, ? extends ChargeLine> getOrderManager()
	{
		return ordermanager;
	}

	public void setOrderManager(final OrderManager<? extends Order<? extends OrderLine, ? extends ChargeLine>, ? extends OrderLine, ? extends ChargeLine> ordermanager)
	{
		this.ordermanager = Objects.requireNonNull(ordermanager, "ordermanager");
	}

	public Time getTime()
	{
		return time;
	}

	public void setTime(final Time time)
	{
		this.time = Objects.requireNonNull(time, "time");
	}

	public UserManager<? extends User> getUserManager()
	{
		return usermanager;
	}

	public void setUserManager(final UserManager<? extends User> userManager)
	{
		this.usermanager = Objects.requireNonNull(userManager, "userManager");
	}

	public Inventory<? extends Product> getInventory()
	{
		return inventory;
	}

	public void setInventory(final Inventory<? extends Product> inventory)
	{
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	public Calendar<? extends CalendarEntry> getCalendar()
	{
		return calendar;
	}

	public void setCalendar(Calendar<? extends CalendarEntry> calendar)
	{
		this.calendar = Objects.requireNonNull(calendar, "calendar");
	}
}
