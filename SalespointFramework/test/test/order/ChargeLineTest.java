package test.order;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.PersistentChargeLine;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
public class ChargeLineTest {

	@BeforeClass
	public static void setUp() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Test
	public void chargeLineTest() {
		PersistentUser user = new PersistentUser(new UserIdentifier(), "");
		PersistentOrder order = new PersistentOrder(user.getIdentifier());
		PersistentChargeLine c = new PersistentChargeLine(Money.ZERO, "gaaar nix");
		order.addChargeLine(c);
		
		PersistentOrderManager om = new PersistentOrderManager();
		om.add(order);
		
	}
}
