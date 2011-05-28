package org.salespoint.core.data.users;

public interface UserManager<T extends User> {
	Iterable<T> getUsers();
}
