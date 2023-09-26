/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.useraccount;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link PersistentUserAccountManagement}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Transactional
@ApplicationModuleTest
class UserAccountManagerIntegrationTests {

	private static final UnencryptedPassword PASSWORD = UserAccountTestUtils.UNENCRYPTED_PASSWORD;

	@Autowired UserAccountManagement users;
	@Autowired PasswordEncoder passwordEncoder;

	@Test
	void rejectsSavingNullUserAccount() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> users.save(null));
	}

	@Test
	void testAddContains() {

		var account = createAccount();

		assertThat(users.contains(account.getId())).isTrue();
	}

	@Test
	void disablesUserAccount() {

		var account = createAccount();
		var id = account.getId();

		users.disable(id);

		assertThat(users.get(id)).hasValueSatisfying(it -> {
			assertThat(it.isEnabled()).isFalse();
		});
	}

	@Test
	void returnsAllExistingUserAccounts() {

		var alice = createAccount("Alice");
		var bob = createAccount("Bob");

		assertThat(users.findAll()).containsExactlyInAnyOrder(alice, bob);
	}

	@Test
	void findsUserAccountById() {

		var account = createAccount();

		assertThat(users.get(account.getId())).hasValue(account);
	}

	@Test
	void encryptsPlainTextPassword() {

		var account = createAccount();

		assertThat(account.getPassword()).isInstanceOf(EncryptedPassword.class);
	}

	@Test
	void doesNotReEncryptEncryptedPassword() {

		var encryptedPassword = EncryptedPassword.of("encrypted");

		var account = createAccount().setPassword(encryptedPassword);

		assertThat(users.save(account).getPassword()).isEqualTo(encryptedPassword);
	}

	@Test
	void changesPasswordCorrectly() {

		var acc = users.create("Bob", PASSWORD, Role.of("ROLE_CHEF"));

		users.changePassword(acc, UnencryptedPassword.of("asd"));

		assertThat(acc.getPassword()).isInstanceOf(EncryptedPassword.class);
		assertThat(passwordEncoder.matches("asd", acc.getPassword().asString())).isTrue();
	}

	@Test // #46
	void findsUserByUsername() {

		var reference = users.create("Bob", PASSWORD, List.of(Role.of("ROLE_CHEF")));

		var user = users.findByUsername("Bob");

		assertThat(user).hasValue(reference);
	}

	@Test // #55
	void rejectsCreationOfUserWithExistingUsername() {

		users.create("username", PASSWORD);

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> users.create("username", PASSWORD));
	}

	@Test // #313
	void publishesUserAccountCreatedEventOnCreation(PublishedEvents events) {

		var account = users.create("username", PASSWORD);

		assertThat(events.ofType(UserAccountCreated.class)) //
				.hasSize(1) //
				.extracting(UserAccountCreated::getUserAccountIdentifier)
				.anyMatch(account::hasId);
	}

	@Test // #55
	void rejectsChangingUserNameToOneOfExistingAccount() {

		users.create("username", PASSWORD);

		var other = users.create("other username", PASSWORD);
		other.setUsername("username");

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> users.save(other));
	}

	private UserAccount createAccount() {
		return createAccount("userId");
	}

	private UserAccount createAccount(String username) {
		return users.create(username, PASSWORD);
	}
}
