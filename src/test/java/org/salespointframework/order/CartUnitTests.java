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
import static org.junit.Assert.*;

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
}
