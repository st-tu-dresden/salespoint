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
package org.salespointframework.payment;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;

/**
 * Unit tests for {@link CreditCard}.
 *
 * @author Oliver Gierke
 */
class CreditCardUnitTests {

	static final LocalDateTime NOW = LocalDateTime.now();
	static final Money AMOUNT = Money.of(2000, Currencies.EURO);

	static final CreditCard VISA = new CreditCard("VISA", "whatever", "1234123412341234", "Oliver Gierke", "Some address",
			NOW, NOW.plusMonths(12), "567", AMOUNT, AMOUNT);
	static final CreditCard VISA_CLONE = new CreditCard("VISA", "whatever", "1234123412341234", "Oliver Gierke",
			"Some address", NOW, NOW.plusMonths(12), "567", AMOUNT, AMOUNT);
	static final CreditCard MASTER_CARD = new CreditCard("MasterCard", "whatever", "1234123412341234", "Oliver Gierke",
			"Some address", NOW, NOW.plusMonths(12), "567", AMOUNT, AMOUNT);

	@Test // #80
	void hasCorrectToString() {

		var cardString = VISA.toString();

		assertThat(cardString).contains(VISA.getCardNumber());
		assertThat(cardString).contains(VISA.getNameOnCard());

		assertThat(cardString).doesNotContain(VISA.getCardVerificationCode());
	}

	@Test // #80
	void equalsHashCode() {

		assertThat(VISA).isEqualTo(VISA);
		assertThat(VISA).isEqualTo(VISA_CLONE);
		assertThat(VISA_CLONE).isEqualTo(VISA);

		assertThat(VISA).isNotEqualTo(MASTER_CARD);
		assertThat(MASTER_CARD).isNotEqualTo(VISA);
	}
}
