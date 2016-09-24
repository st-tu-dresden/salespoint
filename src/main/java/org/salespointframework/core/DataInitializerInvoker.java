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
	 * APplication context so that the autowiring of {@link DataInitializer} into {@link DataInitializerInvoker} works.
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
