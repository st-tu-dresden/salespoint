package test.time;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.time.DeLoreanTime;

public class TestTime extends DeLoreanTime {
	
	@Override
	public void beforeGoAhead(Duration d) {
		// TODO irgendwelche Aktionen ausführen
	}
	
	@Override
	public void afterGoAhead(Duration d) {
		// TODO irgendwelche Aktionen ausführen
	}
	
	@Test 
	public void testGetDateTime() {
		
		assertEquals(getDateTime().getSecondOfDay(),Shop.INSTANCE.getTime().getDateTime().getSecondOfDay());
	}
	
	@Test
	public void testAfterGoAhead(){
		
		DateTime a = new DateTime();
		Duration d = new Duration(300000);
		afterGoAhead(d);
		
		//assertEquals(Shop.INSTANCE.getTime().getDateTime().plus(300000).getSecondOfDay(), getDateTime().getSecondOfDay());
		System.out.println(getDateTime());
		afterGoAhead(d);
		
		System.out.println(Shop.INSTANCE.getTime().getDateTime().plus(30000).getMillis());
	}

}
