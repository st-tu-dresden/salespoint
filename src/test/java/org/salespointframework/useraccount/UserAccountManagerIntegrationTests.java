package org.salespointframework.useraccount;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Integration tests for {@link PersistentUserAccountManager}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@SuppressWarnings("javadoc")
public class UserAccountManagerIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;
	@Autowired PasswordEncoder passwordEncoder;

	UserAccount userAccount;

	@Before
	public void before() {
		userAccount = userAccountManager.create("userId", "password");
	}

	@Test(expected = IllegalArgumentException.class)
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

		UserAccountIdentifier id = userAccount.getIdentifier();

		userAccount = userAccountManager.save(userAccount);
		userAccountManager.disable(id);

		Optional<UserAccount> result = userAccountManager.get(id);
		assertThat(result.isPresent(), is(true));
		assertThat(result.get().isEnabled(), is(false));
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
		assertThat(userAccountManager.get(userAccount.getIdentifier()).get(), is(userAccount));
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

	@Test
	public void changesPasswordCorrectly() {

		UserAccount acc = userAccountManager.create("Bob", "123", new Role("ROLE_CHEF"));
		userAccountManager.save(acc);

		userAccountManager.changePassword(acc, "asd");

		assertThat(acc.getPassword().isEncrypted(), is(true));
		assertThat(passwordEncoder.matches("asd", acc.getPassword().toString()), is(true));
	}

	/**
	 * @see #46
	 */
	@Test
	public void findsUserByUsername() {

		UserAccount reference = userAccountManager.save(userAccountManager.create("Bob", "123", new Role("ROLE_CHEF")));

		Optional<UserAccount> user = userAccountManager.findByUsername("Bob");

		assertThat(user.isPresent(), is(true));
		assertThat(user.get(), is(reference));
	}
}
