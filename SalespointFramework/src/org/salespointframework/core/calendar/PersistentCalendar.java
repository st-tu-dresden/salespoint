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
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * This is an implementation of the interface {@link Calendar} that provides
 * basic functionality like add/remove calendar entries. Using this
 * implementation, it is possible to persist all information to a database.
 * 
 * 
 * @author Stanley Förster
 */
public final class PersistentCalendar implements
		Calendar<PersistentCalendarEntry> {

	private final EntityManagerFactory emf = Database.INSTANCE
			.getEntityManagerFactory();

	@Override
	public void add(PersistentCalendarEntry entry) {
		Objects.requireNonNull(entry, "entry");

		EntityManager em = emf.createEntityManager();
		em.persist(entry);
		beginCommit(em);
	}

	@Override
	public boolean contains(CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier,
				"calendarEntryIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(PersistentCalendarEntry.class, calendarEntryIdentifier) != null;
	}

	@Override
	public <T extends PersistentCalendarEntry> T get(Class<T> clazz,
			CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier,
				"calendarEntryIdentifier");

		EntityManager em = emf.createEntityManager();
		return em.find(clazz, calendarEntryIdentifier);
	}

	/**
	 * Returns all entries that have the given title.
	 * 
	 * @param title
	 *            The title that entries should have.
	 * @return An iterable with all found entries
	 */
	//TODO add Class<T> parameter, plz, bitch.
	public Iterable<PersistentCalendarEntry> findByTitle(String title) {
		Objects.requireNonNull(title, "title");

		EntityManager em = emf.createEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersistentCalendarEntry> q = cb
				.createQuery(PersistentCalendarEntry.class);
		Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

		//TODO use SQL-Like instead equal
		//cb.like(arg0, arg1)
		Predicate pEntry = cb.equal(r.get(PersistentCalendarEntry_.title),
				title);

		q.where(pEntry);

		TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * Returns all entries for that the given user is the owner.
	 * 
	 * @param userIdentifier
	 *            {@link UserIdentifier} of the user whose entries should be
	 *            found.
	 * @return An {@Iterable} with all found entries.
	 */
	public Iterable<PersistentCalendarEntry> find(
			UserIdentifier userIdentifier) {
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersistentCalendarEntry> q = cb
				.createQuery(PersistentCalendarEntry.class);
		Root<PersistentCalendarEntry> r = q.from(PersistentCalendarEntry.class);

		Predicate pOwner = cb.equal(r.get(PersistentCalendarEntry_.owner),
				userIdentifier);

		q.where(pOwner);
		TypedQuery<PersistentCalendarEntry> tq = em.createQuery(q);

		return Iterables.from(tq.getResultList());
	}

	/**
	 * Returns all entries that end at or between the given start and end date.
	 * 
	 * @param interval
	 *            - The {@link Interval} in which entries should end.
	 * @return An iterable with all found entries.
	 * @throws ArgumentNullException
	 *             if <code>interval</code> is <code>null</code>
	 */
	// TODO use 2 joda-dates instead of interval
	//TODO between, startsBetween, endsBetween
	public Iterable<PersistentCalendarEntry> findThatEndBetween(
			final Interval interval) { // TODO
										// ThatAndBetween
										// ?
										// Naming
										// ??
		Objects.requireNonNull(interval, "interval");
		Iterable<PersistentCalendarEntry> allEntries = find(PersistentCalendarEntry.class);
		List<PersistentCalendarEntry> result = new ArrayList<PersistentCalendarEntry>();

		for (PersistentCalendarEntry entry : allEntries) {
			if (interval.contains(entry.getEnd())) {
				result.add(entry);
				continue;
			}

			long maxCount = entry.getCount();

			if (maxCount != 0) {
				maxCount = maxCount == -1 ? Long.MAX_VALUE : maxCount;
				DateTime nextEnd = entry.getEnd();

				for (int i = 1; i < maxCount && !interval.isBefore(nextEnd); i++) {
					nextEnd = nextEnd.plus(entry.getPeriod());
					if (interval.contains(nextEnd)) {
						result.add(entry);
						continue;
					}
				}

			}
		}

		return Iterables.from(result);
	}

	/**
	 * Returns all entries that start between the given start and end date.
	 * 
	 * @param interval
	 *            - The {@link Interval} in which entries should start.
	 * @return An iterable with all found entries.
	 * @throws ArgumentNullException
	 *             if <code>interval</code> is <code>null</code>
	 */
	@SuppressWarnings("null")
	public Iterable<PersistentCalendarEntry> findThatStartBetween(
			final Interval interval) {
		Objects.requireNonNull(interval, "interval");

		Iterable<PersistentCalendarEntry> allEntries = null;// = find();
		List<PersistentCalendarEntry> result = new ArrayList<PersistentCalendarEntry>();

		for (PersistentCalendarEntry entry : allEntries) {
			if (interval.contains(entry.getStart())) {
				result.add(entry);
				continue;
			}

			long maxCount = entry.getCount();

			if (maxCount != 0) {
				maxCount = maxCount == -1 ? Long.MAX_VALUE : maxCount;
				DateTime nextStart = entry.getStart();

				for (int i = 1; i < maxCount && !interval.isBefore(nextStart); i++) {
					nextStart = nextStart.plus(entry.getPeriod());
					if (interval.contains(nextStart)) {
						result.add(entry);
						continue;
					}
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

		return Iterables.from(tq.getResultList());
	}

	@Override
	public boolean remove(CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier,
				"calendarEntryIdentifier");
		try {
			EntityManager em = emf.createEntityManager();
			Object calendarEntry = em.find(PersistentCalendarEntry.class,
					calendarEntryIdentifier);
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