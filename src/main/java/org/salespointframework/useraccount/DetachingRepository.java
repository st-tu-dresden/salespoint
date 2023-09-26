/*
 * Copyright 2023 the original author or authors.
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
package org.salespointframework.useraccount;

import jakarta.persistence.EntityManager;

/**
 * Helper interface to be able to detach entities from the persistence context without having to refer to the
 * {@link EntityManager}.
 *
 * @author Oliver Drotbohm
 * @since 9.0
 */
interface DetachingRepository<T> {

	/**
	 * Detaches the given entity from the persistence context
	 *
	 * @param <S> the type of the entity
	 * @param entity must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	<S extends T> S detach(S entity);
}
