package org.salespointframework.accountancy;

import java.time.LocalDateTime;

import org.salespointframework.core.SalespointRepository;
import org.salespointframework.time.Interval;
import org.springframework.util.Assert;

/**
 * Repository for {@link AccountancyEntry}s.
 * 
 * @author Oliver Gierke
 */
interface AccountancyEntryRepository extends SalespointRepository<AccountancyEntry, AccountancyEntryIdentifier> {

	/**
	 * Returns all {@link AccountancyEntry}s in the given time frame.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	Iterable<AccountancyEntry> findByDateBetween(LocalDateTime from, LocalDateTime to);

	/**
	 * Returns all {@link AccountancyEntry}s within the given {@link Interval}.
	 * 
	 * @param interval must not be {@literal null}.
	 * @return
	 */
	default Iterable<AccountancyEntry> findByDateIn(Interval interval) {

		Assert.notNull(interval, "Interval must not be null!");

		return findByDateBetween(interval.getStart(), interval.getEnd());
	}
}
