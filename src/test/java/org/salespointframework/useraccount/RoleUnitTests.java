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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link Role}.
 *
 * @author Oliver Drotbohm
 */
class RoleUnitTests {

	@MethodSource("nameToAuthority") // #280
	@ParameterizedTest(name = "Role named {0} produces authority {1}")
	void prefixesAuthorityWithRoleUnderscore(String name, String authority) {
		assertThat(Role.of(name).toAuthority()).isEqualTo(authority);
	}

	private static String[][] nameToAuthority() {

		return new String[][] { //
				new String[] { "CUSTOMER", "ROLE_CUSTOMER" }, //
				new String[] { "ROLE_CUSTOMER", "ROLE_CUSTOMER" } //
		};
	}
}
