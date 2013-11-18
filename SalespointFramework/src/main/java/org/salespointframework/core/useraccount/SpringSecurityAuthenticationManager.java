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
package org.salespointframework.core.useraccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * {@link AuthenticationManager} using the current SpringSecurity {@link Authentication} to lookup a {@link UserAccount}
 * by the identifier of it.
 * 
 * @author Oliver Gierke
 */
@Component
class SpringSecurityAuthenticationManager implements AuthenticationManager {

	private final UserAccountRepository repository;

	/**
	 * Creates a new {@link SpringSecurityAuthenticationManager} using the given {@link UserAccountRepository}.
	 * 
	 * @param repository must not be {@literal null}.
	 */
	@Autowired
	public SpringSecurityAuthenticationManager(UserAccountRepository repository) {

		Assert.notNull(repository, "UserAccountRepository must not be null!");
		this.repository = repository;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.core.useraccount.AuthenticationManager#getCurrentUser()
	 */
	@Override
	public UserAccount getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return null;
		}

		UserAccountIdentifier userAccountIdentifier = new UserAccountIdentifier(authentication.getName());
		return repository.findOne(userAccountIdentifier);
	}
}
