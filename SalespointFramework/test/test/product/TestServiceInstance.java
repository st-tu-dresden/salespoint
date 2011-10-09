package test.product;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.salespointframework.core.product.later.PersistentServiceInstance;
import org.salespointframework.core.product.later.ServiceType;


@SuppressWarnings("javadoc")
@Entity
public class TestServiceInstance extends PersistentServiceInstance{

	@Deprecated
	public TestServiceInstance(){}
	
	public TestServiceInstance(ServiceType serviceType, DateTime start, DateTime end) {
		super(serviceType, start, end);
		}
}
