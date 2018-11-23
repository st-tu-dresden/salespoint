package org.salespointframework.quantity;

import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Quantity}.
 * 
 * @author Martin Morgenstern
 */
class QuantityIntegrationTests extends AbstractIntegrationTests {
	@Autowired
	RepeatedNameEntityRepository repository;

	/**
	 * Verifies that an entity which embeds {@link Quantity} and repeats its
	 * attribute names can be written to the database.
	 */
	@Test
	void canRepeatAttributeName() {
		repository.save(new RepeatedNameEntity());
	}
}
