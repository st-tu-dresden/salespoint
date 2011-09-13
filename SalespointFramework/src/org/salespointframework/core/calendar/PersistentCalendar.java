package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.ProductType;
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
public final class PersistentCalendar implements Calendar<PersistentCalendarEntry> {

    /**
     * The entity manager that is used to persist the entries of this calendar.
     */
    private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

    /**
     * Adds the given entry to the calendar and inserts it into the database.
     * The given entry must not be <code>null</code>
     */
    @Override
    public void add(PersistentCalendarEntry entry) {
        Objects.requireNonNull(entry, "entry");
        
        EntityManager em = emf.createEntityManager();
        em.persist(entry);
        beginCommit(em);
    }

    /**
     * Removes the given entry from calendar and the database.
     */
    @Override
    public void remove(CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier");
		
		EntityManager em = emf.createEntityManager();
		Object calendarEntry = em.find(PersistentProductType.class, calendarEntryIdentifier);
		if(calendarEntry != null) {
			em.remove(calendarEntry);
			beginCommit(em);
		}
    }
    
    @Override
    public boolean contains(CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(ProductType.class, calendarEntryIdentifier) != null;
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
        
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        List<PersistentCalendarEntry> entries = new ArrayList<PersistentCalendarEntry>();

        for (PersistentCalendarEntry entry : tq.getResultList()) {
            if (filter.invoke(entry))
                entries.add(entry);
        }

        return Iterables.from(entries);
    }

    /**
     * Returns all entries for that the given user is the owner.
     * 
     * @param owner
     *            Useridentifier of the user whose entries should be found.
     * @return An {@iterable} with all found entries.
     */
    public Iterable<PersistentCalendarEntry> findByUserIdentifier(UserIdentifier userIdentifier) {
        Objects.requireNonNull(userIdentifier, "userIdentifier");
        
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pOwner = cb.equal(r.get(PersistentCalendarEntry_.owner), userIdentifier);

        q.where(pOwner);
        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that have the given title.
     * 
     * @param title
     *            The title that entries should have.
     * @return An iterable with all found entries
     */
    public Iterable<PersistentCalendarEntry> findByTitle(String title) {
        Objects.requireNonNull(title, "title");

        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersistentCalendarEntry> q = cb.createQuery(PersistentCalendarEntry.class);
        Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

        Predicate pEntry = cb.equal(r.get(PersistentCalendarEntry_.title), title);

        q.where(pEntry);

        TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

        return Iterables.from(tq.getResultList());
    }

    /**
     * Returns all entries that end at or between the given start and end date.
     * 
     * @param interval
     *            - The {@link Interval} in which entries should end.
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> findThatEndBetween(final Interval interval) {	// TODO ThatAndBetween ? Naming ??
        return getEntries(new Filter<PersistentCalendarEntry>() {

            @SuppressWarnings("boxing")
            @Override
            public Boolean invoke(PersistentCalendarEntry arg) {
                if (interval.contains(arg.getEnd()))
                    return true;

                long maxCount = arg.getCount();

                if (maxCount != 0) {
                    maxCount = maxCount == -1 ? Long.MAX_VALUE : maxCount;
                    DateTime nextEnd = arg.getEnd();

                    for (int i = 1; i < maxCount && !interval.isBefore(nextEnd); i++) {
                        nextEnd = nextEnd.plus(arg.getPeriod());
                        if (interval.contains(nextEnd))
                            return true;
                    }

                }

                return false;
            }
        });
    }

    /**
     * Returns all entries that start between the given start and end date.
     * 
     * @param interval
     *            - The {@link Interval} in which entries should start.
     * 
     * @return An iterable with all found entries.
     */
    public Iterable<PersistentCalendarEntry> findThatStartBetween(final Interval interval) {
        Objects.requireNonNull(interval, "interval");
        return getEntries(new Filter<PersistentCalendarEntry>() {

            @SuppressWarnings("boxing")
            @Override
            public Boolean invoke(PersistentCalendarEntry arg) {
                if (interval.contains(arg.getStart()))
                    return true;

                long maxCount = arg.getCount();

                if (maxCount != 0) {
                    maxCount = maxCount == -1 ? Long.MAX_VALUE : maxCount;
                    DateTime nextStart = arg.getStart();

                    for (int i = 1; i < maxCount && !interval.isBefore(nextStart); i++) {
                        nextStart = nextStart.plus(arg.getPeriod());
                        if (interval.contains(nextStart))
                            return true;
                    }

                }

                return false;
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see EntityManager#find(Class, Object)
     */
    @Override
    public PersistentCalendarEntry get(CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier");
        
        EntityManager em = emf.createEntityManager();
        return em.find(PersistentCalendarEntry.class, calendarEntryIdentifier);
    }
    
    private void beginCommit(EntityManager entityManager) {
    	entityManager.getTransaction().begin();
    	entityManager.getTransaction().commit();
    }
    
}