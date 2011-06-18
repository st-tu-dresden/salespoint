package org.salespointframework.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

// verhindert Iterable -> Collection Casts 
// denn Iterable/Iterator ist readonly, Collection ist es nicht
public class SalespointIterable<T> implements Iterable<T> {

	private Iterable<T> source;
	
	public SalespointIterable(T[] source) {
		this.source = Arrays.asList(source);
	}
	
	public SalespointIterable(Collection<T> source) {
		this.source = source;
	}
	
	public SalespointIterable(Iterable<T> source) {
		this.source = source;
	}

	@Override
	public Iterator<T> iterator() {
		return source.iterator();
	}
}
