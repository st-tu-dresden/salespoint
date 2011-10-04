package org.salespointframework.util;

/**
 * 
 * @author Paul Henke
 *
 * @param <T1>
 * @param <T2>
 */
public final class Tuple<T1, T2>
{
	private final T1 item1;
	private final T2 item2;

	private Tuple(T1 item1, T2 item2)
	{
		this.item1 = Objects.requireNonNull(item1, "item1");
		this.item2 = Objects.requireNonNull(item2, "item2");
	}

	public static final <T1, T2> Tuple<T1, T2> create(T1 item1, T2 item2)
	{
		return new Tuple<T1, T2>(item1, item2);
	}

	public final T1 getItem1()
	{
		return item1;
	}

	public final T2 getItem2()
	{
		return item2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (!(other instanceof Tuple))
		{
			return false;
		}
		return this.equals((Tuple) other);
	}

	@SuppressWarnings("rawtypes")
	public final boolean equals(Tuple other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		return this.item1.equals(other.item1) && this.item2.equals(other.item2);
	}

	@Override
	public final int hashCode()
	{
		return Objects.hash(item1, item2);
	}

	@Override
	public final String toString()
	{
		return item1.toString() + "," + item2.toString();
	}
}
