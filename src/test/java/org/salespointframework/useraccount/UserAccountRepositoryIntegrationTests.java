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

import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link UserAccountRepository}.
 * 
 * @author Oliver Gierke
 */
@Transactional
class UserAccountRepositoryIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserAccountRepository repository;

	UserAccount firstUser, secondUser;

	@BeforeEach
	void setUp() {

		this.firstUser = createAccount();
		this.firstUser.setPassword(Password.encrypted("encrypted"));
		this.firstUser = repository.save(firstUser);

		this.secondUser = createAccount();
		this.secondUser.setPassword(Password.encrypted("encrypted2"));
		this.secondUser.setEnabled(false);
		this.secondUser = repository.save(secondUser);
	}

	@Test
	void preventsUnencryptedPasswordsFromBeingPersisted() {

		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class) //
				.isThrownBy(() -> repository.save(createAccount()));
	}

	@Test
	void findsEnabledUsers() {

		Iterable<UserAccount> result = repository.findByEnabledTrue();

		assertThat(result, is(Matchers.<UserAccount> iterableWithSize(1)));
		assertThat(result, hasItem(firstUser));
	}

	@Test
	void findsDisabledUsers() {

		Iterable<UserAccount> result = repository.findByEnabledFalse();

		assertThat(result, is(Matchers.<UserAccount> iterableWithSize(1)));
		assertThat(result, hasItem(secondUser));
	}

	@Test // #55
	void rejectsUserAccountWithSameUsername() {

		UserAccount userAccount = new UserAccount(firstUser.getId(), "unencrypted");
		userAccount.setPassword(Password.encrypted("encrypted"));

		assertThatExceptionOfType(DataIntegrityViolationException.class) //
				.isThrownBy(() -> repository.save(userAccount));
	}

	@Test // #222
	void looksUpUserViaEmail() {

		UserAccount account = new UserAccount(new UserAccountIdentifier("username"), "password");
		account.setPassword(Password.encrypted("encrypted"));
		account.setEmail("foo@bar.com");

		UserAccount reference = repository.save(account);

		assertThat(repository.findByEmail("foo@bar.com")).hasValue(reference);
	}

	@Test // #222
	void rejectsMultipleUsersWithTheSameEmailAddress() {

		String email = "foo@bar.com";

		UserAccount first = createAccount();
		first.setEmail(email);
		first.setPassword(Password.encrypted("encrypted"));

		repository.save(first);

		UserAccount second = createAccount();
		second.setEmail(email);
		second.setPassword(Password.encrypted("encrypted"));

		assertThatExceptionOfType(DataIntegrityViolationException.class) //
				.isThrownBy(() -> {

					// Persist second user
					repository.save(second);

					// Flush to trigger insert
					repository.findByEmail(email);
				});
	}

	static UserAccount createAccount() {

		UserAccountIdentifier identifier = new UserAccountIdentifier(UUID.randomUUID().toString());
		return new UserAccount(identifier, "password", Role.of("USER"));
	}
}
