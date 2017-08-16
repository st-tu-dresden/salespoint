/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.util.Assert;

/**
 * Simple interface to ease streamability of {@link Iterable}s.
 * 
 * @author Oliver Gierke
 */
public interface Streamable<T> extends Iterable<T> {

	/**
	 * Creates a non-parallel {@link Stream} of the underlying {@link Iterable}.
	 * 
	 * @return will never be {@literal null}.
	 */
	default Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	/**
	 * Returns a {@link Streamable} for the given {@link Iterable}.
	 * 
	 * @param iterable must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static <T> Streamable<T> of(Iterable<T> iterable) {

		Assert.notNull(iterable, "Iterable must not be null!");

		return DefaultStreamable.of(() -> iterable.iterator());
	}

	/**
	 * Creates a new {@link Streamable} from the given {@link Supplier} that can produce a {@link Stream} in the first
	 * place. The given {@link Supplier} will only be accessed once the created {@link Streamable} is actually used, i.e.
	 * {@link #stream()} is called.
	 * 
	 * @param supplier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static <T> Streamable<T> ofLazy(Supplier<Stream<T>> supplier) {

		Assert.notNull(supplier, "Supplier must not be null!");

		return DefaultStreamable.of(new Streamable<T>() {

			/* 
			 * (non-Javadoc)
			 * @see org.salespointframework.core.Streamable#stream()
			 */
			@Override
			public Stream<T> stream() {
				return supplier.get();
			}

			/*
			 * (non-Javadoc)
			 * @see java.lang.Iterable#iterator()
			 */
			@Override
			public Iterator<T> iterator() {
				return stream().iterator();
			}
		});
	}

	/**
	 * Returns an empty {@link Streamable}.
	 * 
	 * @return will never be {@literal null}.
	 */
	public static <T> Streamable<T> empty() {
		return of(Collections.emptyList());
	}
}
