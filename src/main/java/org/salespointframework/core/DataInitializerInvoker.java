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
package org.salespointframework.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Internal component triggering the initialization of all {@link DataInitializer} instances registered in the
 * {@link ApplicationContext}.
 * 
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor
class DataInitializerInvoker implements ApplicationRunner {

	private final @NonNull List<DataInitializer> initializers;

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
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
