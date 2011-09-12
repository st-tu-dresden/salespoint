package org.salespointframework.core.shop;

import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.inventory.__Inventory__;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductType;
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
	private Accountancy accountancy;
	private OrderManager ordermanager;
	private UserManager<User> usermanager;
	private __Inventory__<Product, ProductType> inventory;

	public void setAccountancy(final Accountancy accountancy) {
		Objects.requireNonNull(accountancy, "accountancy");
		this.accountancy = accountancy;
	}

	public Accountancy getAccountancy() {
		return accountancy;
	}

	public void setOrderManager(final OrderManager ordermanager) {
		this.ordermanager = Objects
				.requireNonNull(ordermanager, "ordermanager");
	}

	public OrderManager getOrderManager() {
		return ordermanager;
	}

	public void setTime(final Time time) {
		this.time = Objects.requireNonNull(time, "time");
	}

	public Time getTime() {
		return time;
	}

	public void setUserManager(final UserManager<User> userManager) {
		this.usermanager = Objects.requireNonNull(userManager, "userManager");
	}

	public UserManager<User> getUserManager() {
		return usermanager;
	}
	
	public void setInventory(final __Inventory__<Product, ProductType> inventory) {
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	public __Inventory__<Product, ProductType> getInventory() {
		return inventory;
	}

	
}
