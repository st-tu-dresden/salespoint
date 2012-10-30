package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;


/**
 * Transient implementation of the {@link Calendar} interface.
 * 
 * @author Paul Henke
 * @author Stanley Förster
 * 
 */
public class TransientCalendar implements Calendar<TransientCalendarEntry>
{
	private static final Map<CalendarEntryIdentifier, TransientCalendarEntry> entryMap = new ConcurrentHashMap<>();

	@Override
	public void add(TransientCalendarEntry entry)
	{
		Objects.requireNonNull(entry, "entry must not be null");
		entryMap.put(entry.getIdentifier(), entry);

	}

	@Override
	public boolean contains(CalendarEntryIdentifier calendarEntryIdentifier)
	{
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
		return entryMap.containsKey(calendarEntryIdentifier);
	}

	@Override
	public <E extends TransientCalendarEntry> E get(Class<E> clazz, CalendarEntryIdentifier calendarEntryIdentifier)
	{
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
		return clazz.cast(entryMap.get(calendarEntryIdentifier));
	}

	@Override
	public <E extends TransientCalendarEntry> Iterable<E> find(Class<E> clazz)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientCalendarEntry entry : entryMap.values()) 
		{
			if(clazz.isAssignableFrom(entry.getClass())) 
			{ 
				temp.add(clazz.cast(entry));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public boolean remove(CalendarEntryIdentifier calendarEntryIdentifier)
	{
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
		return entryMap.remove(calendarEntryIdentifier) != null;
	}

	@Override
	public <E extends TransientCalendarEntry> Iterable<E> find(Class<E> clazz, String title)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(title, "title must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientCalendarEntry entry : entryMap.values()) 
		{
			if(clazz.isAssignableFrom(entry.getClass()) && entry.getTitle().equals(title)) 
			{ 
				temp.add(clazz.cast(entry));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public <E extends TransientCalendarEntry> Iterable<E> find(Class<E> clazz, UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(userIdentifier, "title must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientCalendarEntry entry : entryMap.values()) 
		{
			if(clazz.isAssignableFrom(entry.getClass()) && entry.getOwner().equals(userIdentifier)) 
			{ 
				temp.add(clazz.cast(entry));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public <T extends TransientCalendarEntry> Iterable<T> between(Class<T> clazz, DateTime start, DateTime end)
	{
        Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"));

        Iterable<T> allEntries = find(clazz);
        List<T> result = new ArrayList<T>();

        for (T entry : allEntries) {
            for (Interval i : entry.getEntryList(interval)) {
                if (interval.contains(i)) {
                    result.add(entry);
                    break;
                }

            }
        }

        return result;
	}

	@Override
	public <T extends TransientCalendarEntry> Iterable<T> endsBetween(Class<T> clazz, DateTime start, DateTime end)
	{
        Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"));

        Iterable<T> allEntries = find(clazz);
        List<T> result = new ArrayList<T>();

        for (T entry : allEntries) {
            for (Interval i : entry.getEntryList(interval)) {
                if (interval.contains(i.getEnd())) {
                    result.add(entry);
                    break;
                }

            }
        }

        return result;
	}


	@Override
	public <T extends TransientCalendarEntry> Iterable<T> startsBetween(Class<T> clazz, DateTime start, DateTime end)
	{
        Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"));

        Iterable<T> allEntries = find(clazz);
        List<T> result = new ArrayList<T>();

        for (T entry : allEntries) {
            for (Interval i : entry.getEntryList(interval)) {
                if (interval.contains(i.getStart())) {
                    result.add(entry);
                    break;
                }

            }
        }

        return result;
	}


}
