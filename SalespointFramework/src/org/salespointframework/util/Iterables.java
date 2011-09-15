package org.salespointframework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

// TODO netter schreiben 
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
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T> List<T> toList(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");

		List<T> temp = new ArrayList<T>();
		for (T item : iterable)
		{
			temp.add(item);
		}
		return temp;
	}

	public static <T> Set<T> toSet(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");

		Set<T> temp = new HashSet<T>();
		for (T item : iterable)
		{
			temp.add(item);
		}
		return temp;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
		return (T[]) toList(iterable).toArray();
	}

	/**
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T> boolean isEmpty(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
		return !iterable.iterator().hasNext();
	}

	/**
	 * 
	 * @return
	 */
	public static <T> Iterable<T> empty()
	{
		return from(new ArrayList<T>(0));
	}

	/**
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T> int size(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
		int size = 0;
		for (Iterator<T> iterator = iterable.iterator(); iterator.hasNext(); iterator.next())
		{
			size++;
		}
		return size;
	}

	/**
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T> T first(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
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
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T> Iterable<T> from(final Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");

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
	 * 
	 * @param array
	 * @return
	 */
	public static <T> Iterable<T> from(final T[] array)
	{
		Objects.requireNonNull(array, "array");

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
