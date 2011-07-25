package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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

    public AbstractCalendar(EntityManager em) {
        Objects.requireNonNull(em, "em");
        this.em = em;
    }
    
    @SuppressWarnings("boxing")
    @Override
    public Iterable<T> getEntries(Filter<T> filter) {
        
        Objects.requireNonNull(filter, "filter");

        TypedQuery<T> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce",
                        getContentClass());

        List<T> entries = new ArrayList<T>();
        
        for (T entry : q.getResultList()) {
            if (filter.invoke(entry) == true)
                entries.add(entry);
        }
        
        return entries;
    }

    @SuppressWarnings("boxing")
    @Override
    public T getEntryByID(long l) {
        return em.find(this.getContentClass(), l);
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
