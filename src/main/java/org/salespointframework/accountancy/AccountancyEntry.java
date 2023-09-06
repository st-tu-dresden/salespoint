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

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.accountancy.AccountancyEntry.AccountancyEntryIdentifier;
import org.salespointframework.core.AbstractEntity;
import org.springframework.util.Assert;

/**
 * This class represents an accountancy entry. It is advisable to sub-class it, to define specific entry types for an
 * accountancy, for example a {@link ProductPaymentEntry}.
 *
 * @author Hannes Weisbach
 * @author Oliver Drotbohm
 */
@Entity
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED, onConstructor = @__(@Deprecated))
public class AccountancyEntry extends AbstractEntity<AccountancyEntryIdentifier> {

	private @EmbeddedId AccountancyEntryIdentifier accountancyEntryIdentifier = AccountancyEntryIdentifier
			.of(UUID.randomUUID().toString());

	private @Getter MonetaryAmount value;
	private @Setter(AccessLevel.PACKAGE) LocalDateTime date = null;
	private @Getter String description;

	/**
	 * Creates a new <code>AccountancyEntry</code> with a specific value.
	 *
	 * @param value The value that is stored in this entry.
	 */
	public AccountancyEntry(MonetaryAmount value) {
		this(value, "");
	}

	/**
	 * Creates a new <code>AccountancyEntry</code> with a specific value and a user defined description.
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
	 * Returns whether this entry already has a {@code date} set.
	 *
	 * @return true if and only if this entry's date is not {@literal null}.
	 */
	public boolean hasDate() {
		return date != null;
	}

	/**
	 * Returns the date this entry was posted.
	 * 
	 * @return the date when this entry was posted, or an empty {@code Optional} if no date is set.
	 */
	public Optional<LocalDateTime> getDate() {
		return Optional.ofNullable(date);
	}

	/**
	 * Returns the unique identifier of this {@link AccountancyEntry}.
	 * 
	 * @return will never be {@literal null}
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

	/**
	 * Manual verification that invariants are met as JPA requires us to expose a default constructor that also needs to
	 * be callable from sub-classes as they need to declare one as well.
	 */
	@PrePersist
	void verifyConstraints() {
		Assert.state(value != null,
				"No value set! Make sure you have created the accountancy entry by calling a non-default constructor!");
	}

	/**
	 * {@link AccountancyEntryIdentifier} serves as an identifier type and primary key for {@link AccountancyEntry}
	 * objects. The main reason for its existence is type safety for identifiers across the Salespoint framework.
	 * However, it can also be used as a key for non-persistent, {@link Map}-based implementations.
	 *
	 * @author Hannes Weisbach
	 * @author Oliver Drotbohm
	 */
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
	public static class AccountancyEntryIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -7802218428666489137L;

		private final String accountancyEntryId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return accountancyEntryId;
		}
	}
}
