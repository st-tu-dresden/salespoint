package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Filter;
import org.salespointframework.util.Objects;

/**
 * This is a basic implementation of the interface {@link Calendar}. To realize
 * your own calendar you just have to implement
 * {@link ICanHasClass#getContentClass}
 * 
 * @param <T>
 *            Type of calendar entries (extends
 *            {@link org.salespointframework.core.calendar.CalendarEntry})
 * 
 * @author stanley
 */
public abstract class AbstractCalendar<T extends CalendarEntry> implements Calendar<T>, ICanHasClass<T> {

    private EntityManager em;

    /**
     * Creates a new Calendar. An {@link EntityManager} is required for
     * persistence reasons.
     * 
     * @param em
     *            the entity manager which is used to persist the entries of
     *            this calendar
     */
    public AbstractCalendar(EntityManager em) {
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
    public Iterable<T> getEntries(Filter<T> filter) {

        Objects.requireNonNull(filter, "filter");

        TypedQuery<T> q = em.createQuery("SELECT ce FROM " + getContentClass().getSimpleName() + " ce", getContentClass());

        List<T> entries = new ArrayList<T>();

        for (T entry : q.getResultList()) {
            if (filter.invoke(entry))
                entries.add(entry);
        }

        return entries;
    }

    @SuppressWarnings("boxing")
    @Override
    public T getEntryByID(long l) {
        return em.find(this.getContentClass(), l);
    }

    /**
     * {@inheritDoc} <br />
     * The given entry must not be <code>null</code>
     */
    @Override
    public void addEntry(T entry) {
        Objects.requireNonNull(entry, "entry");
        em.persist(entry);
    }

    @Override
    public void deleteEntry(long calendarEntryIdentifier) {
        em.remove(this.getEntryByID(calendarEntryIdentifier));
    }
}