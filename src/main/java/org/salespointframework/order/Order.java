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
import java.util.List;

import javax.money.MonetaryAmount;
import javax.persistence.*;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.ChargeLine.AttachedChargeLine;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Range.Bound;
import org.springframework.data.util.StreamUtils;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@Table(name = "ORDERS")
@ToString(doNotUseGetters = true)
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
	private List<OrderLine> orderLines = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL) //
	private List<ChargeLine> chargeLines = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL) //
	private List<AttachedChargeLine> attachedChargeLines = new ArrayList<>();

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

	/**
	 * Returns all {@link OrderLine}s of the {@link Order}.
	 * 
	 * @return
	 */
	public Totalable<OrderLine> getOrderLines() {
		return Totalable.of(orderLines);
	}

	/**
	 * Returns all {@link OrderLine} instances that refer to the given {@link Product}.
	 * 
	 * @param product must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	public Totalable<OrderLine> getOrderLines(Product product) {

		Assert.notNull(product, "Product must not be null!");

		return Totalable.of(Streamable.of(() -> orderLines.stream() //
				.filter(it -> it.refersTo(product))));
	}

	/**
	 * Returns all {@link ChargeLine} instances registered for the current {@link Order}.
	 * 
	 * @return
	 */
	public Totalable<ChargeLine> getChargeLines() {
		return Totalable.of(chargeLines);
	}

	/**
	 * Returns all {@link ChargeLine} instances, i.e. both standard ones and {@link AttachedChargeLine}s.
	 * 
	 * @return
	 */
	public Totalable<ChargeLine> getAllChargeLines() {
		return Totalable.of(chargeLines).and(attachedChargeLines);
	}

	/**
	 * Returns all {@link AttachedChargeLine}s for the {@link OrderLine} with the given index.
	 * 
	 * @param index must be in the range of {@link OrderLine}s.
	 * @return
	 * @since 7.1
	 */
	public Totalable<AttachedChargeLine> getChargeLines(int index) {
		return getChargeLines(getRequiredOrderLineByIndex(index));
	}

	/**
	 * Returns all {@link AttachedChargeLine}s for the given {@link OrderLine}.
	 * 
	 * @param orderLine must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	public Totalable<AttachedChargeLine> getChargeLines(OrderLine orderLine) {

		List<AttachedChargeLine> foo = attachedChargeLines;

		return Totalable.of(Streamable.of(() -> foo.stream() //
				.filter(it -> it.belongsTo(orderLine))));
	}

	/**
	 * Returns the total price of the {@link Order}.
	 * 
	 * @return
	 * @since 7.1
	 */
	public MonetaryAmount getTotal() {
		return getOrderLines().getTotal().add(getAllChargeLines().getTotal());
	}

	/**
	 * Returns the total price of the {@link Order}.
	 * 
	 * @return
	 * @deprecated since 7.1, use {@link #getTotal()} instead.
	 */
	@Deprecated
	public MonetaryAmount getTotalPrice() {
		return getTotal();
	}

	/**
	 * Returns the total of all {@link OrderLine}s.
	 * 
	 * @return
	 * @deprecated since 7.1, use {@link #getOrderLines()} and call {@link Totalable#getTotal()} on the result.
	 */
	@Deprecated
	public MonetaryAmount getOrderedLinesPrice() {
		return Priced.sumUp(orderLines);
	}

	/**
	 * Returns the total of all charge lines registered with the order and order lines.
	 * 
	 * @return
	 * @deprecated since 7.1, prefer {@link #getChargeLines()}, {@link #getAllChargeLines()} and call
	 *             {@link PricedTotalable#getTotal()} on the result for fine grained control over which
	 *             {@link ChargeLine}s to calculate the total for.
	 */
	@Deprecated
	public MonetaryAmount getChargeLinesPrice() {
		return Priced.sumUp(getAllChargeLines());
	}

	/**
	 * Adds an {@link OrderLine} to the {@link Order}, the {@link OrderStatus} must be OPEN.
	 * 
	 * @param orderLine the {@link OrderLine} to be added.
	 * @return the {@link OrderLine} added.
	 * @throws IllegalArgumentException if orderLine is {@literal null}.
	 * @deprecated since 7.1, use {@link #addOrderLine(Product, Quantity)} instead.
	 */
	@Deprecated
	public OrderLine add(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");
		assertOrderIsOpen();

		this.orderLines.add(orderLine);

		return orderLine;
	}

	/**
	 * Adds an {@link OrderLine} for the given with the given {@link Quantity}.
	 * 
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 * @return the {@link OrderLine} added.
	 * @since 7.1
	 */
	public OrderLine addOrderLine(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must not be null!");
		Assert.notNull(quantity, "Quantity must not be null!");

		OrderLine orderLine = new OrderLine(product, quantity);

		this.orderLines.add(orderLine);

		return orderLine;
	}

	/**
	 * Removes the given {@link OrderLine} as well as all {@link AttachedChargeLine} associated with it.
	 * 
	 * @param orderLine must not be {@literal null}.
	 */
	public void remove(OrderLine orderLine) {

		Assert.notNull(orderLine, "OrderLine must not be null!");
		assertOrderIsOpen();

		removeChargeLinesFor(orderLine);

		this.orderLines.remove(orderLine);
	}

	/**
	 * Adds a charge line to the {@link Order}.
	 * 
	 * @param chargeLine
	 * @deprecated since 7.1, use {@link #addChargeLine(MonetaryAmount, String)} instead
	 */
	@Deprecated
	public ChargeLine add(ChargeLine chargeLine) {

		Assert.notNull(chargeLine, "ChargeLine must not be null!");
		assertOrderIsOpen();

		this.chargeLines.add(chargeLine);

		return chargeLine;
	}

	/**
	 * Adds a {@link ChargeLine} with the given price and description to the {@link Order}.
	 * 
	 * @param price must not be {@literal null}.
	 * @param description must not be {@literal null}.
	 * @return the {@link ChargeLine} created.
	 * @since 7.1
	 */
	public ChargeLine addChargeLine(MonetaryAmount price, String description) {

		Assert.notNull(price, "Price must not be null!");
		Assert.notNull(description, "Description must not be null!");

		assertOrderIsOpen();

		ChargeLine chargeLine = new ChargeLine(price, description);

		this.chargeLines.add(chargeLine);

		return chargeLine;
	}

	/**
	 * Adds an {@link AttachedChargeLine} with the given price and description to the {@link OrderLine} with the given
	 * index.
	 * 
	 * @param price must not be {@literal null}.
	 * @param description must not be {@literal null}.
	 * @param index must be within the range of {@link OrderLine}s already registered.
	 * @return the {@link AttachedChargeLine} created.
	 * @since 7.1
	 */
	public AttachedChargeLine addChargeLine(MonetaryAmount price, String description, int index) {

		Assert.notNull(price, "Price must not be null!");
		Assert.notNull(description, "Description must not be null!");

		assertOrderIsOpen();

		OrderLine orderLine = getRequiredOrderLineByIndex(index);

		return addChargeLine(price, description, orderLine);
	}

	/**
	 * Adds an {@link AttachedChargeLine} with the given price and description to the given {@link OrderLine}.
	 * 
	 * @param price must not be {@literal null}.
	 * @param description must not be {@literal null}.
	 * @param orderLine must not be {@literal null}.
	 * @return the {@link AttachedChargeLine} created.
	 * @since 7.1
	 */
	public AttachedChargeLine addChargeLine(MonetaryAmount price, String description, OrderLine orderLine) {

		Assert.notNull(price, "Price must not be null!");
		Assert.notNull(description, "Description must not be null!");
		Assert.notNull(orderLine, "Order line must not be null!");

		assertOrderIsOpen();

		AttachedChargeLine chargeLine = new AttachedChargeLine(price, description, orderLine);

		this.attachedChargeLines.add(chargeLine);

		return chargeLine;
	}

	public void remove(ChargeLine chargeLine) {

		Assert.notNull(chargeLine, "ChargeLine must not be null!");

		assertOrderIsOpen();

		this.chargeLines.remove(chargeLine);
	}

	/**
	 * Removes the given {@link AttachedChargeLine} from the {@link Order}.
	 * 
	 * @param chargeLine must not be {@literal null}.
	 * @since 7.1
	 */
	public void remove(AttachedChargeLine chargeLine) {

		Assert.notNull(chargeLine, "Attached charge line must not be null!");

		assertOrderIsOpen();

		this.attachedChargeLines.remove(chargeLine);
	}

	/**
	 * Removes all {@link AttachedChargeLine}s attached to the given {@link OrderLine}.
	 * 
	 * @param orderLine must not be {@literal null}.
	 * @since 7.1
	 */
	public void removeChargeLinesFor(OrderLine orderLine) {

		Assert.notNull(orderLine, "Order line must not be null!");

		getChargeLines(orderLine).stream() //
				.collect(StreamUtils.toUnmodifiableList()) //
				.forEach(this::remove);
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

		Assert.isTrue(isPaid(), "An order must be paid to be completed!");

		this.orderStatus = OrderStatus.COMPLETED;

		registerEvent(OrderCompleted.of(this));

		return this;
	}

	/**
	 * Cancels the current {@link Order}.
	 * 
	 * @return
	 * @deprecated since 7.1, use {@link #cancel(String)} instead.
	 */
	Order cancel() {
		return cancel("¯\\_(ツ)_/¯");
	}

	/**
	 * Cancels the current {@link Order} with the given reason. Will publish an {@link OrderCancelled} even
	 * 
	 * @param reason must not be {@literal null}.
	 * @return
	 */
	Order cancel(String reason) {

		Assert.isTrue(!isCanceled(), "Order is already cancelled!");

		this.orderStatus = OrderStatus.CANCELLED;

		registerEvent(OrderCompleted.of(this));
		registerEvent(OrderCancelled.of(this, reason));

		return this;
	}

	int getNumberOfLineItems() {
		return this.orderLines.size();
	}

	boolean isPaymentExpected() {
		return orderStatus == OrderStatus.OPEN && paymentMethod != null;
	}

	Order markPaid() {

		Assert.isTrue(!isPaid(), "Order is already paid!");

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

	private OrderLine getRequiredOrderLineByIndex(int index) {

		Range<Integer> allowedIndexRange = Range.from(Bound.inclusive(0))//
				.to(Bound.exclusive(orderLines.size()));

		Assert.isTrue(allowedIndexRange.contains(index),
				String.format("Invalid order line index %s. Required: %s!", index, allowedIndexRange));

		return this.orderLines.get(index);
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

	@Value(staticConstructor = "of")
	public static class OrderCancelled {

		Order order;
		String reason;

		/* 
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OrderCancelled: " + reason;
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
