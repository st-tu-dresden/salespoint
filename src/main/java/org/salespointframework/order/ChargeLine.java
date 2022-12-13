/*
 * Copyright 2017-2022 the original author or authors.
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
package org.salespointframework.order;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.ChargeLine.ChargeLineIdentifier;
import org.springframework.util.Assert;

/**
 * A charge line represents extra expenses, such as shipping, for an {@link Order} as a whole. Expenses for an
 * individual {@link OrderLine} can be modeled using the {@link AttachedChargeLine} sub-class.
 * <p>
 * This class is immutable.
 *
 * @see AttachedChargeLine
 * @see OrderLine
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ChargeLine extends AbstractEntity<ChargeLineIdentifier> implements Priced {

	private @EmbeddedId ChargeLineIdentifier chargeLineIdentifier = ChargeLineIdentifier.of(UUID.randomUUID().toString());

	private final @NonNull MonetaryAmount price;
	private final @NonNull String description;

	/**
	 * Returns the unique identifier of this {@link ChargeLine}.
	 * 
	 * @return will never be {@literal null}
	 */
	public ChargeLineIdentifier getId() {
		return chargeLineIdentifier;
	}

	/**
	 * {@link ChargeLineIdentifier} serves as an identifier type for {@link ChargeLine} objects. The main reason for its
	 * existence is type safety for identifier across the Salespoint Framework.
	 *
	 * @author Paul Henke
	 * @author Oliver Gierke
	 */
	@Embeddable
	@Value
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class ChargeLineIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -2971693909958003661L;

		String id;
	}

	/**
	 * A {@link ChargeLine} that's attached to an {@link OrderLine}. Create via
	 * {@link Order#addChargeLine(MonetaryAmount, String, int)} or
	 * {@link Order#addChargeLine(MonetaryAmount, String, OrderLine)}.
	 *
	 * @author Oliver Gierke
	 * @since 7.1
	 */
	@Entity
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	@Getter
	public static class AttachedChargeLine extends ChargeLine {

		private final @ManyToOne(optional = false) OrderLine orderLine;

		/**
		 * Creates a new {@link AttachedChargeLine} for the given price, description and {@link OrderLine}.
		 *
		 * @param price must not be {@literal null}.
		 * @param description must not be {@literal null}.
		 * @param orderLine must not be {@literal null}.
		 */
		AttachedChargeLine(MonetaryAmount price, String description, OrderLine orderLine) {

			super(price, description);

			Assert.notNull(orderLine, "Order line must not be null!");

			this.orderLine = orderLine;
		}

		/**
		 * Returns whether the {@link AttachedChargeLine} belongs to the given {@link OrderLine}.
		 *
		 * @param orderLine must not be {@literal null}.
		 * @return
		 */
		public boolean belongsTo(OrderLine orderLine) {

			Assert.notNull(orderLine, "Reference order line must not be null!");

			return this.orderLine.equals(orderLine);
		}
	}
}
