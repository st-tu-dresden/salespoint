/*
 * Copyright 2020-2022 the original author or authors.
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode;

/**
 * Integration tests for {@link OrderRepository}.
 *
 * @author Oliver Drotbohm
 */
@ApplicationModuleTest(mode = BootstrapMode.DIRECT_DEPENDENCIES)
@RequiredArgsConstructor
class OrderRepositoryIntegrationTests {

	private final OrderRepository<Order> orders;

	@Test // #304
	@SuppressWarnings("deprecation")
	void rejectsInstanceCreatedViaDefaultConstructor() {

		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class) //
				.isThrownBy(() -> orders.save(new Order()));
	}
}
