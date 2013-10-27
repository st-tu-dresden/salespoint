package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.util.Iterables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is an implementation of the interface {@link Calendar} that provides basic functionality like add/remove
 * calendar entries. Using this implementation, it is possible to persist all information to a database.
 * 
 * @author Stanley Föerster
 * @author Oliver Gierke
 */
@Service
@Transactional
class PersistentCalendar implements Calendar {

	@PersistenceContext private EntityManager em;

	@Override
	public void add(CalendarEntry entry) {
		Objects.requireNonNull(entry, "entry must not be null");
		em.persist(entry);
	}

	@Override
	public boolean contains(CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
		return em.find(CalendarEntry.class, calendarEntryIdentifier) != null;
	}

	@Override
	public <T extends CalendarEntry> T get(Class<T> clazz, CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");
		return em.find(clazz, calendarEntryIdentifier);
	}

	@Override
	public <T extends CalendarEntry> Iterable<T> find(Class<T> clazz, String title) {
		Objects.requireNonNull(title, "title must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);

		Predicate pEntry = cb.like(r.get(CalendarEntry_.title), title);

		q.where(pEntry);

		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.of(tq.getResultList());
	}

	/**
	 * Updates and persists an existing {@link CalendarEntry} to this calendar and the database.
	 * 
	 * @param entry the <code>CalendarEntry</code> to be updated
	 * @throws NullPointerException if <code>entry</code> is <code>null</code>
	 */
	public void update(CalendarEntry entry) {

		Objects.requireNonNull(entry, "entry must not be null");
		em.merge(entry);
	}

	@Override
	public <T extends CalendarEntry> Iterable<T> find(Class<T> clazz, UserAccountIdentifier userIdentifier) {
		Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);

		Predicate pOwner = cb.equal(r.get(CalendarEntry_.owner), userIdentifier);

		q.where(pOwner);
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public <T extends CalendarEntry> Iterable<T> between(Class<T> clazz, DateTime start, DateTime end) {
		Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(
				end, "end must not be null"));

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

	@Override
	public <T extends CalendarEntry> Iterable<T> endsBetween(Class<T> clazz, DateTime start, DateTime end) {
		Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(
				end, "end must not be null"));

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

	@Override
	public <T extends CalendarEntry> Iterable<T> startsBetween(Class<T> clazz, DateTime start, DateTime end) {
		Interval interval = new Interval(Objects.requireNonNull(start, "start must not be null"), Objects.requireNonNull(
				end, "end must not be null"));

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
	public <T extends CalendarEntry> Iterable<T> find(Class<T> clazz) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		TypedQuery<T> tq = em.createQuery(q);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public boolean remove(CalendarEntryIdentifier calendarEntryIdentifier) {
		Objects.requireNonNull(calendarEntryIdentifier, "calendarEntryIdentifier must not be null");

		Object calendarEntry = em.find(CalendarEntry.class, calendarEntryIdentifier);
		if (calendarEntry != null) {
			em.remove(calendarEntry);
		}

		return true;
	}
}
