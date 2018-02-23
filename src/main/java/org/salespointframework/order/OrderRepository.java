/*
 * Copyright 2017-2018 the original author or authors.
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

import java.time.LocalDateTime;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Order}s.
 *
 * @author Oliver Gierke
 */
interface OrderRepository<T extends Order> extends CrudRepository<T, OrderIdentifier> {

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	Iterable<T> findByDateCreatedBetween(LocalDateTime from, LocalDateTime to);

	/**
	 * @param orderStatus
	 * @return
	 */
	Iterable<T> findByOrderStatus(OrderStatus orderStatus);

	/**
	 * @param userAccount
	 * @return
	 */
	Iterable<T> findByUserAccount(UserAccount userAccount);

	/**
	 * @param userAccount
	 * @param from
	 * @param to
	 * @return
	 */
	Iterable<T> findByUserAccountAndDateCreatedBetween(UserAccount userAccount, LocalDateTime from, LocalDateTime to);
}
