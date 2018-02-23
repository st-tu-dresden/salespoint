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
package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.salespointframework.core.AbstractEntity;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@Table(name = "ORDERS")
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Order extends AbstractEntity<OrderIdentifier> {

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID")) //
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	private @Lob PaymentMethod paymentMethod;

	@Getter //
	@OneToOne //
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID")) //
	private UserAccount userAccount;

	@Getter //
	@Setter(AccessLevel.PACKAGE) //
	private LocalDateTime dateCreated = null;

	@Getter
	// tag::orderStatus[]
	@Enumerated(EnumType.STRING) //
	private OrderStatus orderStatus = OrderStatus.OPEN;
	// end::orderStatus[]

	@OneToMany(cascade = CascadeType.ALL) //
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();

	@OneToMany(cascade = CascadeType.ALL) //
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();

	private transient final Collection<Object> events = new ArrayList<>();

	/**
	 * Creates a new Order
	 * 
	 * @param userAccount The {@link UserAccount} connected to this order, must not be {@literal null}.
	 */
	public Order(UserAccount userAccount) {

		Assert.notNull(userAccount, "userAccount must not be null");

		this.userAccount = userAccount;
		this.dateCreated = LocalDateTime.now();
	}

	/**
	 * Creates a new {@link Order} for the given {@link UserAccount} and {@link PaymentMethod}.
	 * 
	 * @param userAccount The {@link UserAccount} connected to this order, must not be {@literal null}.
	 * @param paymentMethod The {@link PaymentMethod} connected to this order, must not be {@literal null}.
	 */
	public Order(UserAccount userAccount, PaymentMethod paymentMethod) {

		Assert.notNull(userAccount, "UserAccount must not be null!");
		Assert.notNull(paymentMethod, "PaymentMethod must not be null!");

		this.userAccount = userAccount;
		this.paymentMethod = paymentMethod;
		this.dateCreated = LocalDateTime.now();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public OrderIdentifier getId() {
		return orderIdentifier;
	}

	public Streamable<OrderLine> getOrderLines() {
		return Streamable.of(orderLines);
	}

	public Streamable<ChargeLine> getChargeLines() {
		return Streamable.of(chargeLines);
	}

	public MonetaryAmount getTotalPrice() {
		return getOrderedLinesPrice().add(getChargeLinesPrice());
	}

	public MonetaryAmount getOrderedLinesPrice() {
		return Priced.sumUp(orderLines);
	}

	public MonetaryAmount getChargeLinesPrice() {
		return Priced.sumUp(chargeLines);
	}

	/**
	 * Adds an {@link OrderLine} to the {@link Order}, the {@link OrderStatus} must be OPEN.
	 * 
	 * @param orderLine the {@link OrderLine} to be added.
	 * @return true if the orderline was added, else {@literal false}.
	 * @throws IllegalArgumentException if orderLine is {@literal null}.
	 */
	public void add(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");
		assertOrderIsOpen();

		this.orderLines.add(orderLine);
	}

	public void remove(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");
		assertOrderIsOpen();

		this.orderLines.remove(orderLine);
	}

	public void add(ChargeLine chargeLine) {

		Assert.notNull(chargeLine, "ChargeLine must not be null!");
		assertOrderIsOpen();

		this.chargeLines.add(chargeLine);
	}

	public void remove(ChargeLine chargeLine) {

		Assert.notNull(chargeLine, "ChargeLine must not be null!");
		assertOrderIsOpen();

		this.chargeLines.remove(chargeLine);
	}

	/**
	 * Convenience method for checking if an order has the status PAID
	 * 
	 * @return true if OrderStatus is PAID, otherwise false
	 */
	public boolean isPaid() {
		return orderStatus == OrderStatus.PAID;
	}

	/**
	 * Convenience method for checking if an order has the status CANCELLED
	 * 
	 * @return true if OrderStatus is CANCELLED, otherwise false
	 */
	public boolean isCanceled() {
		return orderStatus == OrderStatus.CANCELLED;
	}

	/**
	 * Convenience method for checking if an order has the status COMPLETED
	 * 
	 * @return true if OrderStatus is COMPLETED, otherwise false
	 */
	public boolean isCompleted() {
		return orderStatus == OrderStatus.COMPLETED;
	}

	/**
	 * Convenience method for checking if an order has the status OPEN
	 * 
	 * @return true if OrderStatus is OPEN, otherwise false
	 */
	public boolean isOpen() {
		return orderStatus == OrderStatus.OPEN;
	}

	public final PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {

		if (orderStatus != OrderStatus.OPEN) {
			return;
		}

		Assert.notNull(paymentMethod, "PaymentMethod must not be null");
		this.paymentMethod = paymentMethod;
	}

	Order complete() {

		this.orderStatus = OrderStatus.COMPLETED;

		registerEvent(OrderCompleted.of(this));

		return this;
	}

	void cancel() {
		this.orderStatus = OrderStatus.CANCELLED;
	}

	int getNumberOfLineItems() {
		return this.orderLines.size();
	}

	boolean isPaymentExpected() {
		return orderStatus == OrderStatus.OPEN && paymentMethod != null;
	}

	Order markPaid() {

		this.orderStatus = OrderStatus.PAID;

		registerEvent(OrderPaid.of(this));

		return this;
	}

	/**
	 * Asserts that the {@link Order} is {@link OrderStatus#OPEN}. Usually a precondition to manipulate the {@link Order}
	 * state internally.
	 */
	private void assertOrderIsOpen() {

		if (!isOpen()) {
			throw new IllegalStateException("Order is not open anymore! Current state is: " + orderStatus);
		}
	}

	@Value(staticConstructor = "of")
	public static class OrderCompleted {

		Order order;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "OrderCompleted";
		}
	}

	@Value(staticConstructor = "of")
	public static class OrderPaid {

		Order order;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "OrderPaid";
		}
	}

	private <T> T registerEvent(T event) {

		this.events.add(event);

		return event;
	}

	@DomainEvents
	Collection<Object> getEvents() {
		return Collections.unmodifiableCollection(events);
	}

	@AfterDomainEventPublication
	void wipeEvents() {
		this.events.clear();
	}
}
