package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Filter;
import org.salespointframework.util.Objects;

/**
 * This is a basic implementation of the interface {@link Calendar}. To realize
 * your own calendar you just have to implement
 * {@link ICanHasClass#getContentClass}
 *
 * @author stanley
 */
public final class PersistentCalendar implements Calendar<PersistentCalendarEntry>, ICanHasClass<PersistentCalendarEntry> {

    private EntityManager em;

    /**
     * Creates a new Calendar. An {@link EntityManager} is required for
     * persistence reasons.
     * 
     * @param em
     *            the entity manager which is used to persist the entries of
     *            this calendar
     */
    public PersistentCalendar(EntityManager em) {
        Objects.requireNonNull(em, "em");
        this.em = em;
    }

    /**
     * Returns all Entries that match the filter criteria. If no entries were
     * found an empty {@link Iterable} will be returned, not <code>null</code>;
     * 
     * @param filter
     *            The filter will be invoked for each entry in this calendar.
     *            Every entry for which {@link Filter#invoke(Object)} returns
     *            <code>true</code> will be part of the result.
     * 
     * @return Iterable of all entries that match the filter criteria
     */
    @SuppressWarnings("boxing")
    @Override
    public Iterable<PersistentCalendarEntry> getEntries(Filter<PersistentCalendarEntry> filter) {

        Objects.requireNonNull(filter, "filter");

        TypedQuery<PersistentCalendarEntry> q = em.createQuery("SELECT ce FROM " + getContentClass().getSimpleName() + " ce", getContentClass());

        List<PersistentCalendarEntry> entries = new ArrayList<PersistentCalendarEntry>();

        for (PersistentCalendarEntry entry : q.getResultList()) {
            if (filter.invoke(entry))
                entries.add(entry);
        }

        return entries;
    }

    /**
     * Returns an {@link Iterable} that contains all entries that have the give title.
     * @param title The title that entries should have.
     * @return An iterable with all found entries
     */
    public Iterable<PersistentCalendarEntry> getEntriesByTitle(String title) {
        Objects.requireNonNull(title, "title");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.title == :title",
                        getContentClass());
        
        q.setParameter("title", title);

        return q.getResultList();
    }
    
    public Iterable<PersistentCalendarEntry> getEntriesThatStartAfter(DateTime start) {
        Objects.requireNonNull(start, "from");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.start >= :start",
                        getContentClass());
        
        q.setParameter("start", start);

        return q.getResultList();
    }

    public Iterable<PersistentCalendarEntry> getEntriesThatStartBefore(DateTime start) {
        Objects.requireNonNull(start, "from");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.start <= :start",
                        getContentClass());
        
        q.setParameter("start", start);

        return q.getResultList();
    }
    
    public Iterable<PersistentCalendarEntry> getEntriesThatEndBefore(DateTime end) {
        Objects.requireNonNull(end, "end");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.end <= :end",
                        getContentClass());
        
        q.setParameter("end", end);

        return q.getResultList();
    }

    public Iterable<PersistentCalendarEntry> getEntriesThatEndAfter(DateTime end) {
        Objects.requireNonNull(end, "end");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.end >= :end",
                        getContentClass());
        
        q.setParameter("end", end);

        return q.getResultList();
    }
    
    public Iterable<PersistentCalendarEntry> getEntriesThatStartBetween(DateTime start, DateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.start BETWEEN :start and :end",
                        getContentClass());
        q.setParameter("start", start);
        q.setParameter("end", end);

        return q.getResultList();
    }

    public Iterable<PersistentCalendarEntry> getEntriesThatEndBetween(DateTime start, DateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        
        TypedQuery<PersistentCalendarEntry> q = em
                .createQuery(
                        "SELECT ce FROM "+getContentClass().getSimpleName()+" ce WHERE ce.end BETWEEN :start and :end",
                        getContentClass());
        q.setParameter("start", start);
        q.setParameter("end", end);

        return q.getResultList();
    }    
    
    public Iterable<PersistentCalendarEntry> getEntriesByOwner(final String owner) {
        return getEntries(new Filter<PersistentCalendarEntry>() {
            @SuppressWarnings("boxing")
            @Override
            public Boolean invoke(PersistentCalendarEntry arg) {
                return arg.getOwner().equals(owner);
            }
        });
    }
    
    @Override
    public PersistentCalendarEntry getEntryByID(CalendarEntryIdentifier l) {
        return em.find(this.getContentClass(), l);
    }

    /**
     * {@inheritDoc} <br />
     * The given entry must not be <code>null</code>
     */
    @Override
    public void addEntry(PersistentCalendarEntry entry) {
        Objects.requireNonNull(entry, "entry");
        em.persist(entry);
    }

    @Override
    public void deleteEntry(CalendarEntryIdentifier calendarEntryIdentifier) {
        em.remove(this.getEntryByID(calendarEntryIdentifier));
    }

    @Override
    public Class<PersistentCalendarEntry> getContentClass() {
        return PersistentCalendarEntry.class;
    }
}