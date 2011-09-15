package org.salespointframework.util;

/**
 * The filter determines whether a given object matches the filter criteria or
 * not.
 * 
 * @author unknown
 * 
 * @param <T>
 *            Type of elements that should be filtered.
 */
public interface Filter<T> extends Function<T, Boolean>
{

}
