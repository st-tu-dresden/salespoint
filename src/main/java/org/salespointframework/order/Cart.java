package org.salespointframework.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * Abstraction of a shopping cart.
 *
 * @authow Paul Henke
 * @author Oliver Gierke
 */
public class Cart implements Iterable<CartItem> {

	private final List<CartItem> cartItems = new ArrayList<>();

	/**
	 * This method is a NOP and deprecated, please use #add(Product, Quantity)}. 
	 */
	@Deprecated
	public boolean add(OrderLine orderLine) {
		return false;
	}

	
	/**
	 * Creates a {@link CartItem} and adds it to the cart.
	 * @param product must not be {@literal null}
	 * @param quantity must not be {@literal null}
	 * @return The created CartItem.
	 */
	
	public CartItem add(Product product, Quantity quantity)  {
		Assert.notNull(product, "Product must not be null!");
		Assert.notNull(quantity, "Quantity must not be null!");
		CartItem cartItem = new CartItem(product, quantity);
		cartItems.add(cartItem);
		return cartItem;
	}

	/**
	 * This method is a NOP and deprecated, please use {@link #remove(String).
	 */
	@Deprecated
	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {
		return false;
	}
	
	/**
	 * Removes the {@link CartItem} with the given identifier.
	 * @param cartItemIdentifier
	 * @return
	 */
	public Optional<CartItem> remove(String cartItemIdentifier) {
		Assert.notNull(cartItemIdentifier, "CartItemIdentifier must not be null!");
		Optional<CartItem> optionalItem = get(cartItemIdentifier);
		optionalItem.ifPresent(cartItem -> cartItems.remove(cartItem));
		return optionalItem;
	}
	
	/**
	 * Returns the CartItem for the given identifier.
	 * @param cartItemIdentifier
	 * @return
	 */
	public Optional<CartItem> get(String cartItemIdentifier) {
		Assert.notNull(cartItemIdentifier, "CartItemIdentifier must not be null!");
		return cartItems.stream().filter(item -> item.getIdentifier().equals(cartItemIdentifier)).findFirst();
	}

	/**
	 * Clears the cart.
	 */
	public void clear() {
		cartItems.clear();
	}

	/**
	 * Returns whether the cart is currently empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return cartItems.isEmpty();
	}

	/**
	 * Returns the total price of the cart.
	 * 
	 * @return
	 */
	public Money getTotalPrice() {
		return cartItems.stream().//
				map(CartItem::getPrice).//
				reduce((left, right) -> left.plus(right)).orElse(Money.zero(CurrencyUnit.EUR));
	}

	/**
	 * Turns the current state of the cart into an {@link Order}.
	 * 
	 * @param order must not be {@literal null}.
	 */
	public void toOrder(Order order) {
		Assert.notNull(order, "Order must not be null!");
		if (order.getOrderStatus() != OrderStatus.OPEN) {
			throw new IllegalArgumentException("OrderStatus must be OPEN");
		}
		cartItems.forEach(item -> order.add(item.toOrderline()));
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<CartItem> iterator() {
		return cartItems.iterator();
	}
}
