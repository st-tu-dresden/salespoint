package org.salespointframework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

//TODO
// SCHEISS Collection<T> die Idioten von Sun hätten mal ihre APIs um Iterable<T> erweitern sollen, alles muss man selber machen....
// http://google-collections.googlecode.com/svn/trunk/javadoc/com/google/common/collect/Iterables.html
// ob es sich lohnt die ganze lib reinzuziehen?

public final class Iterables {

	private Iterables() {
	}

	public static <T> List<T> toList(final Iterable<T> iterable) {
		List<T> temp = new ArrayList<T>();
		for (T item : iterable) {
			temp.add(item);
		}
		return temp;
	}

	public static <T> Set<T> toSet(final Iterable<T> iterable) {
		Set<T> temp = new HashSet<T>();
		for (T item : iterable) {
			temp.add(item);
		}
		return temp;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(final Iterable<T> iterable) {
		return (T[]) toList(iterable).toArray();
	}

	public static <T> boolean isEmpty(final Iterable<T> iterable) {
		return !iterable.iterator().hasNext();
	}

	
	//WHAT THE FUCK
	//einziger Use Case von anonymen Klassen: andere Fuckups in der Sprache/Lib kaschieren
	public static <T> Iterable<T> from(final Iterable<T> iterable) {
		final Iterator<T> iterator = iterable.iterator();
		return new Iterable<T>() {
			@Override
            public Iterator<T> iterator() {
				return new Iterator<T>() {
					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public T next() {
						return iterator.next();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static <T> Iterable<T> from(final T[] array) {
		return new Iterable<T>() {
			@Override
            public Iterator<T> iterator() {
				return new Iterator<T>() {
					int n = 0;
					@Override
					public boolean hasNext() {
						return n < array.length;
					}

					@Override
					public T next() {
						if(n >= array.length) throw new NoSuchElementException();
						T element = array[n];
						n++;
						return element; 
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	

}
