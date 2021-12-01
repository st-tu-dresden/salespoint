/*
 * Copyright 2017-2019 the original author or authors.
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

import java.util.Arrays;
import java.util.Collection;

import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.core.SalespointRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Product}s
 *
 * @author Oliver Gierke
 */
@Repository
public interface Catalog<T extends Product> extends SalespointRepository<T, ProductIdentifier> {

	/**
	 * Returns all {@link Product}s assigned to the given category.
	 *
	 * @param category
	 * @return
	 */
	@Query("select p from #{#entityName} p where :category member of p.categories")
	Streamable<T> findByCategory(String category);

	/**
	 * Returns all {@link Product} that are assigned to all given categories.
	 *
	 * @param categories must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	default Streamable<T> findByAllCategories(String... categories) {
		return findByAllCategories(Arrays.asList(categories));
	}

	/**
	 * Returns all {@link Product} that are assigned to all given categories.
	 *
	 * @param categories must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	@Query("select  p from #{#entityName} p " + //
			"where (select count(c.id) " + //
			"from #{#entityName} p2 inner join p2.categories c " + //
			"where p2.id = p.id and c.id in :categories) = ?#{#categories.size().longValue()}")
	Streamable<T> findByAllCategories(Collection<String> categories);

	/**
	 * Returns all {@link Product}s that are assigned to any of the given categories.
	 *
	 * @param categories must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	default Streamable<T> findByAnyCategory(String... categories) {
		return findByAnyCategory(Arrays.asList(categories));
	}

	/**
	 * Returns all {@link Product}s that are assigned to any of the given categories.
	 *
	 * @param categories must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	@Query("select p from #{#entityName} p join p.categories c where c in :categories")
	Streamable<T> findByAnyCategory(Collection<String> categories);

	/**
	 * Returns the {@link Product}s with the given name.
	 *
	 * @param name
	 * @return
	 */
	Streamable<T> findByName(String name);
}
