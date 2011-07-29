package test.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.core.product.features.ProductFeatureType;

public class ProductInstanceTest {

	private KeksProduct keks1 = new KeksProduct ("keks1", Money.ZERO);
	private KeksProduct keks2 = new KeksProduct ("keks2", new Money(3.50));
	
	private KeksInstance k1 = new KeksInstance(keks1);
	private KeksInstance k2 = new KeksInstance(keks1);
	private KeksInstance k3 = new KeksInstance(keks2);
	
	private ProductFeature butter = ProductFeature.create("Butter",new Money(0.25));
	private ProductFeature ecken = ProductFeature.create("52",new Money(0.52));
	
	private ProductFeature[] productFeatures ={butter, ecken};
	private ProductFeatureType Butterkeks = new ProductFeatureType("Butterkeks", "", productFeatures);
	 
	
	
	@Test
	public void testEquals() {
		KeksInstance k3 = k1;
		
		assertEquals(k3,k1);
		
	}
	
	@Test
	public void testNotEquals1() {
		
		assertNotSame(k2, k1);
	}

	@Test
	public void testNotEquals2() {
		
		assertNotSame(k3, k1);
	}

	@Test
	public void testGetPrice1() {
		
		assertEquals(k3.getPrice(), new Money(3.50));
		
	}

	@Test
	public void testGetPrice2() {
		
		k3.addProductFeatures(Butterkeks);
		assertEquals(k3.getPrice(), new Money(4.27));
		
	}
	
	@Test
	public void testGetPrice3() {
		
		k3.addProductFeatures(Butterkeks);
		k3.removeProductFeatures();
		assertEquals(k3.getPrice(), new Money(3.50));
		
	}
	
	@Test
	public void testGetProductFeatures(){
		
		for(ProductFeature pf: k1.getProductFeatures()) {
	        System.out.println(pf);
	        
	        assertEquals(pf,null);
		}
		
	}	
	@Test
	public void testAddProductFeatures(){
		
		k1.addProductFeatures(Butterkeks);
		
	    Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
	    	for (ProductFeature p : k1.getProductFeatures()){
	    		if (p!=null){
	    			m1.put(p.getName(), p);
	    		}
	    	}
	    
	    Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
	      m2.put("Butter", butter);
	      m2.put("52", ecken);
	        
		assertEquals(m1,m2);
	      
	}
	
	@Test
	public void testAddProductFeatures2(){
		
		k1.addProductFeatures(Butterkeks);
		
	    Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
	    	for (ProductFeature p : k1.getProductFeatures()){
	    		if (p!=null){
	    			m1.put(p.getName(), p);
	    		}
	    	}
	    
	    Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
	      m2.put("Butter", butter);
	      m2.put("48", ecken);
	        
		assertNotSame(m1,m2);
	      
	}
	
	@Test
	public void testRemoveProductFeatures1(){
		
		k1.removeProductFeatures();
		
		Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
    	for (ProductFeature p : k1.getProductFeatures()){
    		if (p!=null){
    			m1.put(p.getName(), p);
    		}
    	}
    	
    	assertEquals(m1.toString(),"{}");	
		
	}
	
	@Test
	public void testRemoveProductFeatures2(){
		
		k1.addProductFeatures(Butterkeks);
		k1.removeProductFeatures();
		
		Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
    	for (ProductFeature p : k1.getProductFeatures()){
    		if (p!=null){
    			m1.put(p.getName(), p);
    		}
    	}
    	
    	assertEquals(m1.toString(),"{}");	
		
	}
	
	@Test
	public void getNameOfProductFeature1(){
		
		k1.addProductFeatures(Butterkeks);
		
		assertEquals(k1.getProductFeature("52"),ecken);
		
	}
	
	@Test
	public void getNameOfProductFeature2(){
		
		k1.addProductFeatures(Butterkeks);
		
		assertEquals(k1.getProductFeature("50"),null);
		
	}
	
	@Test
	public void testEqualsMethod(){
	
		KeksInstance k4 = k1;
		
		assertTrue(k1.equals(k1));
		assertTrue(k1.equals(k4));
		assertFalse(k1.equals(k2));
		assertFalse(k1.equals(k3));
	}
}
