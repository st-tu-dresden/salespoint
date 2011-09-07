package org.salespointframework.core.calendar;

import org.salespointframework.util.Filter;

/**
 * A calendar manages a set of calendar entries.
 *
 * @param <T> Type of calendar entries (extends {@link org.salespointframework.core.calendar.CalendarEntry})
 * 
 * @author Stanley Förster
 */
public interface Calendar<T extends CalendarEntry> {
    
    /**
     * Returns all entries that match the filter criteria.
     * 
     * @param filter The Filter object used to determine if an entry is part of the result or not 
     * 
     * @return Iterable<T> of all entries that match the filter criteria.
     * 
     * @see Filter
     */
    Iterable<T> getEntries(Filter<T> filter);
    
    /**
     * Returns the calendar entry which has the given id.
     * 
     * @param id Id of the requested entry.
     * @return the entry with the given id.
     */
    T getEntryByID(CalendarEntryIdentifier id);
    
    /**
     * Adds the given entry to the calendar.
     * @param entry the entry that is to be added to the calendar.
     */
    void addEntry(T entry);
    
    /**
     * Deletes the entry with the given id from the calendar.
     * @param id Id of the entry that is to be removed from the calendar.
     */
    void deleteEntry(CalendarEntryIdentifier id);
}
