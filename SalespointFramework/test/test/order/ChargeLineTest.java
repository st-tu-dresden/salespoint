package test.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.PersistentChargeLine;
import org.salespointframework.core.order.PersistentOrder;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;

@SuppressWarnings("javadoc")
public class ChargeLineTest {

	private PersistentUser user;
	private PersistentOrder order;
	private PersistentChargeLine chargeLine;
	 
	@BeforeClass
	public static void beforeClass() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("deprecation")
	@Before
	public void before() {
		user = new PersistentUser(new UserIdentifier(), "");
		order = new PersistentOrder(user.getIdentifier());
		chargeLine = new PersistentChargeLine(Money.ZERO, "gaaar nix");
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
}
