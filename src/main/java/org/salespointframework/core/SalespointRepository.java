/*
 * Copyright 2019 the original author or authors.
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
package org.salespointframework.core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Streamable;

/**
 * Base interface for repositories. Declared explicitly to keep a single point of dependency to Spring Data as well as
 * override {@link #findAll()} to return a {@link Streamable} out of the box.
 *
 * @author Oliver Drotbohm
 * @since 8.0
 */
@NoRepositoryBean
public interface SalespointRepository<T, ID extends SalespointIdentifier> extends CrudRepository<T, ID> {

	/**
	 * Re-declaration of {@link CrudRepository#findAll()} to return {@link Streamable} instead of {@link Iterable} for
	 * easier composition.
	 *
	 * @return all aggregates managed by the repository.
	 */
	Streamable<T> findAll();
}
