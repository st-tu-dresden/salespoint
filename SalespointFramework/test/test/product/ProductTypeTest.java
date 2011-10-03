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
public class ProductTypeTest {

	private KeksType keksType;
	
	
	@Before
	public void before() {
		keksType = new KeksType("Schoooki", Money.ZERO);
	}
	
	
	@Test(expected=ArgumentNullException.class)
	public void addNullProductFeature() {
		keksType.addProductFeature(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void removeNullProductFeature() {
		keksType.removeProductFeature(null);
	}
	
	@Test
	public void addProductFeature() {
		ProductFeature feature = ProductFeature.create("Color", "Blue");
		assertTrue(keksType.addProductFeature(feature));
	}
	
	@Test
	public void addProductFeature2() {
		ProductFeature feature = ProductFeature.create("Color", "Yellow");
		keksType.addProductFeature(feature);
		assertFalse(keksType.addProductFeature(feature));
	}

	@Test
	public void removeProductFeature() {
		ProductFeature feature = ProductFeature.create("Color", "Green");
		keksType.addProductFeature(feature);
		assertTrue(keksType.removeProductFeature(feature));
	}
	
	@Test
	public void removeProductFeature2() {
		ProductFeature feature = ProductFeature.create("Color", "Red");
		assertFalse(keksType.removeProductFeature(feature));
	}

	@Test(expected=ArgumentNullException.class)
	public void addNullCategory() {
		keksType.addCategory(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void removeNullCategory() {
		keksType.removeCategory(null);
	}
	
	@Test
	public void addCategory() {
		assertTrue(keksType.addCategory("Sci-Fi"));
	}
	
	@Test
	public void addCategory2() {
		keksType.addCategory("Fantasy");
		assertFalse(keksType.addCategory("Fantasy"));
	}

	
	@Test
	public void removeCategory() {
		keksType.addCategory("Sci-Fi");
		assertTrue(keksType.removeCategory("Sci-Fi"));
	}
	
	@Test
	public void removeCategory2() {
		assertFalse(keksType.removeCategory(Double.toString(new Random().nextDouble())));
	}
	
	
}
