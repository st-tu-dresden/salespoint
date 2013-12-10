package org.salespointframework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Sample {@link DataInitializer} for integration tests.
 *
 * @author Oliver Gierke
 */
@Component
public class TestDataInitializer implements DataInitializer {

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {
		assertThat(TransactionSynchronizationManager.isActualTransactionActive(), is(true));
	}
}
