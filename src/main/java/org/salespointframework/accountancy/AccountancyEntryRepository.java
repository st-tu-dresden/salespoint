package org.salespointframework.accountancy;

import java.time.LocalDateTime;

import org.salespointframework.core.SalespointRepository;

/**
 * Repository for {@link AccountancyEntry}s.
 * 
 * @author Oliver Gierke
 */
interface AccountancyEntryRepository<T extends AccountancyEntry> extends SalespointRepository<T, AccountancyEntryIdentifier> {

	/**
	 * Returns all {@link AccountancyEntry}s in the given time frame.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	Iterable<T> findByDateBetween(LocalDateTime from, LocalDateTime to);
}
