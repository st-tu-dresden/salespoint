package org.salespointframework.util;

import java.util.Arrays;

// nützliche Klasse für hashcode, equals, nullchecks, etc
// ist in 1.7 vorhanden http://download.oracle.com/javase/7/docs/api/java/util/Objects.html
// wirf aber eine andere Exception bei requireNonNull


public final class Objects {
	private Objects() {}
	
	/*
	public static <T> T requireNonNull(T object) {
		if(object == null) {
			throw new ArgumentNullException();
		}
		return object;
	}
	*/
	
	public static <T> T requireNonNull(T object, String paramName) {
		if(object == null) {
			throw new ArgumentNullException(paramName);
		}
		return object;
	}
	
	public static int hash(Object... values) {
		return Arrays.hashCode(values);
	}
}
