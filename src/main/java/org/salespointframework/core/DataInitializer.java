package org.salespointframework.core;

import org.springframework.transaction.annotation.Transactional;

/**
 * Callback interface for components that shall be initialized on application startup.
 *
 * @author Oliver Gierke
 */
public interface DataInitializer {

	/**
	 * Called on application startup to trigger data initialization. Will run inside a transaction.
	 */
	@Transactional
	void initialize();
}
