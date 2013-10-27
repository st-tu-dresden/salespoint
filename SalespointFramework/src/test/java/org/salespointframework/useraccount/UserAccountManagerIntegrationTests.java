package org.salespointframework.useraccount;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


// FIXME
@SuppressWarnings("javadoc")
@Transactional
public class UserAccountManagerIntegrationTests extends AbstractIntegrationTests {

	@Autowired
	private UserAccountManager userAccountManager;
	
	private UserAccountIdentifier userAccountIdentifier;
	private UserAccount userAccount;
	
	@Before
	public void before() {
		userAccountIdentifier = new UserAccountIdentifier();
		userAccount = userAccountManager.create(userAccountIdentifier, "");
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNull() {
		userAccountManager.save(null);
		
	}
	
	@Test
	public void testAddContains() {
		userAccountManager.save(userAccount);
		assertTrue(userAccountManager.contains(userAccount.getIdentifier()));
	}
	
	@Test
	public void testDisable() {
		userAccountManager.save(userAccount);
		userAccountManager.disable(userAccount.getIdentifier());
		
		assertFalse(userAccount.isEnabled());
		
	}
	
	@Test
	public void testFind() {
		userAccountManager.save(userAccount);
		Iterable<UserAccount> customers = userAccountManager.findAll();
		
		assertThat(customers, is(Matchers.<UserAccount> iterableWithSize(1)));
		assertThat(customers, hasItem(userAccount));
	}

	
	@Test
	public void testGet() {
		userAccountManager.save(userAccount);
		assertEquals(userAccount, userAccountManager.get( userAccount.getIdentifier()));
	}

}
