/*
 * Copyright 2023 the original author or authors.
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
import lombok.RequiredArgsConstructor;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.accountancy.AccountancyEventPublicationTests.CustomAccountancyEntryListener;
import org.salespointframework.accountancy.AccountancyEvents.AccountancyEntryCreated;
import org.salespointframework.core.Currencies;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.AssertablePublishedEvents;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests related to event publication within the accountancy.
 *
 * @author Oliver Drotbohm
 */
@Transactional
@ApplicationModuleTest(extraIncludes = "org.salespointframework.time")
@RequiredArgsConstructor
@DirtiesContext
@Import(CustomAccountancyEntryListener.class)
class AccountancyEventPublicationTests {

	private final Accountancy accountancy;
	private final CustomAccountancyEntryListener listener;

	@AfterEach
	void afterEach() {
		listener.event = null;
	}

	@Test // GH-449
	void publishesEventForCreation(AssertablePublishedEvents events) {

		var entry = accountancy.add(new AccountancyEntry(Money.of(10, Currencies.EURO)));

		assertThat(events).contains(AccountancyEntryCreated.class)
				.matching(it -> it.getEntry().equals(entry));

		assertThat(listener.event).isNull();
	}

	@Test // GH-449
	void listensToCustomEventsByGenericsDeclaration() {

		var entry = accountancy.add(new CustomAccountancyEntry(Money.of(10, Currencies.EURO)));

		assertThat(listener.event).isNotNull()
				.satisfies(it -> {
					assertThat(it.getEntry()).isEqualTo(entry);
				});
	}

	@Component
	static class CustomAccountancyEntryListener {

		AccountancyEntryCreated<? extends AccountancyEntry> event;

		@EventListener
		void on(AccountancyEntryCreated<CustomAccountancyEntry> event) {
			this.event = event;
		}
	}

	@Entity
	static class CustomAccountancyEntry extends AccountancyEntry {

		public CustomAccountancyEntry(MonetaryAmount amount) {
			super(amount, "Custom entry");
		}
	}
}
