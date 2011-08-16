package org.salespointframework.core.calendar;

import org.salespointframework.util.Filter;

/**
 * 
 * A calendar manages a set of calendar entries.
 *
 * @param <T> Type of calendar entries (extends {@link org.salespointframework.core.calendar.CalendarEntry})
 * 
 * @author Stanley Foerster
 */
public interface Calendar<T extends CalendarEntry> {
    
    /**
     * Should return all Entries that match the filter criteria.
     * 
     * @param filter The filter that defines the filter criteria. 
     * 
     * @return Iterable of all entries that match the filter criteria.
     * 
     * @see Filter
     */
    Iterable<T> getEntries(Filter<T> filter);
    
    /**
     * Should return the calendar entry which has the given id or <code>null</code> if no entry exists with this id.
     * 
     * @param id of the entry that should be returned.
     * @return the entry with the given id or <code>null</code>.
     */
    T getEntryByID(CalendarEntryIdentifier id);
    
    /**
     * Should add the given entry to the calendar.
     * @param entry the entry that should be added to the calendar.
     */
    void addEntry(T entry);
    
    /**
     * Should delete the entry with the given id from the calendar.
     * @param id of the entry that should be removed from calendar.
     */
    void deleteEntry(CalendarEntryIdentifier id);
}
