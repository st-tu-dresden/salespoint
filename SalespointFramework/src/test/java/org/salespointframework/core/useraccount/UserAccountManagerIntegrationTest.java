package org.salespointframework.core.useraccount;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * Integration tests for {@link PersistentUserAccountManager}.
 *
 *@author Paul Henke
 * @author Oliver Gierke
 */
@SuppressWarnings("javadoc")
@Transactional
public class UserAccountManagerIntegrationTest extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;
	
	UserAccountIdentifier userAccountIdentifier;
	UserAccount userAccount;
	
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
		assertThat(userAccountManager.contains(userAccount.getIdentifier()), is(true));
	}
	
	@Test
	public void testDisable() {
		
		userAccountManager.save(userAccount);
		userAccountManager.disable(userAccount.getIdentifier());
		
		assertThat(userAccountManager.get(userAccount.getIdentifier()).isEnabled(), is(false));
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
		assertThat(userAccountManager.get( userAccount.getIdentifier()), is(userAccount));
	}
	
	@Test
	public void encryptsPlainTextPassword() {
		
		UserAccount account = userAccountManager.save(userAccount);
		Password password = account.getPassword();
		assertThat(password.isEncrypted(), is(true));
	}
	
	@Test
	public void doesNotReEncryptExncryptedPassword() {
		
		UserAccount account = userAccountManager.save(userAccount);
		Password encryptedPassword = new Password("encrypted", true);
		account.setPassword(encryptedPassword);
		
		UserAccount result = userAccountManager.save(account);
		assertThat(result.getPassword(), is(encryptedPassword));
	}
}
