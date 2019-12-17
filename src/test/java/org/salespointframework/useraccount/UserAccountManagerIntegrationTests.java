/*
 * Copyright 2017-2019 the original author or authors.
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

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
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

	private static final UnencryptedPassword PASSWORD = UserAccountTestUtils.UNENCRYPTED_PASSWORD;

	@Autowired UserAccountManager userAccountManager;
	@Autowired PasswordEncoder passwordEncoder;

	@Test
	void rejectsSavingNullUserAccount() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> userAccountManager.save(null));
	}

	@Test
	void testAddContains() {

		UserAccount account = createAccount();

		assertThat(userAccountManager.contains(account.getId())).isTrue();
	}

	@Test
	void disablesUserAccount() {

		UserAccount account = createAccount();
		UserAccountIdentifier id = account.getId();

		userAccountManager.disable(id);

		Optional<UserAccount> result = userAccountManager.get(id);

		assertThat(result).hasValueSatisfying(it -> {
			assertThat(it.isEnabled()).isFalse();
		});
	}

	@Test
	void returnsAllExistingUserAccounts() {

		UserAccount alice = createAccount("Alice");
		UserAccount bob = createAccount("Bob");

		Streamable<UserAccount> customers = userAccountManager.findAll();

		assertThat(customers).containsExactlyInAnyOrder(alice, bob);
	}

	@Test
	void findsUserAccountById() {

		UserAccount account = createAccount();

		assertThat(userAccountManager.get(account.getId())).hasValue(account);
	}

	@Test
	void encryptsPlainTextPassword() {

		UserAccount account = createAccount();
		assertThat(account.getPassword()).isInstanceOf(EncryptedPassword.class);
	}

	@Test
	void doesNotReEncryptEncryptedPassword() {

		UserAccount account = createAccount();

		EncryptedPassword encryptedPassword = EncryptedPassword.of("encrypted");
		account.setPassword(encryptedPassword);

		UserAccount result = userAccountManager.save(account);

		assertThat(result.getPassword()).isEqualTo(encryptedPassword);
	}

	@Test
	void changesPasswordCorrectly() {

		UserAccount acc = userAccountManager.create("Bob", PASSWORD, Role.of("ROLE_CHEF"));

		userAccountManager.changePassword(acc, UnencryptedPassword.of("asd"));

		assertThat(acc.getPassword()).isInstanceOf(EncryptedPassword.class);
		assertThat(passwordEncoder.matches("asd", acc.getPassword().asString())).isTrue();
	}

	@Test // #46
	void findsUserByUsername() {

		UserAccount reference = userAccountManager.create("Bob", PASSWORD, Role.of("ROLE_CHEF"));

		Optional<UserAccount> user = userAccountManager.findByUsername("Bob");

		assertThat(user).hasValue(reference);
	}

	@Test // #55
	void rejectsCreationOfUserWithExistingUsername() {

		userAccountManager.create("username", PASSWORD);

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> userAccountManager.create("username", PASSWORD));
	}

	private UserAccount createAccount() {
		return createAccount("userId");
	}

	private UserAccount createAccount(String username) {
		return userAccountManager.create(username, PASSWORD);
	}
}
