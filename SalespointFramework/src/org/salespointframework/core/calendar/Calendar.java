package org.salespointframework.core.calendar;

import org.salespointframework.util.Filter;

/**
 * 
 * Calendar manages a set of calendar entries.
 * 
 * @author stanley
 *
 * @param <T> Type of calendar entries (extends {@link org.salespointframework.core.calendar.CalendarEntry})
 */
public interface Calendar<T extends CalendarEntry> {
    
    /**
     * Returns all Entries that match the filter criteria.
     * If no entries were found an empty {@link Iterable} will be returned, not null;
     * 
     * 
     * @return Iterable of all entries that match the filter criteria
     * 
     * @see {@link Filter}
     */
    Iterable<T> getEntries(Filter<T> filter);
    
    /**
     * Returns the calendar entry which has the given id or null if no entry exists with this id.
     * @param id of the entry that should be returned
     * @return the entry with the given id or null
     */
    T getEntryByID(long id);
    
    /**
     * Adds the given entry to the calendar.
     * @param entry the entry that should be added to the calendar
     */
    void addEntry(T entry);
    
    /**
     * Deletes the entry with the given id.
     * @param id of the entry that should be removed from calendar
     */
    void deleteEntry(int id);
}
