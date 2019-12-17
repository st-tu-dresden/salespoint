/*
 * Copyright 2017-2019 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.moduliths.test.ModuleTest.BootstrapMode;
import org.salespointframework.core.Currencies;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link AccountancyEntryRepository}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest(BootstrapMode.DIRECT_DEPENDENCIES)
class AccountancyRepositoryTests {

	@Autowired AccountancyEntryRepository repository;

	@Test // #182
	void findsEntriesWithinInterval() {

		AccountancyEntry entry = new AccountancyEntry(Money.of(10, Currencies.EURO));
		entry.setDate(LocalDateTime.now());

		entry = repository.save(entry);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime firstOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

		Interval firstOfMonthToNow = Interval.from(firstOfMonth).to(now);
		assertThat(repository.findByDateIn(firstOfMonthToNow)).contains(entry);

		Interval allMonth = Interval.from(firstOfMonth).to(firstOfMonth.plusMonths(1));
		assertThat(repository.findByDateIn(allMonth)).contains(entry);

		Interval nextMonth = Interval.from(firstOfMonth.plusMonths(1)).to(now.plusMonths(1));
		assertThat(repository.findByDateIn(nextMonth)).doesNotContain(entry);
	}
}
