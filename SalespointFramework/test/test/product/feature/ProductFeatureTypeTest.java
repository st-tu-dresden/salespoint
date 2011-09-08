package test.product.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature_old;
import org.salespointframework.core.product.features.ProductFeatureType_old;
import org.salespointframework.util.ArgumentNullException;

public class ProductFeatureTypeTest {

	private ProductFeature_old butter = ProductFeature_old.create("Butter",new Money(0.25));
	private ProductFeature_old ecken = ProductFeature_old.create("52",new Money(0.52));
	private ProductFeature_old[] productFeatures ={butter, ecken};
	
	ProductFeatureType_old p1 = new ProductFeatureType_old("p1","",productFeatures);
	ProductFeatureType_old p2 = new ProductFeatureType_old("p2","",productFeatures);
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		ProductFeatureType_old p = new ProductFeatureType_old(null,"",productFeatures);
		
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullDescription() {
		@SuppressWarnings("unused")
		ProductFeatureType_old p = new ProductFeatureType_old("p",null,productFeatures);
	
	}
	

	@Test(expected=ArgumentNullException.class)
	public void testNotNullFeatures() {
		ProductFeature_old[] pf = null;
		@SuppressWarnings("unused")
		ProductFeatureType_old p = new ProductFeatureType_old("p","",pf);
	
	}
	@Test
	public void testEquals1() {
		ProductFeatureType_old p = p1;
		
		assertEquals(p,p1);
		
	}
	
	@Test
	public void testNotEquals1() {
		ProductFeatureType_old p = new ProductFeatureType_old("p1","",productFeatures);
		
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
		
		 Map <String, ProductFeature_old> m1 = new HashMap<String, ProductFeature_old>();
		 
		 for(ProductFeature_old pf: p1.getProductFeatures()) {
			    if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	     
		 Map <String, ProductFeature_old> m2 = new HashMap<String, ProductFeature_old>();
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
		
		ProductFeature_old zucker = ProductFeature_old.create("Zucker", new Money(0.75));
		p1.addFeature(zucker);
		
	    Map <String, ProductFeature_old> m1 = new HashMap<String, ProductFeature_old>();
	    	for (ProductFeature_old pf : p1.getProductFeatures()){
	    		if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	    
	    Map <String, ProductFeature_old> m2 = new HashMap<String, ProductFeature_old>();
		m2.put(butter.getName(), butter);
		m2.put(ecken.getName(), ecken);
		m2.put(zucker.getName(), zucker);	   
	      
		assertEquals(m1,m2);
	      
	}
	
	@Test
	public void testAddProductFeatures2(){
		
		p1.addFeature(butter);
		
	    Map <String, ProductFeature_old> m1 = new HashMap<String, ProductFeature_old>();
	    	for (ProductFeature_old pf : p1.getProductFeatures()){
	    		if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	    
	    Map <String, ProductFeature_old> m2 = new HashMap<String, ProductFeature_old>();
		m2.put(butter.getName(), butter);
		m2.put(ecken.getName(), ecken);	   
	      
		assertEquals(m1,m2);
	      
	}

	@Test
	public void testRemoveProductFeatures1(){
		
		p1.removeFeature(butter.getName());
		
		Map <String, ProductFeature_old> m1 = new HashMap<String, ProductFeature_old>();
    	for (ProductFeature_old pf : p1.getProductFeatures()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	Map <String, ProductFeature_old> m2 = new HashMap<String, ProductFeature_old>();
		m2.put(ecken.getName(), ecken);	   
	    
    	
    	assertEquals(m1,m2);	
		
	}
	
	@Test
	public void testRemoveProductFeatures2(){
		
		ProductFeature_old zucker = ProductFeature_old.create("Zucker", new Money(0.75));
		
		p1.removeFeature(butter.getName());
		p1.removeFeature(zucker.getName());
		
		Map <String, ProductFeature_old> m1 = new HashMap<String, ProductFeature_old>();
    	for (ProductFeature_old pf : p1.getProductFeatures()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	Map <String, ProductFeature_old> m2 = new HashMap<String, ProductFeature_old>();
		m2.put(ecken.getName(), ecken);	   
	    
    	
    	assertEquals(m1,m2);	
			
	}


	@Test
	public void testPossibleValues(){
		
		ProductFeature_old zucker = ProductFeature_old.create("Zucker", new Money(0.75));
		
		p1.removeFeature(butter.getName());
		p1.addFeature(zucker);
		p1.addFeature(butter);
		p1.removeFeature(zucker.getName());
		
		Map <String, ProductFeature_old> m1 = new HashMap<String, ProductFeature_old>();
    	for (ProductFeature_old pf : p1.getProductFeatures()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	Map <String, ProductFeature_old> m2 = new HashMap<String, ProductFeature_old>();
    	m2.put(ecken.getName(), ecken);	   
    	m2.put(butter.getName(), butter);	   
	    
    	assertEquals(m1,m2);	
			
	}

}
