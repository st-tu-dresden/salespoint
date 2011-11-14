package test.product;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.service.PersistentService;

@SuppressWarnings("javadoc")
@Entity
public class TestServiceType extends PersistentService{

		@Deprecated
		protected TestServiceType() {
			
		}
		
		public TestServiceType(String name, Money price) {
			super(name, price);
		}
		
		public TestServiceType(String name, Money price, DateTime start, DateTime end){
			super(name, price, start, end);
		}

}
