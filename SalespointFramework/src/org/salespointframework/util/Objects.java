package org.salespointframework.util;

import java.util.Arrays;

/**
 * This class provides useful methods for null-checking and hash calculation of
 * multiple objects.
 * inspired by http://download.oracle.com/javase/7/docs/api/java/util/Objects.html
 * 
 * @author Paul Henke
 * 
 */
public final class Objects
{
	// too bad there are no real static classes, -1 for java
	private Objects()
	{
	}

	/**
	 * This method is used to check if an object is <code>null</code> or not. In
	 * the case of an <code>null</code> reference, an exception is thrown
	 * otherwise the object is returned.
	 * 
	 * @param <T>
	 *            Type of the object that should be checked.
	 * @param object
	 *            The object that should be checked.
	 * @param paramName
	 *            The object's name to identify in exception description.
	 * @return The given object if it isn't <code>null</code>
	 * @throws ArgumentNullException if object is <code>null</code>
	 */
	public static <T> T requireNonNull(T object, String paramName)
	{
		if (object == null)
		{
			throw new ArgumentNullException(paramName);
		}
		return object;
	}

	/**
	 * Generates a hash code for a sequence of input values.
	 * This method is useful for implementing {@code Object.hashCode} on objects containing multiple fields.
	 * 
	 * @param values the values to be hashed
	 * @return a hash value of the sequence of input values
	 */
	public static int hash(Object... values)
	{
		return Arrays.hashCode(values);
	}
}
