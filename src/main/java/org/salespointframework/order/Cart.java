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

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * Abstraction of a shopping cart.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class Cart implements Streamable<CartItem>, Priced {

	private final Map<ProductIdentifier, CartItem> items = new LinkedHashMap<>();

	/**
	 * Creates a {@link CartItem} for the given {@link Product} and {@link Quantity}. If a {@link CartItem} for the given
	 * {@link Product} already exists the {@link Cart} will be updated to reflect the combined {@link Quantity} for the
	 * backing {@link CartItem}. If the resulting {@link Quantity} is less than or equal to zero then the item is removed
	 * from the {@link Cart}.
	 *
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 * @return the created {@link CartItem} or an empty {@link Optional} if the given {@link Quantity} adds up to zero and
	 *         the item is removed from the {@link Cart}.
	 */
	public Optional<CartItem> addOrUpdateItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must not be null!");
		Assert.notNull(quantity, "Quantity must not be null!");

		return Optional.ofNullable(this.items.compute(product.getId(), (it, item) -> {

			return item == null
					? quantity.isZeroOrNegative() ? null : new CartItem(product, quantity)
					: item.add(quantity);
		}));
	}

	/**
	 * Creates a {@link CartItem} for the given {@link Product} and amount. If a {@link CartItem} for the given
	 * {@link Product} already exists the {@link Cart} will be updated to reflect the combined {@link Quantity} for the
	 * backing {@link CartItem}. If the resulting {@link Quantity} is less than or equal to zero then the item is removed
	 * from the {@link Cart}.
	 *
	 * @param product must not be {@literal null}.
	 * @param amount must not be {@literal null}.
	 * @return the created {@link CartItem} or an empty {@link Optional} if the given {@link Quantity} adds up to zero and
	 *         the item is removed from the {@link Cart}.
	 */
	public Optional<CartItem> addOrUpdateItem(Product product, long amount) {
		return addOrUpdateItem(product, product.createQuantity(amount));
	}

	/**
	 * Creates a {@link CartItem} for the given {@link Product} and amount. If a {@link CartItem} for the given
	 * {@link Product} already exists the {@link Cart} will be updated to reflect the combined {@link Quantity} for the
	 * backing {@link CartItem}. If the resulting {@link Quantity} is less than or equal to zero then the item is removed
	 * from the {@link Cart}.
	 *
	 * @param product must not be {@literal null}.
	 * @param amount must not be {@literal null}.
	 * @return the created {@link CartItem} or an empty {@link Optional} if the given {@link Quantity} adds up to zero and
	 *         the item is removed from the {@link Cart}.
	 */
	public Optional<CartItem> addOrUpdateItem(Product product, double amount) {
		return addOrUpdateItem(product, product.createQuantity(amount));
	}

	/**
	 * Removes the {@link CartItem} with the given identifier.
	 *
	 * @param identifier must not be {@literal null}.
	 */
	public void removeItem(String identifier) {

		Assert.notNull(identifier, "CartItem identifier must not be null!");

		getItem(identifier) //
				.ifPresent(item -> items.remove(item.getProduct().getId()));
	}

	/**
	 * Returns the CartItem for the given identifier.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<CartItem> getItem(String identifier) {

		Assert.notNull(identifier, "CartItem identifier must not be null!");

		return items.values().stream() //
				.filter(item -> item.getId().equals(identifier)) //
				.findFirst();
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
	 * Adds all items currently contained in this cart to the given {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 * @return the {@link Order} with the added items of this cart
	 * @throws IllegalStateException if the given Order is not {@link OrderStatus#OPEN} anymore.
	 */
	public Order addItemsTo(Order order) {

		Assert.notNull(order, "Order must not be null!");

		items.values().forEach(item -> order.addOrderLine(item.getProduct(), item.getQuantity()));

		return order;
	}

	/**
	 * Creates a new Order for the given {@link UserAccount} from the current {@link Cart}.
	 *
	 * @param user must not be {@literal null}.
	 * @return a new Order for the current {@link Cart} and given {@link UserAccount}.
	 */
	public Order createOrderFor(UserAccount user) {

		Assert.notNull(user, "User account must not be null!");

		return addItemsTo(new Order(user));
	}

	/**
	 * Returns the number of items currently in the cart. Sums up the contained {@link Product}'s units defaulting to a
	 * single unit in case the {@link Product} is not handled in units.
	 *
	 * @return will never be {@literal null}.
	 * @see Quantity#toUnit()
	 * @since 7.5
	 */
	public int getNumberOfItems() {

		return stream() //
				.map(CartItem::getQuantity) //
				.map(Quantity::toUnit) //
				.map(Quantity::getAmount) //
				.map(BigDecimal::intValue) //
				.reduce(0, Integer::sum);
	}

	/**
	 * Returns the quantity for the given {@link Product} currently contained in the {@link Cart}.
	 *
	 * @param product must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 7.5
	 */
	public Quantity getQuantity(Product product) {

		Assert.notNull(product, "Product must not be null!");

		return getQuantity(product.getId());
	}

	/**
	 * Returns the quantity for the given {@link ProductIdentifier} currently contained in the {@link Cart}.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 7.5
	 */
	public Quantity getQuantity(ProductIdentifier identifier) {

		Assert.notNull(identifier, "ProductIdentifier must not be null!");

		var item = items.get(identifier);

		return item == null ? Quantity.NONE : item.getQuantity();
	}

	/**
	 * Returns the size of the {@link Cart}, in other words, the number of items in it.
	 *
	 * @return
	 * @since 8.0.1
	 */
	public int size() {
		return items.size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.Priced#getPrice()
	 */
	@Override
	public MonetaryAmount getPrice() {

		return items.values().stream() //
				.map(CartItem::getPrice) //
				.reduce(MonetaryAmount::add) //
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
}
