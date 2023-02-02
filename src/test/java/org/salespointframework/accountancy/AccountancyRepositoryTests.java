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

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order.OrderIdentifier;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link AccountancyEntryRepository}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ApplicationModuleTest(BootstrapMode.DIRECT_DEPENDENCIES)
@RequiredArgsConstructor
class AccountancyRepositoryTests {

	private final AccountancyEntryRepository repository;
	private final EntityManager em;

	@Test // #182
	void findsEntriesWithinInterval() {

		var entry = repository.save(new AccountancyEntry(Money.of(10, Currencies.EURO)).setDate(LocalDateTime.now()));

		var now = LocalDateTime.now();
		var firstOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

		var firstOfMonthToNow = Interval.from(firstOfMonth).to(now);
		assertThat(repository.findByDateIn(firstOfMonthToNow)).contains(entry);

		var allMonth = Interval.from(firstOfMonth).to(firstOfMonth.plusMonths(1));
		assertThat(repository.findByDateIn(allMonth)).contains(entry);

		var nextMonth = Interval.from(firstOfMonth.plusMonths(1)).to(now.plusMonths(1));
		assertThat(repository.findByDateIn(nextMonth)).doesNotContain(entry);
	}

	@Test // #304
	@SuppressWarnings("deprecation")
	void rejectsInstanceCreatedViaDefaultConstructor() {

		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class) //
				.isThrownBy(() -> repository.save(new AccountancyEntry()));
	}

	@Test // GH-314
	void storesCustomAccountancyEntry() {

		var entry = repository.save(new CustomAccountancyEntry(Money.of(50, Currencies.EURO)));

		em.flush();
		em.clear();

		assertThat(repository.findById(entry.getId())).hasValue(entry);
	}

	@Test // GH-314
	void findsEntriesByConcreteType() {

		var orderId = OrderIdentifier.of("orderId");
		var accountId = UserAccountIdentifier.of("accountId");
		var money = Money.of(20, Currencies.EURO);

		var orderEntry = repository
				.save(new OrderPaymentEntry(orderId, accountId, money, "Some description.", Cash.CASH));
		var customEntry = repository.save(new CustomAccountancyEntry(money));

		assertThat(repository.findAll()).containsExactlyInAnyOrder(orderEntry, customEntry);
		assertThat(repository.findAll(OrderPaymentEntry.class)).containsExactly(orderEntry);
		assertThat(repository.findAll(CustomAccountancyEntry.class)).containsExactly(customEntry);
	}

	@Entity
	@NoArgsConstructor(force = true)
	static class CustomAccountancyEntry extends AccountancyEntry {

		CustomAccountancyEntry(MonetaryAmount amount) {
			super(amount);
		}
	}
}
