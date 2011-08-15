package org.salespointframework.util;

import java.util.Arrays;
import java.util.Iterator;

// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()
// USE Iterables.from()

// verhindert Iterable -> Collection Casts 
// denn Iterable/Iterator ist readonly, Collection ist es nicht
@Deprecated
public final class SalespointIterable<T> implements Iterable<T> {
	@Deprecated
	private Iterable<T> source;
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	@Deprecated
	private SalespointIterable(Iterable<T> source) {
		this.source = Objects.requireNonNull(source, "source");
	}
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	@Deprecated
	public static <T> Iterable<T> from(Iterable<T> source) {
		return new SalespointIterable<T>(source);
	}
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	@Deprecated
	private SalespointIterable(T[] source) {
		this.source = Arrays.asList(Objects.requireNonNull(source, "source"));
	}
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	@Deprecated
	public static <T> Iterable<T> from(T[] source) {
		return new SalespointIterable<T>(source);
	}
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	// USE Iterables.from()
	@Override
	@Deprecated
	public final Iterator<T> iterator() {
		return source.iterator();
	}
}
