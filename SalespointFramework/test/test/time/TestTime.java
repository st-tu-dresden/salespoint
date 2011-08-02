package test.time;

import org.joda.time.Duration;
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

}
