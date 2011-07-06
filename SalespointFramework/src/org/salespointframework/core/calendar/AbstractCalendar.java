package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Filter;
import org.salespointframework.util.Objects;

/**
 * 
 * @author stanley
 *
 * @param <T>
 */
public abstract class AbstractCalendar<T extends CalendarEntry> implements Calendar<T>, ICanHasClass<T> {

    private EntityManager em;

    public static final CalendarEntryCapability CAP_OWNER = new CalendarEntryCapability("Owner");
    public static final CalendarEntryCapability CAP_SHARE = new CalendarEntryCapability("Share");
    public static final CalendarEntryCapability CAP_READ = new CalendarEntryCapability("Read");
    public static final CalendarEntryCapability CAP_CHANGE = new CalendarEntryCapability("Change");
    public static final CalendarEntryCapability CAP_REMOVE = new CalendarEntryCapability("Remove");
    
    public AbstractCalendar(EntityManager em) {
        Objects.requireNonNull(em, "em");
        this.em = em;
    }
    
    @Override
    public Iterable<T> getEntries(Filter<T> filter) {
        List<T> entries = new ArrayList<T>();
        
        for (T entry : entries) {
            if (filter.invoke(entry).equals(new Boolean(true)))
                entries.add(entry);
        }
        
        return entries;
    }

    @SuppressWarnings("boxing")
    @Override
    public T getEntryByID(int id) {
        return em.find(this.getContentClass(), id);
    }
    
    @Override
    public void addEntry(T entry) {
        Objects.requireNonNull(entry, "entry");
        em.persist(entry);
    }

    @Override
    public void deleteEntry(int calendarEntryIdentifier) {
        em.remove(this.getEntryByID(calendarEntryIdentifier));
    }
}
