package org.salespointframework.util;

import java.util.Arrays;
import java.util.Iterator;

// verhindert Iterable -> Collection Casts 
// denn Iterable/Iterator ist readonly, Collection ist es nicht
public final class SalespointIterable<T> implements Iterable<T> {

	private Iterable<T> source;
	
	private SalespointIterable(Iterable<T> source) {
		this.source = Objects.requireNonNull(source, "source"); 
	}
	
	public static <T> Iterable<T> from(Iterable<T> source) {
		return new SalespointIterable<T>(source);
	}

	private SalespointIterable(T[] source) {
		Arrays.asList(Objects.requireNonNull(source, "source"));
	}
	
	public static <T> Iterable<T> from(T[] source) {
		return new SalespointIterable<T>(source);
	}

	@Override
	public final Iterator<T> iterator() {
		return source.iterator();
	}
}
