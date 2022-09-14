/*
 * Copyright 2019-2022 the original author or authors.
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

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.moduliths.test.ModuleTest.BootstrapMode;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

/**
 * Integration tests for working with custom extensions of {@link Order}
 *
 * @author Oliver Drotbohm
 */
@ModuleTest(BootstrapMode.ALL_DEPENDENCIES)
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
public class OrderCustomizationTests {

	private final OrderManagement<CustomOrder> orders;
	private final UserAccountManagement users;

	@Test // #292
	void sortsByPropertyOfCustomOrder() {

		UserAccount account = users.create("user", UnencryptedPassword.of("password"));
		CustomOrder first = orders.save(new CustomOrder(account, "first"));
		CustomOrder second = orders.save(new CustomOrder(account, "second"));

		var sort = Sort.by("customProperty");

		assertThat(orders.findAll(getFirstPage(sort.ascending()))).containsExactly(first, second);
		assertThat(orders.findAll(getFirstPage(sort.descending()))).containsExactly(second, first);
	}

	private static Pageable getFirstPage(Sort sort) {
		return PageRequest.of(0, 10, sort);
	}
}
