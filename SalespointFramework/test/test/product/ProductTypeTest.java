package test.product;


//FIXME
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature_old;
import org.salespointframework.core.product.features.ProductFeatureType_old;
import org.salespointframework.util.ArgumentNullException;

public class ProductTypeTest {

	private KeksProduct k1 = new KeksProduct("keks", Money.ZERO);
	private KeksProduct k2 = new KeksProduct("keks", Money.ZERO);
	private KeksProduct k3 = new KeksProduct("keks", new Money(20));


	private ProductFeature_old[] p = new ProductFeature_old[2];
	private ProductFeatureType_old Butterkeks = new ProductFeatureType_old("Butterkeks","",p);
	private ProductFeatureType_old Schokokeks = new ProductFeatureType_old("Schokokeks","",p);
	private ProductFeatureType_old Reiskeks = new ProductFeatureType_old("Reiskeks","",p);
	
	@Test
	public void testEquals1() {
		KeksProduct k4 = k1;
		
		assertEquals(k4,k1);
		
	}
	
	@Test
	public void testEquals2() {
		
		assertEquals(k2.getName(),k1.getName());
		assertEquals(k2.getPrice(),k1.getPrice());
	
	}
	
	@Test
	public void testNotEquals1() {
		KeksProduct k4 = new KeksProduct("wurst", new Money(1));
		
		assertNotSame(k4, k1);
	}
	
	@Test
	public void testNotEquals2() {
		
		assertNotSame(k2, k1);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		KeksProduct k = new KeksProduct(null, Money.ZERO);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullPrice() {
		@SuppressWarnings("unused")
		KeksProduct k = new KeksProduct("keks", null);
	}
	
	@Test
	public void testTypeName(){
		
		assertEquals("keks", k1.getName());
	}

	@Test
	public void testTypePrice(){
		
		assertEquals(Money.ZERO, k1.getPrice());
	}
	
	@Test
	public void testEquals(){
	
		KeksProduct k4 = k1;
		
		assertTrue(k1.equals(k1));
		assertTrue(k1.equals(k4));
		assertFalse(k1.equals(k2));
		assertFalse(k1.equals(k3));
	}
	
	/*
	@Test
	public void testGetProductFeatureTypes(){
		
		for(ProductFeatureType_old pf: k1.getProductFeatureTypes()) {
	        System.out.println(pf);
	        
	        assertEquals(pf,null);
		}
		
	}	

	@Test
	public void testAddProductFeatureTypes(){
		
		k1.addProductFeatureType(Butterkeks);
		k1.addProductFeatureType(Schokokeks);
		k1.addProductFeatureType(Reiskeks);
		
	    Map <String, ProductFeatureType_old> m1 = new HashMap<String, ProductFeatureType_old>();
	    	for (ProductFeatureType_old pf : k1.getProductFeatureTypes()){
	    		if (pf!=null){
	    			m1.put(pf.getName(), pf);
	    		}
	    	}
	    
	    Map <String, ProductFeatureType_old> m2 = new HashMap<String, ProductFeatureType_old>();
	      m2.put("Butterkeks", Butterkeks);
	      m2.put("Schokokeks", Schokokeks);
	      m2.put("Reiskeks", Reiskeks);
	      
	      
		assertEquals(m1,m2);
	      
	}
	
	
	@Test
	public void testRemoveProductFeatureType1(){
		
		k1.addProductFeatureType(Butterkeks);
		k1.addProductFeatureType(Reiskeks);
		k1.removeProductFeatureType(Butterkeks);
		
		Map <String, ProductFeatureType_old> m1 = new HashMap<String, ProductFeatureType_old>();
    	for (ProductFeatureType_old pf : k1.getProductFeatureTypes()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	assertEquals(m1.keySet().toString(),"[Reiskeks]");	
		
	}
	
	@Test
	public void testRemoveProductFeatureType2(){
		
		k1.addProductFeatureType(Butterkeks);
		k1.addProductFeatureType(Reiskeks);
		k1.removeProductFeatureType(Schokokeks);
		k1.removeProductFeatureType(Butterkeks);
		
		Map <String, ProductFeatureType_old> m1 = new HashMap<String, ProductFeatureType_old>();
    	for (ProductFeatureType_old pf : k1.getProductFeatureTypes()){
    		if (pf!=null){
    			m1.put(pf.getName(), pf);
    		}
    	}
    	
    	assertEquals(m1.keySet().toString(),"[Reiskeks]");	
	}
	*/
}
