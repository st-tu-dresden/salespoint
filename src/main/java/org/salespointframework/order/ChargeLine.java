/*
 * Copyright 2017-2020 the original author or authors.
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.salespointframework.core.AbstractEntity;
import org.springframework.util.Assert;

/**
 * A charge line represents extra expenses, such as shipping, for an
 * {@link Order} as a whole. Expenses for an individual {@link OrderLine} can be
 * modeled using the {@link AttachedChargeLine} sub-class.
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

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "CHARGELINE_ID")) //
	private ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier();

	private final @NonNull MonetaryAmount price;
	private final @NonNull String description;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	public ChargeLineIdentifier getId() {
		return chargeLineIdentifier;
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
		 * @param oderLine must not be {@literal null}.
		 * @return
		 */
		public boolean belongsTo(OrderLine oderLine) {

			Assert.notNull(oderLine, "Reference order line must not be null!");

			return this.orderLine.equals(oderLine);
		}
	}
}
