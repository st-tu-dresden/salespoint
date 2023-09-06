/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.order;

import static org.assertj.core.api.Assertions.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

/**
 * Unit tests for {@link Cart}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
class CartUnitTests {

	static final Quantity QUANTITY = Quantity.of(10);
	static final Product PRODUCT = new Product("name", Money.of(1, Currencies.EURO));

	Cart cart;

	@BeforeEach
	void setUp() {
		cart = new Cart();
	}

	@Test // #44
	void addsCartItemCorrectly() {

		var reference = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(cart).hasSize(1);
		assertThat(reference).hasValueSatisfying(it -> {
			assertThat(cart).contains(it);
		});
	}

	@Test // #44
	void rejectsNullQuantityOnAdding() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cart.addOrUpdateItem(PRODUCT, null));
	}

	@Test // #44
	void rejectsNullProductOnAdding() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cart.addOrUpdateItem(null, QUANTITY));
	}

	@Test // #44
	void rejectsNullOnRemovingAnItem() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cart.removeItem(null));
	}

	@Test // #44
	void removesItemsCorrectly() {

		cart.addOrUpdateItem(PRODUCT, QUANTITY) //
				.map(CartItem::getId) //
				.ifPresent(cart::removeItem);

		assertThat(cart).hasSize(0);
	}

	@Test // #44
	void providesAccessToCartItem() {

		var reference = cart.addOrUpdateItem(PRODUCT, QUANTITY);
		var item = reference.map(CartItem::getId).flatMap(cart::getItem);

		assertThat(item).isEqualTo(reference);
	}

	@Test // #44
	void returnsEmptyOptionalForNonExistingIdentifier() {

		cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(cart.getItem("foobar")).isEmpty();
	}

	@Test // #44
	void rejectsNullIdentifier() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cart.getItem(null));
	}

	@Test // #44
	void clearsCartCorrectly() {

		cart.addOrUpdateItem(PRODUCT, QUANTITY);
		cart.clear();

		assertThat(cart).hasSize(0);
		assertThat(cart.isEmpty()).isTrue();
	}

	@Test // #44
	void isEmpty() {

		assertThat(cart.isEmpty()).isTrue();

		cart.addOrUpdateItem(PRODUCT, QUANTITY);
		assertThat(cart.isEmpty()).isFalse();
	}

	@Test // #44
	void toOrderFail() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> cart.createOrderFor(null));
	}

	@Test // #44
	void updatesCartItemIfOneForProductAlreadyExists() {

		var item = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(item).hasValueSatisfying(it -> {
			assertThat(it.getProduct()).isEqualTo(PRODUCT);
			assertThat(it.getQuantity()).isEqualTo(QUANTITY);
		});

		assertThat(cart).hasSize(1);

		var updated = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(cart).hasSize(1);
		assertThat(updated).hasValueSatisfying(it -> {
			assertThat(it).isNotEqualTo(item);
			assertThat(it.getProduct()).isEqualTo(PRODUCT);
			assertThat(it.getQuantity()).isEqualTo(QUANTITY.add(QUANTITY));
		});
	}

	@Test // #198
	void addsProductWithLongAmount() {

		var cartItem = cart.addOrUpdateItem(PRODUCT, 10L);

		assertThat(cartItem).hasValueSatisfying(it -> {
			assertThat(it.getQuantity()).isEqualTo(Quantity.of(10L));
		});
	}

	@Test // #198
	void addsProductWithDoubleAmount() {

		var cartItem = cart.addOrUpdateItem(PRODUCT, 10.0);

		assertThat(cartItem).hasValueSatisfying(it -> {
			assertThat(it.getQuantity()).isEqualTo(Quantity.of(10.0));
		});
	}

	@Test // #191
	void keepsIdOfUpdatedCartItem() {

		var cartItem = cart.addOrUpdateItem(PRODUCT, QUANTITY);
		var updated = cart.addOrUpdateItem(PRODUCT, QUANTITY);

		assertThat(updated.map(CartItem::getId)) //
				.isEqualTo(cartItem.map(CartItem::getId));
	}

	@Test // #191
	void iteratorKeepsInsertionOrderEvenAfterUpdateItem() {

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

	@Test // #365
	void effectiveQuantityOfZeroRemovesItemFromCart() {

		var product = new Product("product_1", Money.of(1, Currencies.EURO));

		cart.addOrUpdateItem(product, Quantity.of(1));
		assertThat(cart.isEmpty()).isFalse();

		cart.addOrUpdateItem(product, Quantity.of(-1));
		assertThat(cart.isEmpty()).isTrue();
	}

	@Test // #365
	void addingZeroQuantityDoesNotAddCartItem() {

		var product = new Product("product_1", Money.of(1, Currencies.EURO));
		var item = cart.addOrUpdateItem(product, Quantity.of(0));

		assertThat(cart.isEmpty()).isTrue();
		assertThat(item).isEmpty();
	}

	@Test // #366
	void returnsProductsQuantity() {

		var one = Quantity.of(1);
		var product = new Product("product_1", Money.of(1, Currencies.EURO));

		cart.addOrUpdateItem(product, one);

		assertThat(cart.getQuantity(product)).isEqualTo(one);
		assertThat(cart.getQuantity(product.getId())).isEqualTo(one);
	}

	@Test // #366
	void returnsZeroQuantityForProductNotContainedInCart() {

		var one = Quantity.of(1);
		var product = new Product("product_1", Money.of(1, Currencies.EURO));

		cart.addOrUpdateItem(product, one);

		var reference = new Product("product_2", Money.of(1, Currencies.EURO));

		assertThat(cart.getQuantity(reference)).isEqualTo(Quantity.NONE);
		assertThat(cart.getQuantity(reference.getId())).isEqualTo(Quantity.NONE);
	}

	@Test // #366
	void quanitifiesItems() {

		var apples = new Product("Apples", Money.of(0.40d, Currencies.EURO), Metric.UNIT);
		var wine = new Product("Wine", Money.of(15.00d, Currencies.EURO), Metric.LITER);

		// Unit-based products are reflected by their quantity
		cart.addOrUpdateItem(apples, 5);
		assertThat(cart.getNumberOfItems()).isEqualTo(5);

		// Non-unit-based ones simply are considered "one"
		cart.addOrUpdateItem(wine, Quantity.of(2, Metric.LITER));
		assertThat(cart.getNumberOfItems()).isEqualTo(6);

		cart.addOrUpdateItem(wine, Quantity.of(2, Metric.LITER));
		assertThat(cart.getNumberOfItems()).isEqualTo(6);
	}

	@Test
	void calculatesCartPrice() {

		var apples = new Product("Apples", Money.of(0.40d, Currencies.EURO), Metric.UNIT);
		var wine = new Product("Wine", Money.of(15.00d, Currencies.EURO), Metric.LITER);

		cart.addOrUpdateItem(apples, 5);
		cart.addOrUpdateItem(wine, Quantity.of(2, Metric.LITER));

		assertThat(cart.getPrice()).isEqualTo(Money.of(32, Currencies.EURO));
	}

	@Test // #402
	void calculatesCartSize() {

		var item = cart.addOrUpdateItem(PRODUCT, 5);
		assertThat(cart.size()).isEqualTo(1);

		// Same product, size stays the same
		cart.addOrUpdateItem(PRODUCT, 5);
		assertThat(cart.size()).isEqualTo(1);

		// Different product, size increases
		cart.addOrUpdateItem(new Product("Some product", Money.of(1d, Currencies.EURO)), 5);
		assertThat(cart.size()).isEqualTo(2);

		// Removing item, size decreases
		cart.removeItem(item.orElseThrow().getId());
		assertThat(cart.size()).isEqualTo(1);
	}
}
