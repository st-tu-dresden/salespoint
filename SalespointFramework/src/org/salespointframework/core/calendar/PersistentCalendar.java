package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Filter;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * This is an implementation of the interface {@link Calendar} that provides
 * basic functionality like add/remove calendar entries and filter them
 * according to different criterias.
 * 
 * @author Stanley Förster
 */
public final class PersistentCalendar implements Calendar<PersistentCalendarEntry>, ICanHasClass<PersistentCalendarEntry> {

    /**
     * The entity manager that is used to persist the entries of this calendar.
     */
    private final EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();

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

        return Iterables.from(entries);
    }

    /**
     * Returns all entries that have the given title.
     * 
     * @param title
     *            The title that entries should have.
     * @return An iterable with all found entries
     */
    public Iterable<PersistentCalendarEntry> getEntriesByTitle(String title) {
        Objects.requireNonNull(title, "title");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pEntry = cb.equal(r.get(PersistentCalendarEntry_.title), title);

        q.where(pEntry);

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that start at or after the given date.
     * 
     * @param start
     *            The date at which entries should start at the earliest.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesThatStartAfter(DateTime start) {
        Objects.requireNonNull(start, "from");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pEntry = cb.greaterThanOrEqualTo(r.get(PersistentCalendarEntry_.startTime), start.toDate());

        q.where(pEntry);

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that start before or at the given date.
     * 
     * @param start
     *            The date at which entries should start at the latest.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesThatStartBefore(DateTime start) {
        Objects.requireNonNull(start, "from");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pEntry = cb.lessThanOrEqualTo(r.get(PersistentCalendarEntry_.startTime), start.toDate());

        q.where(pEntry);

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that end before or at the given date.
     * 
     * @param end
     *            The date at which entries should end at the latest.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesThatEndBefore(DateTime end) {
        Objects.requireNonNull(end, "end");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pEntry = cb.lessThanOrEqualTo(r.get(PersistentCalendarEntry_.endTime), end.toDate());

        q.where(pEntry);

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that end at or after the given date.
     * 
     * @param end
     *            The date at which entries should end at the earliest.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesThatEndAfter(DateTime end) {
        Objects.requireNonNull(end, "end");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pEntry = cb.greaterThanOrEqualTo(r.get(PersistentCalendarEntry_.endTime), end.toDate());

        q.where(pEntry);

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that start at or between the given start and end
     * date.
     * 
     * @param start
     *            The date at which entries should start at the earliest.
     * @param end
     *            The date at which entries should start at the latest.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesThatStartBetween(DateTime start, DateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pStart = cb.greaterThanOrEqualTo(r.get(PersistentCalendarEntry_.startTime), start.toDate());
        Predicate pEnd = cb.lessThanOrEqualTo(r.get(PersistentCalendarEntry_.startTime), end.toDate());

        q.where(cb.and(pStart, pEnd));

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);
        
        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that end at or between the given start and end
     * date.
     * 
     * @param start
     *            The date at which entries should end at the earliest.
     * @param end
     *            The date at which entries should end at the latest.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesThatEndBetween(DateTime start, DateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pStart = cb.greaterThanOrEqualTo(r.get(PersistentCalendarEntry_.endTime), start.toDate());
        Predicate pEnd = cb.lessThanOrEqualTo(r.get(PersistentCalendarEntry_.endTime), end.toDate());

        q.where(cb.and(pStart, pEnd));

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);
        
        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries for that the given user is the owner.
     * @param owner Useridentifier of the user whose entries should be found.
     * @return An {@iterable} with all found entries.
     */
    public Iterable<PersistentCalendarEntry> getEntriesByOwner(final UserIdentifier owner) {
        return getEntries(new Filter<PersistentCalendarEntry>() {
            @Override
            public Boolean invoke(PersistentCalendarEntry arg) {
                return new Boolean(arg.getOwner().equals(owner));
            }
        });
    }

    /**
     * Returns the entry which has the given identification.
     */
    @Override
    public PersistentCalendarEntry getEntryByID(CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier");

        return em.find(this.getContentClass(), calendarEntryIdentifier);
    }

    /**
     * Adds the given entry to the calendar and inserts it into the database.
     * The given entry must not be <code>null</code>
     */
    @Override
    public void addEntry(PersistentCalendarEntry entry) {
        Objects.requireNonNull(entry, "entry");
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.persist(entry);
        t.commit();
    }

    /**
     * Removes the given entry from calendar and the database.
     */
    @Override
    public void deleteEntry(CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier");
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.remove(this.getEntryByID(calendarEntryIdentifier));
        t.commit();
    }

    @Override
    public Class<PersistentCalendarEntry> getContentClass() {
        return PersistentCalendarEntry.class;
    }
}