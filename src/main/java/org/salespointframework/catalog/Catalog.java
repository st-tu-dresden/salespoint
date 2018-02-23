/*
 * Copyright 2017 the original author or authors.
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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Product}s
 *
 * @author Oliver Gierke
 */
public interface Catalog<T extends Product> extends CrudRepository<T, ProductIdentifier> {

	/**
	 * Returns all {@link Product}s assigned to the given category.
	 * 
	 * @param category
	 * @return
	 */
	@Query("select p from #{#entityName} p where :category member of p.categories")
	Iterable<T> findByCategory(String category);

	/**
	 * Returns the {@link Product}s with the given name.
	 * 
	 * @param name
	 * @return
	 */
	Iterable<T> findByName(String name);
}
