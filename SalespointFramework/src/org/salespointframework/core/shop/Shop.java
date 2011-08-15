package org.salespointframework.core.shop;

import java.util.HashMap;
import java.util.Map;

import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.time.DefaultTime;
import org.salespointframework.core.time.Time;
import org.salespointframework.core.users.UserManager;
import org.salespointframework.util.Objects;

public enum Shop {
	INSTANCE;

	private Time time = new DefaultTime();
	private Accountancy accountancy;
	private OrderManager ordermanager;
	private Map<String, UserManager<?>> userManagerMap = new HashMap<String, UserManager<?>>();

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

	public void addUserManager(final String name,
			final UserManager<?> userManager) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(userManager, "userManager");
		userManagerMap.put(name, userManager);
	}

	public UserManager<?> getUserManager(final String name) {
		Objects.requireNonNull(name, "name");
		return userManagerMap.get(name);
	}

	public void removeUserManager(final String name) {
		Objects.requireNonNull(name, "name");
		userManagerMap.remove(name);
	}

}
