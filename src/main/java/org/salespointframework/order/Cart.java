/*
 * Copyright 2017 the original author or authors.
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

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.core.Streamable;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * Abstraction of a shopping cart.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
public class Cart implements Streamable<CartItem>, Priced {

	// CartItem.Identifier -> CartItem
	private final Map<String, CartItem> items = new LinkedHashMap<>();

	/**
	 * Creates a {@link CartItem} for the given {@link Product} and {@link Quantity}. If a {@link CartItem} for the given
	 * {@link Product} already exists the {@link Cart} will be updated to reflect the combined {@link Quantity} for the
	 * backing {@link CartItem}.
	 *
	 * @param product  must not be {@literal null}
	 * @param quantity must not be {@literal null}
	 * @return The created {@link CartItem}.
	 */
	public CartItem addOrUpdateItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must not be null!");
		Assert.notNull(quantity, "Quantity must not be null!");

		return items.values().stream() //
				.filter(cartItem -> cartItem.getProduct().equals(product)) //
				.findFirst() //
				.map(cartItem -> updateItem(cartItem, quantity)) //
				.orElseGet(() -> addItem(product, quantity));
	}

	/**
	 * Removes the {@link CartItem} with the given identifier.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<CartItem> removeItem(String identifier) {

		Assert.notNull(identifier, "CartItem identifier must not be null!");
		return Optional.ofNullable(items.remove(identifier));
	}

	/**
	 * Returns the CartItem for the given identifier.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<CartItem> getItem(String identifier) {

		Assert.notNull(identifier, "CartItem identifier must not be null!");

		return Optional.ofNullable(items.get(identifier));
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
		items.values().forEach(item -> order.add(item.toOrderLine()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.Priced#getPrice()
	 */
	@Override
	public MonetaryAmount getPrice() {

		return items.values().stream() //
				.map(CartItem::getPrice) //
				.reduce(MonetaryAmount::add)//
				.orElse(Money.of(0, Currencies.EURO));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<CartItem> iterator() {
		return items.values().iterator();
	}

	/**
	 * Adds a {@link CartItem} for the given {@link Product} with the given {@link Quantity} to the {@link Cart}.
	 *
	 * @param product  must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 * @return
	 */
	private CartItem addItem(Product product, Quantity quantity) {

		CartItem cartItem = new CartItem(UUID.randomUUID().toString(), product, quantity);
		this.items.put(cartItem.getId(), cartItem);

		return cartItem;
	}

	private CartItem updateItem(CartItem cartItem, Quantity quantity) {
		CartItem newItem = new CartItem(cartItem.getId(), cartItem.getProduct(), cartItem.getQuantity().add(quantity));
		items.put(newItem.getId(), newItem);

		return newItem;
	}
}
