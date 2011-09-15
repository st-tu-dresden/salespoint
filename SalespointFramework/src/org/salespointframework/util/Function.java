package org.salespointframework.util;

public interface Function<T, TResult>
{
	TResult invoke(T arg);
}
