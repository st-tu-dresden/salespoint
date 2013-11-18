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
package org.salespointframework.core.catalog;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.catalog.ProductRepository;
import org.salespointframework.core.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link ProductRepository}.
 * 
 * @author Oliver Gierke
 */
@Transactional
public class ProductRepositoryIntegrationTests extends AbstractIntegrationTests {

	@Autowired ProductRepository<Keks> keksRepository;

	@Test
	public void persistsProductSubtypesCorrectly() {

		Keks doubleChoc = new Keks("DoubleChoc", new Money(1.25d));
		doubleChoc.addCategory("chocolate");
		doubleChoc.property = "Yummy!";

		doubleChoc = keksRepository.save(doubleChoc);

		assertKeksFound(keksRepository.findAll(), doubleChoc);
		assertKeksFound(keksRepository.findByCategory("chocolate"), doubleChoc);
	}

	private static void assertKeksFound(Iterable<Keks> result, Keks keks) {

		assertThat(result, is(Matchers.<Keks> iterableWithSize(1)));
		assertThat(result, hasItem(keks));
		assertThat(result.iterator().next().property, is(keks.property));
	}
}
