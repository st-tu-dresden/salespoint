/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.accountancy;

import java.time.LocalDateTime;

import org.salespointframework.accountancy.AccountancyEntry.AccountancyEntryIdentifier;
import org.salespointframework.core.SalespointRepository;
import org.salespointframework.time.Interval;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * Repository for {@link AccountancyEntry}s.
 *
 * @author Oliver Drotbohm
 */
interface AccountancyEntryRepository extends SalespointRepository<AccountancyEntry, AccountancyEntryIdentifier> {

	/**
	 * Finds all {@link AccountancyEntry} of the given type.
	 *
	 * @param <T> the actual {@link AccountancyEntry} type
	 * @param type must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@SuppressWarnings("unchecked")
	default <T extends AccountancyEntry> Streamable<T> findAll(Class<T> type) {

		Assert.notNull(type, "Type must not be null!");

		return AccountancyEntry.class.equals(type) ? (Streamable<T>) findAll() : findAllTyped(type);
	}

	/**
	 * Finds all {@link AccountancyEntry} of the given type created between the given start and end date.
	 *
	 * @param <T> the actual {@link AccountancyEntry} type
	 * @param from must not be {@literal null}.
	 * @param to must not be {@literal null}.
	 * @param type must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@Query("select ae from AccountancyEntry ae where ae.date > :from and ae.date < :to and type(ae) = :type")
	<T extends AccountancyEntry> Streamable<T> findByDateBetween(LocalDateTime from, LocalDateTime to,
			@Param("type") Class<T> type);

	/**
	 * Returns all {@link AccountancyEntry}s in the given time frame.
	 *
	 * @param from must not be {@literal null}.
	 * @param to must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@Query("select ae from AccountancyEntry ae where ae.date > :from and ae.date < :to")
	Streamable<AccountancyEntry> findByDateBetween(LocalDateTime from, LocalDateTime to);

	/**
	 * Returns all {@link AccountancyEntry}s within the given {@link Interval}.
	 *
	 * @param interval must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	default Streamable<AccountancyEntry> findByDateIn(Interval interval) {

		Assert.notNull(interval, "Interval must not be null!");

		return findByDateIn(interval, AccountancyEntry.class);
	}

	/**
	 * Finds all {@link AccountancyEntry}s of the given type within the given {@link Interval}.
	 *
	 * @param <T> the actual {@link AccountancyEntry} type
	 * @param interval must not be {@literal null}.
	 * @param type must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@SuppressWarnings("unchecked")
	default <T extends AccountancyEntry> Streamable<T> findByDateIn(Interval interval, Class<T> type) {

		Assert.notNull(interval, "Interval must not be null!");
		Assert.notNull(type, "Type must not be null!");

		return AccountancyEntry.class.equals(type)
				? (Streamable<T>) findByDateBetween(interval.getStart(), interval.getEnd())
				: findByDateBetween(interval.getStart(), interval.getEnd(), type);
	}

	/**
	 * Finds all {@link AccountancyEntry}s of the given type.
	 *
	 * @param <T> the actual {@link AccountancyEntry} type.
	 * @param type must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@Query("select ae from AccountancyEntry ae where type(ae) = :type")
	<T extends AccountancyEntry> Streamable<T> findAllTyped(@Param("type") Class<T> type);
}
