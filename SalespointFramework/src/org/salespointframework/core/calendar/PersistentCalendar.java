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
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;
import java.util.Objects;

/**
 * This is an implementation of the interface {@link Calendar} that provides
 * basic functionality like add/remove calendar entries. Using this
 * implementation, it is possible to persist all information to a database.
 * 
 * 
 * @author Stanley Förster
 */
public class PersistentCalendar implements Calendar<PersistentCalendarEntry> {

    private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

    @Override
    public void add(PersistentCalendarEntry entry) {
        Objects.requireNonNull(entry, "entry must not be null");

        EntityManager em = emf.createEntityManager();
        em.persist(entry);
        beginCommit(em);
    }

    @Override
    public boolean contains(CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
        EntityManager em = emf.createEntityManager();
        return em.find(PersistentCalendarEntry.class, calendarEntryIdentifier) != null;
    }

    @Override
    public <T extends PersistentCalendarEntry> T get(Class<T> clazz, CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");

        EntityManager em = emf.createEntityManager();
        return em.find(clazz, calendarEntryIdentifier);
    }

    /**
     * Returns all entries that have the given title.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param title
     *            The title that entries should have.
     * @return An iterable with all found entries
     */
    public <T extends PersistentCalendarEntry> Iterable<T> find(Class<T> clazz, String title) {
        Objects.requireNonNull(title, "title must not be null");

        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(clazz);
        Root<T> r = q.from(clazz);

        Predicate pEntry = cb.like(r.get(PersistentCalendarEntry_.title), title);

        q.where(pEntry);

        TypedQuery<T> tq = em.createQuery(q);

        return Iterables.of(tq.getResultList());
    }

    /**
     * Updates and persists an existing {@link PersistentCalendarEntry} to this
     * calendar and the database.
     * 
     * @param entry
     *            the <code>PersistentCalendarEntry</code> to be updated
     * @throws NullPointerException
     *             if <code>entry</code> is <code>null</code>
     */
    public void update(PersistentCalendarEntry entry) {
        Objects.requireNonNull(entry, "entry must not be null");

        EntityManager em = emf.createEntityManager();
        em.merge(entry);
        beginCommit(em);
    }

    /**
     * Returns all entries for that the given user is the owner.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param userIdentifier
     *            {@link UserIdentifier} of the user whose entries should be
     *            found.
     * @return An {link @Iterable} with all found entries.
     */
    public <T extends PersistentCalendarEntry> Iterable<T> find(Class<T> clazz, UserIdentifier userIdentifier) {
        Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");

        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(clazz);
        Root<T> r = q.from(clazz);

        Predicate pOwner = cb.equal(r.get(PersistentCalendarEntry_.owner), userIdentifier);

        q.where(pOwner);
        TypedQuery<T> tq = em.createQuery(q);

        return Iterables.of(tq.getResultList());
    }

    /**
     * Returns all entries that start and end between the given start and end date.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param start
     *            The start of the interval in which returned entries should
     *            have their start and end date.
     * 
     * @param end
     *            The end of the interval in which returned entries should have
     *            their start and end date. The end is exclusive.
     * 
     * 
     * @return An iterable with all found entries.
     * 
     * @throws NullPointerException
     *             if <code>start</code> or <code>end</code> or both are <code>null</code>
     * 
     * @see Interval#contains(org.joda.time.ReadableInterval)
     */
    public <T extends PersistentCalendarEntry> Iterable<T> between(Class<T> clazz, DateTime start, DateTime end) {
        Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"));

        Iterable<T> allEntries = find(clazz);
        List<T> result = new ArrayList<T>();

        for (T entry : allEntries) {
            for (Interval i : entry.getEntryList(interval)) {
                if (interval.contains(i)) {
                    result.add(entry);
                    break;
                }

            }
        }

        return result;
    }

    /**
     * Returns all entries that end between the given start and end date.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param start
     *            The start of the interval in which returned entries should
     *            have their end date.
     * 
     * @param end
     *            The end of the interval in which returned entries should have
     *            their end date. The end is exclusive.
     * 
     * 
     * @return An iterable with all found entries.
     * 
     * @throws NullPointerException
     *             if <code>start</code> or <code>end</code> or both are <code>null</code>
     * 
     * @see Interval#contains(org.joda.time.ReadableInstant)
     */
    public <T extends PersistentCalendarEntry> Iterable<T> endsBetween(Class<T> clazz, DateTime start, DateTime end) {
        Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"));

        Iterable<T> allEntries = find(clazz);
        List<T> result = new ArrayList<T>();

        for (T entry : allEntries) {
            for (Interval i : entry.getEntryList(interval)) {
                if (interval.contains(i.getEnd())) {
                    result.add(entry);
                    break;
                }

            }
        }

        return result;
    }

    /**
     * Returns all entries that start between the given start and end date.
     * 
     * @param <T>
     *            common super type of all entries returned
     * 
     * @param clazz
     *            Class object corresponding to the type of the entries to be
     *            returned, has to be <code>PersistentCalendarEntry</code> or a
     *            sub-class of it.
     * 
     * @param start
     *            The start of the interval in which returned entries should
     *            have their start date.
     * 
     * @param end
     *            The end of the interval in which returned entries should have
     *            their start date. The end is exclusive.
     * 
     * 
     * @return An iterable with all found entries.
     * 
     * @throws NullPointerException
     *             if <code>start</code> or <code>end</code> or both are <code>null</code>
     * 
     * @see Interval#contains(org.joda.time.ReadableInstant)
     */
    public <T extends PersistentCalendarEntry> Iterable<T> startsBetween(Class<T> clazz, DateTime start, DateTime end) {
        Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(end, "end must not be null"));

        Iterable<T> allEntries = find(clazz);
        List<T> result = new ArrayList<T>();

        for (T entry : allEntries) {
            for (Interval i : entry.getEntryList(interval)) {
                if (interval.contains(i.getStart())) {
                    result.add(entry);
                    break;
                }

            }
        }

        return result;
    }

    @Override
    public <T extends PersistentCalendarEntry> Iterable<T> find(Class<T> clazz) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(clazz);
        TypedQuery<T> tq = em.createQuery(q);

        return Iterables.of(tq.getResultList());
    }

    @Override
    public boolean remove(CalendarEntryIdentifier calendarEntryIdentifier) {
        Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
        try {
            EntityManager em = emf.createEntityManager();
            Object calendarEntry = em.find(PersistentCalendarEntry.class, calendarEntryIdentifier);
            if (calendarEntry != null) {
                em.remove(calendarEntry);
                beginCommit(em);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void beginCommit(EntityManager entityManager) {
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
    }

}