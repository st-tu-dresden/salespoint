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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import de.olivergierke.moduliths.test.ModuleTest;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link PersistentUserAccountManager}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest
class UserAccountManagerIntegrationTests {

	@Autowired UserAccountManager userAccountManager;
	@Autowired PasswordEncoder passwordEncoder;

	UserAccount userAccount;

	@BeforeEach
	void before() {
		userAccount = userAccountManager.create("userId", "password");
	}

	@Test
	void testAddNull() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> userAccountManager.save(null));
	}

	@Test
	void testAddContains() {

		userAccountManager.save(userAccount);
		assertThat(userAccountManager.contains(userAccount.getId()), is(true));
	}

	@Test
	void testDisable() {

		UserAccountIdentifier id = userAccount.getId();

		userAccount = userAccountManager.save(userAccount);
		userAccountManager.disable(id);

		Optional<UserAccount> result = userAccountManager.get(id);
		assertThat(result.isPresent(), is(true));
		assertThat(result.get().isEnabled(), is(false));
	}

	@Test
	void testFind() {

		userAccountManager.save(userAccount);
		Iterable<UserAccount> customers = userAccountManager.findAll();

		assertThat(customers, is(Matchers.<UserAccount> iterableWithSize(1)));
		assertThat(customers, hasItem(userAccount));
	}

	@Test
	void testGet() {

		userAccountManager.save(userAccount);
		assertThat(userAccountManager.get(userAccount.getId()).get(), is(userAccount));
	}

	@Test
	void encryptsPlainTextPassword() {

		UserAccount account = userAccountManager.save(userAccount);
		Password password = account.getPassword();
		assertThat(password.isEncrypted(), is(true));
	}

	@Test
	void doesNotReEncryptEncryptedPassword() {

		UserAccount account = userAccountManager.save(userAccount);
		Password encryptedPassword = Password.encrypted("encrypted");
		account.setPassword(encryptedPassword);

		UserAccount result = userAccountManager.save(account);
		assertThat(result.getPassword(), is(encryptedPassword));
	}

	@Test
	void changesPasswordCorrectly() {

		UserAccount acc = userAccountManager.create("Bob", "123", Role.of("ROLE_CHEF"));

		userAccountManager.changePassword(acc, "asd");

		assertThat(acc.getPassword().isEncrypted(), is(true));
		assertThat(passwordEncoder.matches("asd", acc.getPassword().toString()), is(true));
	}

	@Test // #46
	void findsUserByUsername() {

		UserAccount reference = userAccountManager.create("Bob", "123", Role.of("ROLE_CHEF"));

		Optional<UserAccount> user = userAccountManager.findByUsername("Bob");

		assertThat(user.isPresent(), is(true));
		assertThat(user.get(), is(reference));
	}

	@Test // #55
	void rejectsCreationOfUserWithExistingUsername() {

		userAccountManager.create("username", "password");

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> userAccountManager.create("username", "password"));
	}

	@Test // #218
	void userAccountsCanBeDeleted() {

		UserAccount reference = userAccountManager.create("username", "password");
		assertThat(userAccountManager.findByUsername(reference.getUsername())).hasValue(reference);

		userAccountManager.delete(reference);
		assertThat(userAccountManager.findByUsername(reference.getUsername())).isEmpty();
	}
}
