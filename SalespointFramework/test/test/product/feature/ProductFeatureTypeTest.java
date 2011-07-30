package test.product.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.core.product.features.ProductFeatureType;
import org.salespointframework.util.ArgumentNullException;

import test.product.KeksProduct;

public class ProductFeatureTypeTest {

	private ProductFeature butter = ProductFeature.create("Butter",new Money(0.25));
	private ProductFeature ecken = ProductFeature.create("52",new Money(0.52));
	private ProductFeature[] productFeatures ={butter, ecken};
	
	ProductFeatureType p1 = new ProductFeatureType("p1","",productFeatures);
	ProductFeatureType p2 = new ProductFeatureType("p2","",productFeatures);
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		ProductFeatureType p = new ProductFeatureType(null,"",productFeatures);
		
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullDescription() {
		@SuppressWarnings("unused")
		ProductFeatureType p = new ProductFeatureType("p",null,productFeatures);
	
	}
	

	@Test(expected=ArgumentNullException.class)
	public void testNotNullFeatures() {
		ProductFeature[] pf = null;
		@SuppressWarnings("unused")
		ProductFeatureType p = new ProductFeatureType("p","",pf);
	
	}
	@Test
	public void testEquals1() {
		ProductFeatureType p = p1;
		
		assertEquals(p,p1);
		
	}
	
	@Test
	public void testNotEquals1() {
		ProductFeatureType p = new ProductFeatureType("p1","",productFeatures);
		
		assertNotSame(p, p1);
	}
	
	@Test
	public void testNotEquals2() {
		
		assertNotSame(p2, p1);
	}
	
	@Test
	public void testTypeName(){
		
		assertEquals("p1", p1.getName());
	}

	@Test
	public void testTypeDescription(){
		
		assertEquals("", p1.getDescription());
	}
	
	@Test
	public void testGetPossibleValues(){
		
		 Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
		 
		 for(ProductFeature pf: p1.getPossibleValues()) {
			    if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	     
		 Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
		 m2.put(butter.getName(), butter);
		 m2.put(ecken.getName(), ecken);
		 
		 assertEquals(m1,m2);
		 
	}
	
	@Test
	public void testGetProductFeatureName1(){
		
		assertEquals(p1.getProductFeature("Butter"),butter);
		 
	}
	
	@Test
	public void testGetProductFeatureName2(){
		
		assertNotSame(p1.getProductFeature("52"),butter);
		 
	}
	
	@Test
	public void testGetProductFeatureName3(){
		
		assertEquals(p1.getProductFeature("Reis"),null);
		 
	}

	@Test
	public void testAddProductFeatures1(){
		
		ProductFeature zucker = ProductFeature.create("Zucker", new Money(0.75));
		p1.addFeature(zucker);
		
	    Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
	    	for (ProductFeature pf : p1.getPossibleValues()){
	    		if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	    
	    Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
		m2.put(butter.getName(), butter);
		m2.put(ecken.getName(), ecken);
		m2.put(zucker.getName(), zucker);	   
	      
		assertEquals(m1,m2);
	      
	}
	
	@Test
	public void testAddProductFeatures2(){
		
		p1.addFeature(butter);
		
	    Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
	    	for (ProductFeature pf : p1.getPossibleValues()){
	    		if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	    
	    Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
		m2.put(butter.getName(), butter);
		m2.put(ecken.getName(), ecken);	   
	      
		assertEquals(m1,m2);
	      
	}

	@Test
	public void testRemoveProductFeatures1(){
		
		p1.removeFeature(butter);
		
		Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
    	for (ProductFeature pf : p1.getPossibleValues()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
		m2.put(ecken.getName(), ecken);	   
	    
    	
    	assertEquals(m1,m2);	
		
	}
	
	@Test
	public void testRemoveProductFeatures2(){
		
		ProductFeature zucker = ProductFeature.create("Zucker", new Money(0.75));
		
		p1.removeFeature(butter);
		p1.removeFeature(zucker);
		
		Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
    	for (ProductFeature pf : p1.getPossibleValues()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
		m2.put(ecken.getName(), ecken);	   
	    
    	
    	assertEquals(m1,m2);	
			
	}


	@Test
	public void testPossibleValues(){
		
		ProductFeature zucker = ProductFeature.create("Zucker", new Money(0.75));
		
		p1.removeFeature(butter);
		p1.addFeature(zucker);
		p1.addFeature(butter);
		p1.removeFeature(zucker);
		
		Map <String, ProductFeature> m1 = new HashMap<String, ProductFeature>();
    	for (ProductFeature pf : p1.getPossibleValues()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	Map <String, ProductFeature> m2 = new HashMap<String, ProductFeature>();
    	m2.put(ecken.getName(), ecken);	   
    	m2.put(butter.getName(), butter);	   
	    
    	assertEquals(m1,m2);	
			
	}

}
