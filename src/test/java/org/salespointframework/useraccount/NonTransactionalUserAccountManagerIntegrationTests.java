/*
 * Copyright 2018-2022 the original author or authors.
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for {@link UserAccountManager} that do not run in a transaction by default.
 *
 * @author Oliver Gierke
 */
@ModuleTest
@TestPropertySource(properties = "salespoint.authentication.login-via-email=true")
class NonTransactionalUserAccountManagerIntegrationTests {

	private static final UnencryptedPassword PASSWORD = UserAccountTestUtils.UNENCRYPTED_PASSWORD;

	@Autowired UserAccountManagement users;
	@Autowired UserAccountRepository repository;

	@AfterEach
	void cleanUp() {
		repository.deleteAll();
	}

	@Test // #222
	void rejectsDifferentUserWithSameEmail() {

		var email = "foo@bar.com";

		users.create("username", PASSWORD, email);

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> users.create("someOtherUsername", PASSWORD, email));
	}

	@Test // #222 - see property set on class level
	void rejectsUserWithoutEmailAddress() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> users.create("username", PASSWORD)) //
				.withMessageContaining("login via email");
	}

	@Test // #218
	void userAccountsCanBeDeleted() {

		var reference = users.create("username", PASSWORD, "foo@bar.de");
		assertThat(users.findByUsername(reference.getUsername())).hasValue(reference);

		users.delete(reference);

		assertThat(users.findByUsername(reference.getUsername())).isEmpty();
	}
}
