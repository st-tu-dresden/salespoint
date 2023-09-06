/*
 * Copyright 2019-2023 the original author or authors.
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

import lombok.experimental.UtilityClass;

import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;

/**
 * Testing utilities when working with {@link UserAccount}s.
 *
 * @author Oliver Drotbohm
 * @since 7.2
 */
@UtilityClass
public class UserAccountTestUtils {

	public static UnencryptedPassword UNENCRYPTED_PASSWORD = UnencryptedPassword.of("password");

	static EncryptedPassword ENCRYPTED_PASSWORD = EncryptedPassword.of("encrypted");

	/**
	 * Creates a dummy {@link UserAccount}.
	 *
	 * @return
	 */
	public static UserAccount createUserAccount() {
		return new UserAccount(UserAccountIdentifier.of("4711"), ENCRYPTED_PASSWORD);
	}
}
