package org.salespointframework.core.calendar;

import javax.persistence.EntityManager;

/**
 * 
 * @author stanley
 *
 * @param <T>
 */
public abstract class AbstractCalendar<T extends CalendarEntry> implements Calendar<T> {

    private EntityManager em;
    
    public AbstractCalendar(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public Iterable<T> getEntries() {
        // TODO getEntries (Auto-generated method stub)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("boxing")
    @Override
    public T getEntryByID(int id) {
        return em.find(this.getClassOfContent(), id);
    }
    
    @Override
    public void add(T entry) {
        em.persist(entry);
    }

    @Override
    public void delete(int calendarEntryIdentifier) {
        em.remove(this.getEntryByID(calendarEntryIdentifier));
    }
}
