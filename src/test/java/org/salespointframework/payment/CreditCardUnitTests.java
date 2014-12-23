package org.salespointframework.payment;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

/**
 * Unit tests for {@link CreditCard}.
 * 
 * @author Oliver Gierke
 */
public class CreditCardUnitTests {

	static final LocalDateTime NOW = LocalDateTime.now();

	static final CreditCard VISA = new CreditCard("VISA", "whatever", "1234123412341234", "Oliver Gierke",
			"Some address", NOW, NOW.plusMonths(12), "567", Money.of(CurrencyUnit.EUR, 2000),
			Money.of(CurrencyUnit.EUR, 2000));
	static final CreditCard VISA_CLONE = new CreditCard("VISA", "whatever", "1234123412341234", "Oliver Gierke",
			"Some address", NOW, NOW.plusMonths(12), "567", Money.of(CurrencyUnit.EUR, 2000),
			Money.of(CurrencyUnit.EUR, 2000));
	static final CreditCard MASTER_CARD = new CreditCard("MasterCard", "whatever", "1234123412341234", "Oliver Gierke",
			"Some address", NOW, NOW.plusMonths(12), "567", Money.of(CurrencyUnit.EUR, 2000),
			Money.of(CurrencyUnit.EUR, 2000));

	/**
	 * @see #80
	 */
	@Test
	public void hasCorrectToString() {

		String cardString = VISA.toString();

		assertThat(cardString, containsString(VISA.getCardNumber()));
		assertThat(cardString, containsString(VISA.getNameOnCard()));

		assertThat(cardString, not(containsString(VISA.getCardVerificationCode())));
	}

	/**
	 * @see #80
	 */
	@Test
	public void equalsHashCode() {

		assertThat(VISA, is(VISA));
		assertThat(VISA, is(VISA_CLONE));
		assertThat(VISA_CLONE, is(VISA));

		assertThat(VISA, is(not(MASTER_CARD)));
		assertThat(MASTER_CARD, is(not(VISA)));
	}
}
