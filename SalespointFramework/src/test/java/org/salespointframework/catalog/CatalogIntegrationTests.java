/*
 * Copyright 2013 the original author or authors.
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
package org.salespointframework.catalog;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oliver Gierke
 */
@Transactional
public class CatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired Catalog catalog;
	private Keks keks;
	
	@Test
	public void findsProductsByCategory() {
		
		Product product = new Product("MacBook", new Money(2700.0), Metric.UNITS);
		product.addCategory("Apple");
		
		catalog.add(product);
		
		Iterable<Product> result = catalog.findByCategory(Product.class, "Apple");
		
		assertThat(result, is(Matchers.<Product> iterableWithSize(1)));
		assertThat(result, hasItem(product));
	}
	
	@Before
	public void before() {
		keks = new Keks("Schoki", new Money(0));
	}

	@Test
	public void emFindTest() {
		catalog.add(keks);
		Keks kT1 = catalog.get(Keks.class, keks.getIdentifier());
		Product kT2 = catalog.get(Product.class, keks.getIdentifier());
		assertEquals(kT1,kT2);
	}

	@Test(expected = NullPointerException.class)
	public void nullAddTest() {
		catalog.add(null);
	}

	@Test()
	public void addTest() {
		catalog.add(keks);
	}
	
	@Test
	public void testRemove() {
		catalog.add(keks);
		catalog.remove(keks.getIdentifier());
		assertFalse(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void testContains() {
		catalog.add(keks);
		assertTrue(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void getTest() {
		catalog.add(keks);
		assertEquals(keks, catalog.get(Keks.class, keks.getIdentifier()));
	}
}
