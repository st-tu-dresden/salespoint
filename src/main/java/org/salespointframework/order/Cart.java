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
public class Cart implements Iterable<CartItem>, Priced {

	private final List<CartItem> items = new ArrayList<>();

	/**
	 * Creates a {@link CartItem} for the given {@link Product} and {@link Quantity}. If a {@link CartItem} for the given
	 * {@link Product} already exists the {@link Cart} will be updated to reflect the combined {@link Quantity} for the
	 * backing {@link CartItem}.
	 * 
	 * @param product must not be {@literal null}
	 * @param quantity must not be {@literal null}
	 * @return The created {@link CartItem}.
	 */
	public CartItem addOrUpdateItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must not be null!");
		Assert.notNull(quantity, "Quantity must not be null!");

		return items.stream().//
				filter(item -> item.getProduct().equals(product)).findFirst().//
				map(item -> {
					this.removeItem(item.getIdentifier());
					return addItem(product, item.getQuantity().add(quantity));
				}).//
				orElseGet(() -> addItem(product, quantity));
	}

	/**
	 * Removes the {@link CartItem} with the given identifier.
	 * 
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<CartItem> removeItem(String identifier) {

		Assert.notNull(identifier, "CartItem identifier must not be null!");

		return getItem(identifier).map(item -> {
			items.remove(item);
			return item;
		});
	}

	/**
	 * Returns the CartItem for the given identifier.
	 * 
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<CartItem> getItem(String identifier) {

		Assert.notNull(identifier, "CartItem identifier must not be null!");
		return items.stream().filter(item -> item.getIdentifier().equals(identifier)).findFirst();
	}

	/**
	 * Clears the cart.
	 */
	public void clear() {
		items.clear();
	}

	/**
	 * Returns whether the {@link Cart} is currently empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}

	/**
	 * Turns the current state of the cart into an {@link Order}.
	 * 
	 * @param order must not be {@literal null}.
	 * @throws IllegalStateException if the given Order is not {@link OrderStatus#OPEN} anymore.
	 */
	public void addItemsTo(Order order) {

		Assert.notNull(order, "Order must not be null!");
		items.forEach(item -> order.add(item.toOrderLine()));
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.order.Priced#getPrice()
	 */
	@Override
	public Money getPrice() {

		return items.stream().//
				map(CartItem::getPrice).//
				reduce((left, right) -> left.plus(right)).orElse(Money.zero(CurrencyUnit.EUR));
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<CartItem> iterator() {
		return items.iterator();
	}

	/**
	 * Adds a {@link CartItem} for the given {@link Product} with the given {@link Quantity} to the {@link Cart}.
	 * 
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 * @return
	 */
	private CartItem addItem(Product product, Quantity quantity) {

		CartItem cartItem = new CartItem(product, quantity);
		this.items.add(cartItem);

		return cartItem;
	}
}
