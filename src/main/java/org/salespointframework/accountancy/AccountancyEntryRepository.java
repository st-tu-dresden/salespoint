/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.accountancy;

import java.time.LocalDateTime;

import org.salespointframework.time.Interval;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * Repository for {@link AccountancyEntry}s.
 * 
 * @author Oliver Gierke
 */
interface AccountancyEntryRepository extends CrudRepository<AccountancyEntry, AccountancyEntryIdentifier> {

	/**
	 * Returns all {@link AccountancyEntry}s in the given time frame.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	Streamable<AccountancyEntry> findByDateBetween(LocalDateTime from, LocalDateTime to);

	/**
	 * Returns all {@link AccountancyEntry}s within the given {@link Interval}.
	 * 
	 * @param interval must not be {@literal null}.
	 * @return
	 */
	default Streamable<AccountancyEntry> findByDateIn(Interval interval) {

		Assert.notNull(interval, "Interval must not be null!");

		return findByDateBetween(interval.getStart(), interval.getEnd());
	}
}
