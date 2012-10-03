package org.salespointframework.core.accountancy;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.money.Money;

public class TransientAccountancy implements Accountancy<TransientAccountancyEntry>
{
	private Map<AccountancyEntryIdentifier, TransientAccountancyEntry> entryMap = new ConcurrentHashMap<>();

	@Override
	public void add(TransientAccountancyEntry accountancyEntry)
	{
		Objects.requireNonNull(accountancyEntry, "accountancyEntry must not be null");
		entryMap.put(accountancyEntry.getIdentifier(), accountancyEntry);
	}

	@Override
	public <E extends TransientAccountancyEntry> Iterable<E> find(Class<E> clazz)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TransientAccountancyEntry> TransientAccountancyEntry get(Class<E> clazz, AccountancyEntryIdentifier accountancyEntryIdentifier)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TransientAccountancyEntry> Iterable<E> find(Class<E> clazz, DateTime from, DateTime to)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TransientAccountancyEntry> Map<Interval, Iterable<E>> find(Class<E> clazz, DateTime from, DateTime to, Period period)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TransientAccountancyEntry> Map<Interval, Money> salesVolume(Class<E> clazz, DateTime from, DateTime to, Period period)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
