package org.salespointframework.core.calendar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class TransientCalendar implements Calendar<TransientCalendarEntry>
{
	private Map<CalendarEntryIdentifier, TransientCalendarEntry> entryMap = new ConcurrentHashMap<>();

	@Override
	public void add(TransientCalendarEntry entry)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(CalendarEntryIdentifier calendarEntryIdentifier)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <E extends TransientCalendarEntry> E get(Class<E> clazz, CalendarEntryIdentifier calendarEntryIdentifier)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends TransientCalendarEntry> Iterable<E> find(Class<E> clazz)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(CalendarEntryIdentifier calendarEntryIdentifier)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
