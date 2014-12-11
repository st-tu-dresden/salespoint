/*
 * Copyright 2013 the original author or authors.
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

import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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
public class UserAccountRepositoryIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserAccountRepository repository;

	UserAccount firstUser, secondUser;

	@Before
	public void setUp() {

		this.firstUser = createAccount();
		this.firstUser.setPassword(new Password("encrypted", true));
		this.firstUser = repository.save(firstUser);

		this.secondUser = createAccount();
		this.secondUser.setPassword(new Password("encrypted2", true));
		this.secondUser.setEnabled(false);
		this.secondUser = repository.save(secondUser);
	}

	@Ignore
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void preventsUnencryptedPasswordsFromBeingPersisted() {
		repository.save(createAccount());
	}

	@Test
	public void findsEnabledUsers() {

		Iterable<UserAccount> result = repository.findByEnabledTrue();
		assertThat(result, is(Matchers.<UserAccount> iterableWithSize(1)));
		assertThat(result, hasItem(firstUser));
	}

	@Test
	public void findsDisabledUsers() {

		Iterable<UserAccount> result = repository.findByEnabledFalse();
		assertThat(result, is(Matchers.<UserAccount> iterableWithSize(1)));
		assertThat(result, hasItem(secondUser));
	}

	/**
	 * @see #55
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void testname() {

		UserAccount account = new UserAccount(firstUser.getIdentifier(), "someotherPassword");

		repository.save(account);
	}

	static UserAccount createAccount() {
		UserAccountIdentifier identifier = new UserAccountIdentifier(UUID.randomUUID().toString());
		return new UserAccount(identifier, "password", new Role("USER"));
	}
}
