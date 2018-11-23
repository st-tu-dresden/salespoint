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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.Currencies;
import org.springframework.util.Assert;

/**
 * This class represents an accountancy entry. It is advisable to sub-class it, to define specific entry types for an
 * accountancy, for example a {@link ProductPaymentEntry}.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Entity
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class AccountancyEntry extends AbstractEntity<AccountancyEntryIdentifier> {

	// TODO: if the column is not renamed, it does not work. instead,
	// ProductPaymentEntry's USER_ID column becomes PK. This fucks everything
	// up, if a PersistentAccountancyEntry is retrieved from the database,
	// because its "PK" (USER_ID) would be NULL. Renaming keeps ENTRY_ID the PK.
	// IdClass annotation did not work. Maybe Using a DescriptorCustomizer can
	// be used, to correct the mappings (don't forget to sacrifice a chicken, if
	// it does).
	@EmbeddedId @AttributeOverride(name = "id", column = @Column(name = "ENTRY_ID", nullable = false)) //
	private AccountancyEntryIdentifier accountancyEntryIdentifier = new AccountancyEntryIdentifier();

	private @Getter MonetaryAmount value = Currencies.ZERO_EURO;
	private @Setter(AccessLevel.PACKAGE) LocalDateTime date = null;
	private @Getter String description = "";

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific value.
	 * 
	 * @param value The value that is stored in this entry.
	 */
	public AccountancyEntry(MonetaryAmount value) {
		this(value, "");
	}

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific value and a user defined time.
	 * 
	 * @param value The value that is stored in this entry.
	 * @param description A user-supplied description for this entry.
	 */
	public AccountancyEntry(MonetaryAmount value, String description) {

		Assert.notNull(value, "Value must not be null");
		Assert.notNull(description, "Description must not be null");

		this.value = value;
		this.description = description;
	}

	/**
	 * Returns whether the {@link AccountancyEntry} already has a {@link Date} set.
	 * 
	 * @return
	 */
	public boolean hasDate() {
		return date != null;
	}

	/**
	 * @return the {@link DateTime} when this entry was posted.
	 */
	public final Optional<LocalDateTime> getDate() {
		return Optional.ofNullable(date);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public AccountancyEntryIdentifier getId() {
		return accountancyEntryIdentifier;
	}

	/**
	 * Returns whether the entry is considered revenue, i.e. its value is zero or positive.
	 * 
	 * @return
	 * @since 7.1
	 */
	public boolean isRevenue() {
		return value.isPositiveOrZero();
	}

	/**
	 * Returns whether the entry is considered expense, i.e. its value is negative.
	 * 
	 * @return
	 * @since 7.1
	 */
	public boolean isExpense() {
		return value.isNegative();
	}
}
