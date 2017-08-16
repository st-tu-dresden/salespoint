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
package org.salespointframework.core;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Internal component triggering the initialization of all {@link DataInitializer} instances registered in the
 * {@link ApplicationContext}.
 * 
 * @author Oliver Gierke
 */
@Component
class DataInitializerInvoker {

	/**
	 * Creates a new {@link DataInitializerInvoker} with the given {@link DataInitializer} and triggers the
	 * initialization.
	 * 
	 * @param initializers must not be {@literal null}.
	 */
	public DataInitializerInvoker(List<DataInitializer> initializers) {

		Assert.notNull(initializers, "DataIntializers must not be null!");

		initializers.forEach(DataInitializer::initialize);
	}

	/**
	 * Fallback {@link DataInitializer} to make sure we always have a t least one {@link DataInitializer} in the
	 * Application context so that the autowiring of {@link DataInitializer} into {@link DataInitializerInvoker} works.
	 *
	 * @author Oliver Gierke
	 */
	@Component
	static class NoOpDataInitializer implements DataInitializer {

		/*
		 * (non-Javadoc)
		 * @see org.salespointframework.core.DataInitializer#initialize()
		 */
		@Override
		public void initialize() {}
	}
}
