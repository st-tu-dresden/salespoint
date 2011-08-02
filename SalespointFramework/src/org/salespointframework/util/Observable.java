package org.salespointframework.util;

public interface Observable<T> {
	void register(Observer<T> observer);
	void unregister(Observer<T> observer);
}
