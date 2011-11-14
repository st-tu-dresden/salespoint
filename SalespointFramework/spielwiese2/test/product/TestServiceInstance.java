package test.product;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.salespointframework.core.product.service.PersistentServiceInstance;
import org.salespointframework.core.product.service.Service;


@SuppressWarnings("javadoc")
@Entity
public class TestServiceInstance extends PersistentServiceInstance{

	@Deprecated
	public TestServiceInstance(){}
	
	public TestServiceInstance(Service serviceType, DateTime start, DateTime end) {
		super(serviceType, start, end);
		}
}
