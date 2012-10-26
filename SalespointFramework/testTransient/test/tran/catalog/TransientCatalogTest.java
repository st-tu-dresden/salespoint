package test.tran.catalog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.catalog.TransientCatalog;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.core.quantity.Units;

import test.tran.product.TransientKeks;

@SuppressWarnings("javadoc")
public class TransientCatalogTest
{
	private final TransientCatalog catalog = new TransientCatalog();
	private TransientKeks keks;
	

	@Before
	public void before() {
		keks = new TransientKeks("Schoki", new Money(0), Units.of(10).getMetric());
	}

	// TODO verschieben
	@Test
	public void emFindTest() {
		catalog.add(keks);
		TransientKeks kT1 = catalog.get(TransientKeks.class, keks.getIdentifier());
		TransientProduct kT2 = catalog.get(TransientProduct.class, keks.getIdentifier());
		assertEquals(kT1,kT2);
	}

	@Test(expected = NullPointerException.class)
	public void nullAddTest() {
		catalog.add(null);
	}

	@Test()
	public void addTest() {
		catalog.add(keks);
	}
	
	@Test
	public void testRemove() {
		catalog.add(keks);
		catalog.remove(keks.getIdentifier());
		assertFalse(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void testContains() {
		catalog.add(keks);
		assertTrue(catalog.contains(keks.getIdentifier()));
	}
	
	@Test
	public void getTest() {
		catalog.add(keks);
		assertEquals(keks, catalog.get(TransientKeks.class, keks.getIdentifier()));
	}
	
}
