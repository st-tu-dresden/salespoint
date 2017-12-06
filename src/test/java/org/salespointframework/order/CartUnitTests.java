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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link Cart}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartUnitTests {

	static final Quantity QUANTITY = Quantity.of(10);
	static final Product PRODUCT = new Product("name", Money.of(1, Currencies.EURO));

	Cart cart;

	@Before
	public void setUp() {
		cart = new Cart();
	}

	@Test // #44
	public void addsCartItemCorrectly() {

		CartItem reference = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(cart, hasItem(reference));
		assertThat(cart, is(iterableWithSize(1)));
	}

	@Test(expected = IllegalArgumentException.class) // #44
	public void rejectsNullQuantityOnAdding() {
		cart.addOrUpdateItem(PRODUCT, null);
	}

	@Test(expected = IllegalArgumentException.class) // #44
	public void rejectsNullProductOnAdding() {
		cart.addOrUpdateItem(null, QUANTITY);
	}

	@Test(expected = IllegalArgumentException.class) // #44
	public void rejectsNullOnRemovingAnItem() {
		cart.removeItem(null);
	}

	@Test // #44
	public void removesItemsCorrectly() {

		CartItem reference = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(cart.removeItem(reference.getId()).isPresent(), is(true));
		assertThat(cart, is(iterableWithSize(0)));
	}

	@Test // #44
	public void providesAccessToCartItem() {

		CartItem reference = cart.addOrUpdateItem(PRODUCT, QUANTITY);
		Optional<CartItem> item = cart.getItem(reference.getId());

		assertThat(item.isPresent(), is(true));
		assertThat(item.get(), is(reference));
	}

	@Test // #44
	public void returnsEmptyOptionalForNonExistingIdentifier() {

		cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(cart.getItem("foobar"), is(Optional.empty()));
	}

	@Test(expected = IllegalArgumentException.class) // #44
	public void rejectsNullIdentifier() {
		cart.getItem(null);
	}

	@Test // #44
	public void clearsCartCorrectly() {

		cart.addOrUpdateItem(PRODUCT, QUANTITY);
		cart.clear();

		assertThat(cart, is(iterableWithSize(0)));
		assertThat(cart.isEmpty(), is(true));
	}

	@Test // #44
	public void isEmpty() {

		assertThat(cart.isEmpty(), is(true));

		cart.addOrUpdateItem(PRODUCT, QUANTITY);
		assertThat(cart.isEmpty(), is(false));
	}

	@Test(expected = IllegalArgumentException.class) // #44
	public void toOrderFail() {
		cart.addItemsTo(null);
	}

	@Test // #44
	public void updatesCartItemIfOneForProductAlreadyExists() {

		CartItem item = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(item.getProduct(), is(PRODUCT));
		assertThat(item.getQuantity(), is(QUANTITY));
		assertThat(cart, is(iterableWithSize(1)));

		CartItem updated = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(updated, is(not(item)));
		assertThat(cart, is(iterableWithSize(1)));
		assertThat(updated.getProduct(), is(PRODUCT));
		assertThat(updated.getQuantity(), is(QUANTITY.add(QUANTITY)));
	}

	@Test // #198
	public void addsProductWithLongAmount() {

		CartItem cartItem = cart.addOrUpdateItem(PRODUCT, 10L);

		assertThat(cartItem.getQuantity()).isEqualTo(Quantity.of(10L));
	}

	@Test // #198
	public void addsProductWithDoubleAmount() {

		CartItem cartItem = cart.addOrUpdateItem(PRODUCT, 10.0);

		assertThat(cartItem.getQuantity()).isEqualTo(Quantity.of(10.0));
	}

	@Test // #191
	public void keepsIdOfUpdatedCartItem() {

		CartItem cartItem = cart.addOrUpdateItem(PRODUCT, QUANTITY);
		CartItem updated = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(updated.getId()).isEqualTo(cartItem.getId());
	}

	@Test // #191
	public void iteratorKeepsInsertionOrderEvenAfterUpdateItem() {

		Money money = Money.of(1, Currencies.EURO);

		Product product1 = new Product("product_1", money);
		Product product2 = new Product("product_2", money);
		Product product3 = new Product("product_2", money);

		cart.addOrUpdateItem(product1, QUANTITY);
		cart.addOrUpdateItem(product2, QUANTITY);
		cart.addOrUpdateItem(product3, QUANTITY);

		assertThat(cart).extracting(CartItem::getProduct).containsExactly(product1, product2, product3);

		cart.addOrUpdateItem(product1, QUANTITY);

		assertThat(cart).extracting(CartItem::getProduct).containsExactly(product1, product2, product3);
	}
}
