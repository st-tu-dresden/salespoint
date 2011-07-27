package org.salespointframework.core.shop;

import java.util.HashMap;
import java.util.Map;

import org.salespointframework.core.order.IOrderManager;
import org.salespointframework.core.time.DefaultTime;
import org.salespointframework.core.time.Time;
import org.salespointframework.core.users.UserManager;

public enum Shop {
	INSTANCE;

	//TODO
	//Accountancy und co?
	
	private Time time = new DefaultTime();
	private IOrderManager ordermanager;
	private Map<String, UserManager> userManagerMap = new HashMap<String, UserManager>();
	
	public void setOrderManager(IOrderManager ordermanager) {
		this.ordermanager = ordermanager;
	}
	
	public IOrderManager getOrderManager() {
		return ordermanager;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
	public Time getTime() {
		return time;
	}
	
	public void addUserManager(String name, UserManager userManager) {
		userManagerMap.put(name, userManager);
	}
	
	public void removeUserManager(String name) {
		userManagerMap.remove(name);
	}
}
