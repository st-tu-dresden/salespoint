package test.product;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.util.ArgumentNullException;

@SuppressWarnings("javadoc")
public class ProductTest {

	private Keks keks;
	
	
	@Before
	public void before() {
		keks = new Keks("Schoooki", Money.ZERO);
	}
	
	
	@Test(expected=ArgumentNullException.class)
	public void addNullProductFeature() {
		keks.addProductFeature(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void removeNullProductFeature() {
		keks.removeProductFeature(null);
	}
	
	@Test
	public void addProductFeature() {
		ProductFeature feature = ProductFeature.create("Color", "Blue");
		assertTrue(keks.addProductFeature(feature));
	}
	
	@Test
	public void addProductFeature2() {
		ProductFeature feature = ProductFeature.create("Color", "Yellow");
		keks.addProductFeature(feature);
		assertFalse(keks.addProductFeature(feature));
	}

	@Test
	public void removeProductFeature() {
		ProductFeature feature = ProductFeature.create("Color", "Green");
		keks.addProductFeature(feature);
		assertTrue(keks.removeProductFeature(feature.getIdentifier()));
	}
	
	@Test
	public void removeProductFeature2() {
		ProductFeature feature = ProductFeature.create("Color", "Red");
		assertFalse(keks.removeProductFeature(feature.getIdentifier()));
	}

	@Test(expected=ArgumentNullException.class)
	public void addNullCategory() {
		keks.addCategory(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void removeNullCategory() {
		keks.removeCategory(null);
	}
	
	@Test
	public void addCategory() {
		assertTrue(keks.addCategory("Sci-Fi"));
	}
	
	@Test
	public void addCategory2() {
		keks.addCategory("Fantasy");
		assertFalse(keks.addCategory("Fantasy"));
	}

	
	@Test
	public void removeCategory() {
		keks.addCategory("Sci-Fi");
		assertTrue(keks.removeCategory("Sci-Fi"));
	}
	
	@Test
	public void removeCategory2() {
		assertFalse(keks.removeCategory(Double.toString(new Random().nextDouble())));
	}
	
	
}
