/*
 * Copyright 2018-2023 the original author or authors.
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
package org.salespointframework.useraccount.web;

import lombok.experimental.UtilityClass;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * Utilities to be able to test user account module.
 *
 * @author Oliver Gierke
 */
@UtilityClass
public class UserAccountWebTestUtils {

	/**
	 * Returns the type of the {@link HandlerMethodArgumentResolver} resolving {@link LoggedIn} {@link UserAccount}s.
	 *
	 * @return
	 */
	public static Class<?> getLoggedInArgumentResolverType() {
		return LoggedInUserAccountArgumentResolver.class;
	}
}
