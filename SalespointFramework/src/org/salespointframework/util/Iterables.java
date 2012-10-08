package org.salespointframework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

// SCHEISS Collection<T> die Idioten von Sun hätten mal ihre APIs um Iterable<T> erweitern sollen, alles muss man selber machen....
// http://google-collections.googlecode.com/svn/trunk/javadoc/com/google/common/collect/Iterables.html
// ob es sich lohnt die ganze lib reinzuziehen?

/**
 * Utility class for working with Iterables
 * 
 * @author Paul Henke
 * 
 */
public final class Iterables
{
	// too bad there are no real static classes, -1 for java
	private Iterables()
	{
	}

	/**
	 * Converts an {@link Iterable} to a {@link List}
	 * @param iterable an Iterable 
	 * @return a List
	 * @throws NullPointerException if iterable is null 
	 */
	public static <T> List<T> asList(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");

		List<T> temp = new ArrayList<T>();
		for (T item : iterable)
		{
			temp.add(item);
		}
		return temp;
	}

	/**
	 * Converts an {@link Iterable} to a {@link Set}
	 * @param iterable an Iterable
	 * @return a Set
	 * @throws NullPointerException if iterable is null
	 */
	public static <T> Set<T> asSet(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");

		Set<T> temp = new HashSet<T>();
		for (T item : iterable)
		{
			temp.add(item);
		}
		return temp;
	}

	/**
	 * Converts an {@link Iterable} to an Array 
	 * @param iterable an Iterable
	 * @return an Array
	 * @throws NullPointerException if iterable is null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");
		return (T[]) asList(iterable).toArray();
	}

	/**
	 * Checks if an {@link Iterable} is empty
	 * @param iterable an Iterable
	 * @return true if iterable is empty, otherwise false
	 * @throws NullPointerException if iterable is null
	 */
	public static <T> boolean isEmpty(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");
		return !iterable.iterator().hasNext();
	}

	/**
	 * Creates a new empty {@link Iterable} of T
	 * @return an empty Iterable
	 */
	public static <T> Iterable<T> empty()
	{
		return of(Collections.<T>emptyList());
	}

	/**
	 * Calculates the length/size of an {@link Iterable}
	 * @param iterable an Iterable
	 * @return the number of elements in iterable
	 * @throws NullPointerException if iterable is null
	 */
	public static <T> int size(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");
		int size = 0;
		for (Iterator<T> iterator = iterable.iterator(); iterator.hasNext(); iterator.next())
		{
			size++;
		}
		return size;
	}

	/**
	 * Returns the first element of an {@link Iterable}
	 * @param iterable 
	 * @return the first element, if iterable is empty, null is returned 
	 * @throws NullPointerException if iterable is null
	 */
	public static <T> T first(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");
		Iterator<T> iterator = iterable.iterator();
		if (iterator.hasNext())
		{
			return iterator.next();
		} else
		{
			return null;
		}
	}

	/**
	 * Returns a lazy {@link Iterable} from another Iterable.
	 * {@link Iterator}.remove() throws an UnsupportedOperationException.
	 * @param iterable an Iterable
	 * @return a new Iterable 
	 * @throws NullPointerException if iterable is null
	 * 
	 */
	public static <T> Iterable<T> of(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable must not be null");

		final Iterator<T> iterator = iterable.iterator();
		return new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return new Iterator<T>()
				{
					@Override
					public boolean hasNext()
					{
						return iterator.hasNext();
					}

					@Override
					public T next()
					{
						return iterator.next();
					}

					@Override
					public void remove()
					{
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	
	/**
	 * Returns a lazy {@link Iterable} from an Array.
	 * {@link Iterator}.remove() throws an UnsupportedOperationException.
	 * @param array an Array
	 * @return a new Iterable
	 * @throws NullPointerException if array is null
	 */
	public static <T> Iterable<T> of(final T[] array)
	{
		Objects.requireNonNull(array, "array must not be null");

		return new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return new Iterator<T>()
				{
					int n = 0;
					@Override
					public boolean hasNext()
					{
						return n < array.length;
					}

					@Override
					public T next()
					{
						if (n >= array.length)
						{
							throw new NoSuchElementException();
						}
						T element = array[n];
						n++;
						return element;
					}

					@Override
					public void remove()
					{
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}
