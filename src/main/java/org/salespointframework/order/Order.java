package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
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
import org.salespointframework.core.Streamable;
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
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated) )
public class Order extends AbstractEntity<OrderIdentifier> {

	private static final long serialVersionUID = 7417079332245151314L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID") ) //
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	private @Lob PaymentMethod paymentMethod;

	@Getter //
	@OneToOne //
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID") ) //
	private UserAccount userAccount;

	@Getter //
	@Setter(AccessLevel.PACKAGE) private LocalDateTime dateCreated = null;

	@Getter
	// tag::orderStatus[]
	@Enumerated(EnumType.STRING) //
	private OrderStatus orderStatus = OrderStatus.OPEN;
	// end::orderStatus[]

	@OneToMany(cascade = CascadeType.ALL) //
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();

	@OneToMany(cascade = CascadeType.ALL) //
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();

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

	/**
	 * Asserts that the {@link Order} is {@link OrderStatus#OPEN}. Usually a precondition to manipulate the {@link Order}
	 * state internally.
	 */
	private void assertOrderIsOpen() {

		if (!isOpen()) {
			throw new IllegalStateException("Order is not open anymore! Current state is: " + orderStatus);
		}
	}
}
