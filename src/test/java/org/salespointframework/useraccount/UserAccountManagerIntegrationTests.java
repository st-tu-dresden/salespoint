/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		assertThat(userAccountManager.contains(userAccount.getId()), is(true));
	}

	@Test
	public void testDisable() {

		UserAccountIdentifier id = userAccount.getId();

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
		assertThat(userAccountManager.get(userAccount.getId()).get(), is(userAccount));
	}

	@Test
	public void encryptsPlainTextPassword() {

		UserAccount account = userAccountManager.save(userAccount);
		Password password = account.getPassword();
		assertThat(password.isEncrypted(), is(true));
	}

	@Test
	public void doesNotReEncryptEncryptedPassword() {

		UserAccount account = userAccountManager.save(userAccount);
		Password encryptedPassword = Password.encrypted("encrypted");
		account.setPassword(encryptedPassword);

		UserAccount result = userAccountManager.save(account);
		assertThat(result.getPassword(), is(encryptedPassword));
	}

	@Test
	public void changesPasswordCorrectly() {

		UserAccount acc = userAccountManager.create("Bob", "123", Role.of("ROLE_CHEF"));

		userAccountManager.changePassword(acc, "asd");

		assertThat(acc.getPassword().isEncrypted(), is(true));
		assertThat(passwordEncoder.matches("asd", acc.getPassword().toString()), is(true));
	}

	@Test // #46
	public void findsUserByUsername() {

		UserAccount reference = userAccountManager.create("Bob", "123", Role.of("ROLE_CHEF"));

		Optional<UserAccount> user = userAccountManager.findByUsername("Bob");

		assertThat(user.isPresent(), is(true));
		assertThat(user.get(), is(reference));
	}

	@Test(expected = IllegalArgumentException.class) // #55
	public void rejectsCreationOfUserWithExistingUsername() {

		userAccountManager.create("username", "password");
		userAccountManager.create("username", "password");
	}
}
