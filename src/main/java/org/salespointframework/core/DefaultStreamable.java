package org.salespointframework.core;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
class DefaultStreamable<T> implements Streamable<T> {

	private final Streamable<T> delegate;

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return delegate.iterator();
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return delegate.stream().map(Object::toString).collect(Collectors.joining(", "));
	}
}
