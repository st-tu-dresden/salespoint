package test.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;

@SuppressWarnings("javadoc")
public class ChargeLineTest {

	private User user;
	@SuppressWarnings("rawtypes")
	private Order order;
	private ChargeLine chargeLine;
	 
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Before
	public void before() {
		user = new PersistentUser(new UserIdentifier(), "");
		order = new PersistentOrder(user.getIdentifier(), Cash.CASH);
		chargeLine = new ChargeLine(Money.ZERO, "gaaar nix");
	}
	
	@Test(expected=ArgumentNullException.class)
	public void nullTest() {
		order.addChargeLine(null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void nullTest2() {
		order.removeChargeLine(null);
	}
	
	@Test
	public void addTest() {
		assertTrue(order.addChargeLine(chargeLine));
	}
	
	@Test
	public void addTest2() {
		order.addChargeLine(chargeLine);
		assertFalse(order.addChargeLine(chargeLine));
	}
	
	@Test
	public void removeTest() {
		order.addChargeLine(chargeLine);
		assertTrue(order.removeChargeLine(chargeLine.getIdentifier()));
	}
	
	@Test
	public void removeTest2() {
		assertFalse(order.removeChargeLine(chargeLine.getIdentifier()));
	}
	
	@Test
	public void foo() {
		order.addChargeLine(chargeLine);
		Iterable<ChargeLine> iter = order.getChargeLines();
		for(ChargeLine c : iter) {
			System.out.println(c.getIdentifier());
		}
	}
}
