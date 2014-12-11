package org.salespointframework.order;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

/**
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@Table(name = "ORDERS")
public class Order extends AbstractEntity<OrderIdentifier> {

	private static final long serialVersionUID = 7417079332245151314L;

	@EmbeddedId//
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))//
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	@Lob//
	private PaymentMethod paymentMethod;

	@OneToOne//
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))//
	private UserAccount userAccount;

	@Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")//
	private LocalDateTime dateCreated = null;

	// tag::orderStatus[]
	@Enumerated(EnumType.STRING)//
	private OrderStatus orderStatus = OrderStatus.OPEN;
	// end::orderStatus[]

	@OneToMany(cascade = CascadeType.ALL)//
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();

	@OneToMany(cascade = CascadeType.ALL)//
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected Order() {}

	/**
	 * Creates a new Order
	 * 
	 * @param userAccount The {@link UserAccount} connected to this order, must not be {@literal null}.
	 */
	public Order(UserAccount userAccount) {

		Assert.notNull(userAccount, "userAccount must not be null");
		this.userAccount = userAccount;
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
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.AbstractEntity#getIdentifier()
	 */
	public final OrderIdentifier getIdentifier() {
		return orderIdentifier;
	}

	public Iterable<OrderLine> getOrderLines() {
		return Collections.unmodifiableSet(orderLines);
	}

	public Iterable<ChargeLine> getChargeLines() {
		return Collections.unmodifiableSet(chargeLines);
	}

	public final OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public final UserAccount getUserAccount() {
		return userAccount;
	}

	public Money getTotalPrice() {
		return getOrderedLinesPrice().plus(getChargeLinesPrice());
	}

	public Money getOrderedLinesPrice() {
		return sumUp(orderLines);
	}

	public Money getChargeLinesPrice() {
		return sumUp(chargeLines);
	}

	public final LocalDateTime getDateCreated() {
		return dateCreated;
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

	private Money sumUp(Collection<? extends Priced> priced) {

		return priced.stream().//
				map(Priced::getPrice).//
				reduce((left, right) -> left.plus(right)).orElse(Money.zero(CurrencyUnit.EUR));
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

	void complete() {
		this.orderStatus = OrderStatus.COMPLETED;
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

	ProductPaymentEntry markPaid() {

		ProductPaymentEntry ppe = new ProductPaymentEntry(this.orderIdentifier, this.userAccount, this.getTotalPrice(),
				"Rechnung Nr. " + this.orderIdentifier, this.paymentMethod);
		orderStatus = OrderStatus.PAID;

		return ppe;
	}

	void setDateCreated(LocalDateTime dateTime) {
		this.dateCreated = dateTime;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User: " + userAccount.toString() + " | Order" + orderIdentifier.toString();
	}
}
