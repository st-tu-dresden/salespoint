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
import lombok.RequiredArgsConstructor;

import org.springframework.util.Assert;

/**
 * Simple JPA-based implementation of {@link DetachingRepository}.
 *
 * @author Oliver Drotbohm
 * @since 9.0
 */
@RequiredArgsConstructor
class DetachingRepositoryImpl<T> implements DetachingRepository<T> {

	private final EntityManager em;

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.DetachingRepository#detach(java.lang.Object)
	 */
	@Override
	public <S extends T> S detach(S entity) {

		Assert.notNull(entity, "S must not be null!");

		em.detach(entity);

		return entity;
	}
}
