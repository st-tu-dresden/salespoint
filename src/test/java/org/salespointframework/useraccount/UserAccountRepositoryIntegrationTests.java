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

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link UserAccountRepository}.
 *
 * @author Oliver Gierke
 */
@Transactional
@ModuleTest
class UserAccountRepositoryIntegrationTests {

	private static final EncryptedPassword PASSWORD = UserAccountTestUtils.ENCRYPTED_PASSWORD;

	@Autowired UserAccountRepository repository;

	UserAccount firstUser, secondUser;

	@BeforeEach
	void setUp() {

		this.firstUser = createAccount(EncryptedPassword.of("encrypted"));
		this.firstUser = repository.save(firstUser);

		this.secondUser = createAccount(EncryptedPassword.of("encrypted2"));
		this.secondUser.setEnabled(false);
		this.secondUser = repository.save(secondUser);
	}

	@Test
	void findsEnabledUsers() {
		assertThat(repository.findByEnabledTrue()).containsExactly(firstUser);
	}

	@Test
	void findsDisabledUsers() {
		assertThat(repository.findByEnabledFalse()).containsExactly(secondUser);
	}

	@Test // #55
	void rejectsUserAccountWithSameUsername() {

		UserAccount userAccount = new UserAccount(firstUser.getId(), PASSWORD);

		assertThatExceptionOfType(DataIntegrityViolationException.class) //
				.isThrownBy(() -> repository.save(userAccount));
	}

	@Test // #222
	void looksUpUserViaEmail() {

		UserAccount account = new UserAccount(UserAccountIdentifier.of("username"), PASSWORD);
		account.setEmail("foo@bar.com");

		UserAccount reference = repository.save(account);

		assertThat(repository.findByEmail("foo@bar.com")).hasValue(reference);
	}

	@Test // #222
	void rejectsMultipleUsersWithTheSameEmailAddress() {

		String email = "foo@bar.com";

		UserAccount first = createAccount();
		first.setEmail(email);

		repository.save(first);

		UserAccount second = createAccount();
		second.setEmail(email);

		assertThatExceptionOfType(DataIntegrityViolationException.class) //
				.isThrownBy(() -> {

					// Persist second user
					repository.save(second);

					// Flush to trigger insert
					repository.findByEmail(email);
				});
	}

	static UserAccount createAccount() {
		return createAccount(PASSWORD);
	}

	static UserAccount createAccount(EncryptedPassword encryptedPassword) {

		UserAccountIdentifier identifier = UserAccountIdentifier.of(UUID.randomUUID().toString());
		return new UserAccount(identifier, encryptedPassword, Role.of("USER"));
	}
}
