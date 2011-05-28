package org.salespointframework.core.users;

public interface UserManager<T extends User> {
	Iterable<T> getUsers();
}
