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

	public static <T> List<T> asList(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");

		List<T> temp = new ArrayList<T>();
		for (T item : iterable)
		{
			temp.add(item);
		}
		return temp;
	}

	public static <T> Set<T> asSet(Iterable<T> iterable)
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
	public static <T> T[] asArray(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
		return (T[]) asList(iterable).toArray();
	}

	public static <T> boolean isEmpty(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
		return !iterable.iterator().hasNext();
	}

	public static <T> Iterable<T> empty()
	{
		return of(new ArrayList<T>(0));
	}

	public static <T> int size(Iterable<T> iterable)
	{
		Objects.requireNonNull(iterable, "iterable");
		int size = 0;
		for (Iterator<T> iterator = iterable.iterator(); iterator.hasNext(); iterator.next())
		{
			size++;
		}
		return size;
	}

	public static <T> T first(Iterable<T> iterable)
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

	public static <T> Iterable<T> of(Iterable<T> iterable)
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

	public static <T> Iterable<T> of(final T[] array)
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
