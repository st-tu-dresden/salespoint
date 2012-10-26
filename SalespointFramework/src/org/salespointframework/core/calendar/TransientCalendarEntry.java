package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;

/**
 * 
 * @author Paul Henke
 * 
 */
public class TransientCalendarEntry implements CalendarEntry
{
	private final CalendarEntryIdentifier calendarEntryIdentifier = new CalendarEntryIdentifier();

	private int repeatCount;

	private String description;

	private Interval duration;

	private DateTime start;

	private DateTime end;

	private UserIdentifier owner;

	private Period period;

	private String title;
	
    public TransientCalendarEntry(UserIdentifier owner, String title, DateTime start, DateTime end) {
        this(owner, title, start, end, "", Period.ZERO, 0);
    }
    
    public TransientCalendarEntry(UserIdentifier owner, String title, DateTime start, DateTime end, String description) {
        this(owner, title, start, end, description, Period.ZERO, 0);
    }
    
    public TransientCalendarEntry(UserIdentifier owner, String title, DateTime start, DateTime end, String description, Period period, int count) {

        detectDateAnomalies(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"), count, Objects.requireNonNull(period, "period must not be null"));

        this.owner = Objects.requireNonNull(owner, "owner must not be null must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");

        this.repeatCount = count;
        this.period = period;

        this.start = start;
        this.end = end;
        
        duration = new Interval(start, end);
    }

	@Override
	public final CalendarEntryIdentifier getIdentifier()
	{
		return calendarEntryIdentifier;
	}

	@Override
	public final String getDescription()
	{
		return description;
	}

	@Override
	public final DateTime getEnd()
	{
		return end;
	}

	@Override
	public final UserIdentifier getOwner()
	{
		return owner;
	}

	@Override
	public final DateTime getStart()
	{
		return start;
	}

	@Override
	public final String getTitle()
	{
		return title;
	}

	@Override
	public void setDescription(String description)
	{
		this.description = Objects.requireNonNull(description, "description must not be null");

	}

	@Override
	public void setEnd(DateTime end)
	{
		this.end = Objects.requireNonNull(end, "end must not be null");

	}

	@Override
	public void setStart(DateTime start)
	{
		this.start = Objects.requireNonNull(start, "start must not be null");

	}

	@Override
	public void setTitle(String title)
	{
		this.title = Objects.requireNonNull(title, "title must not be null");
	}
	
    @Override
    public final int hashCode() {
        return calendarEntryIdentifier.hashCode();
    }
	
    private final void detectDateAnomalies(DateTime start, DateTime end, int repeatCount, Period period) {
        Interval duration = new Interval(start, end);
        
        if (repeatCount < -1)
            throw new IllegalArgumentException("The repeat count of an appointment can't be less than zero, except -1, but was " + repeatCount);

        if ((repeatCount != 0) && duration.getStart().plus(period).isBefore(duration.getEnd()))
            throw new IllegalArgumentException("The time between two repetitions can not be less than the duration of a calendar entry!");
    }

    @Override
    public final boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (other instanceof TransientCalendarEntry)
            return this.calendarEntryIdentifier.equals(((TransientCalendarEntry)other).calendarEntryIdentifier);
        return false;
    }
    
    /**
     * Returns a string representation of this entry. The format of this string
     * is:
     * <p>
     * <code>title + " (" + getStart() + " - " + getEnd() + ")"</code>
     * </p>
     * 
     * @return The string representation of this entry.
     */
    @Override
    public String toString() {
        return title + " (" + getStart() + " - " + getEnd() + ")";
    }
    
    public final Iterable<Interval> getEntryList(Interval interval) {
        List<Interval> dates = new ArrayList<Interval>();

        int maxCount = this.repeatCount;

        maxCount = maxCount == -1 ? Integer.MAX_VALUE : maxCount;

        Interval nextInterval = this.duration;

        for (int i = 0; i <= maxCount && !interval.isBefore(nextInterval); i++) {
            if (interval.overlaps(nextInterval)) {
                dates.add(nextInterval);
            }
            nextInterval = new Interval(nextInterval.getStart().plus(this.period), nextInterval.getEnd().plus(this.period));
        }

        return Iterables.of(dates);
    }

}
