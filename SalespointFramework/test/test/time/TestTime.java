package test.time;

import static org.junit.Assert.assertEquals;

import org.joda.time.Duration;
import org.junit.Test;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.time.DeLoreanTime;

@SuppressWarnings("javadoc")
public class TestTime extends DeLoreanTime {
	
	@Override
	public void beforeForward(Duration d) {
		// TODO irgendwelche Aktionen ausführen
	}
	
	@Override
	public void afterForward(Duration d) {
		// TODO irgendwelche Aktionen ausführen
	}
	
	@Test 
	public void testGetDateTime() {
		
		assertEquals(getDateTime().getSecondOfDay(),Shop.INSTANCE.getTime().getDateTime().getSecondOfDay());
	}
	
	@Test
	public void testAfterGoAhead(){
		

		Duration d = new Duration(300000);
		afterForward(d);
		
		//assertEquals(Shop.INSTANCE.getTime().getDateTime().plus(300000).getSecondOfDay(), getDateTime().getSecondOfDay());
		System.out.println(getDateTime());
		afterForward(d);
		
		System.out.println(Shop.INSTANCE.getTime().getDateTime().plus(30000).getMillis());
	}

}
