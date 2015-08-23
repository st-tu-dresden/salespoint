package org.salespointframework.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.hibernate.exception.ConstraintViolationException;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Cookie;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Inventory}.
 *
 * @author Oliver Gierke
 */
public class InventoryTests extends AbstractIntegrationTests {

	public @Rule ExpectedException exception = ExpectedException.none();

	@Autowired Inventory<InventoryItem> inventory;
	@Autowired Catalog<Product> catalog;
	@Autowired EntityManager em;

	Cookie cookie;
	InventoryItem item;

	@Before
	public void before() {

		cookie = catalog.save(new Cookie("Add Superkeks", Currencies.ZERO_EURO));
		item = inventory.save(new InventoryItem(cookie, Quantity.of(10)));
	}

	@Test
	public void savesItemsCorrectly() {
		assertThat(inventory.save(item).getIdentifier(), is(notNullValue()));
	}

	/**
	 * @see #34
	 */
	@Test
	public void deletesItemsCorrectly() {

		inventory.delete(item.getIdentifier());

		assertThat(inventory.exists(item.getIdentifier()), is(false));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testExists() {
		assertThat(inventory.exists(item.getIdentifier()), is(true));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testGet() {

		Optional<InventoryItem> result = inventory.findOne(item.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testFindItemsByProduct() {

		Optional<InventoryItem> result = inventory.findByProduct(cookie);

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	/**
	 * @see #34
	 */
	@Test
	public void testFindItemsByProductId() {

		Optional<InventoryItem> result = inventory.findByProductIdentifier(cookie.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(item));
	}

	@Test
	public void testFindByProductIdentifer() {
		OrderLine orderLine = new OrderLine(cookie, Units.of(11L));
		Optional<InventoryItem> result = inventory.findByProductIdentifier(orderLine.getProductIdentifier());
		assertThat(result.isPresent(), is(true));
	}
	/**
	 * @see #34
	 */
	@Test
	public void decreasesItemAndPersistsIt() {

		InventoryItem item = inventory.findByProduct(cookie).get();
		item.decreaseQuantity(Quantity.of(1));

		// Trigger another finder to flush
		Optional<InventoryItem> result = inventory.findByProductIdentifier(cookie.getIdentifier());

		assertThat(result.isPresent(), is(true));
		assertThat(result.get().getQuantity(), is(Quantity.of(9)));
	}

	/**
	 * @see #68
	 */
	public void rejectsNewInventoryItemForExistingProducts() {

		exception.expect(PersistenceException.class);
		exception.expectCause(is(instanceOf(ConstraintViolationException.class)));

		inventory.save(new InventoryItem(cookie, Quantity.of(10)));

		em.flush();
	}
}