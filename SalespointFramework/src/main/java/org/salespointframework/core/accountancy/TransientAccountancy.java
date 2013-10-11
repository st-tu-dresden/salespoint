package org.salespointframework.core.accountancy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;

/**
 * 
 * @author Paul Henke
 * 
 */
public class TransientAccountancy implements Accountancy<TransientAccountancyEntry>
{
	private static final Map<AccountancyEntryIdentifier, TransientAccountancyEntry> entryMap = new ConcurrentHashMap<>();

	@Override
	public void add(TransientAccountancyEntry accountancyEntry)
	{
		Objects.requireNonNull(accountancyEntry, "accountancyEntry must not be null");
		entryMap.put(accountancyEntry.getIdentifier(), accountancyEntry);
	}

	@Override
	public <E extends TransientAccountancyEntry> Iterable<E> find(Class<E> clazz)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientAccountancyEntry entry : entryMap.values()) 
		{
			if(clazz.isAssignableFrom(entry.getClass())) 
			{ 
				temp.add(clazz.cast(entry));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public <E extends TransientAccountancyEntry> TransientAccountancyEntry get(Class<E> clazz, AccountancyEntryIdentifier accountancyEntryIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(accountancyEntryIdentifier, "accountancyEntryIdentifier must not be null");
		
		return clazz.cast(entryMap.get(accountancyEntryIdentifier));
	}

	@Override
	public <E extends TransientAccountancyEntry> Iterable<E> find(Class<E> clazz, DateTime from, DateTime to)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(from, "from must be not null");
		Objects.requireNonNull(to, "to must be not null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientAccountancyEntry entry : entryMap.values()) 
		{
			if(clazz.isAssignableFrom(entry.getClass()) && entry.getDate().isAfter(from) && entry.getDate().isBefore(to)) 
			{ 
				temp.add(clazz.cast(entry));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public <T extends TransientAccountancyEntry> Map<Interval, Iterable<T>> find(Class<T> clazz, DateTime from, DateTime to, Period period)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(from, "from must be not null");
		Objects.requireNonNull(to, "to must be not null");
		Objects.requireNonNull(period, "period must be not null");
		
		DateTime nextStep;
		Map<Interval, Iterable<T>> entries = new HashMap<Interval, Iterable<T>>();

		for (; from.isBefore(to.minus(period)); from = from.plus(period)) {
			nextStep = from.plus(period);
			entries.put(new Interval(from, nextStep),
					find(clazz, from, nextStep));
		}
		/*
		 * Remove last interval from loop, to save the test for the last
		 * interval in every iteration. But it's java, it not like you're gonna
		 * notice the speedup, hahaha. BTW: the cake is a lie, hahaha.
		 */
		entries.put(new Interval(from, to), find(clazz, from, to));
		return entries;
	}

	@Override
	public <T extends TransientAccountancyEntry> Map<Interval, Money> salesVolume(Class<T> clazz, DateTime from, DateTime to, Period period)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(from, "from must be not null");
		Objects.requireNonNull(to, "to must be not null");
		Objects.requireNonNull(period, "period must be not null");
		
		Money total;
		Map<Interval, Money> sales = new HashMap<Interval, Money>();
		Map<Interval, Iterable<T>> entries = find(clazz, from, to, period);

		for (Entry<Interval, Iterable<T>> e : entries.entrySet()) {
			total = Money.ZERO;
			for (T t : e.getValue()) {
				total = total.add(t.getValue());
			}
			sales.put(e.getKey(), total);
		}

		return sales;
	}

}
