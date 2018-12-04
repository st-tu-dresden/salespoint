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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * {@link AuthenticationManager} using the current SpringSecurity {@link Authentication} to lookup a {@link UserAccount}
 * by the identifier of it.
 * 
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor
class SpringSecurityAuthenticationManager implements AuthenticationManager, UserDetailsService {

	private final @NonNull UserAccountRepository repository;
	private final @NonNull PasswordEncoder passwordEncoder;
	private final @NonNull AuthenticationProperties config;

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.AuthenticationManager#getCurrentUser()
	 */
	@Override
	public Optional<UserAccount> getCurrentUser() {

		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()) //
				.map(Authentication::getName) //
				.map(UserAccountIdentifier::new) //
				.flatMap(repository::findById);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.AuthenticationManager#matches(org.salespointframework.useraccount.Password, org.salespointframework.useraccount.Password)
	 */
	@Override
	public boolean matches(Password candidate, Password existing) {

		Assert.notNull(existing, "Existing password must not be null!");

		return Optional.ofNullable(candidate).//
				map(c -> passwordEncoder.matches(c.getPassword(), existing.getPassword())).//
				orElse(false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Optional<UserAccount> candidate = config.isLoginViaEmail() //
				? repository.findByEmail(name) //
				: repository.findById(new UserAccountIdentifier(name));

		return new UserAccountDetails(
				candidate.orElseThrow(() -> new UsernameNotFoundException("Useraccount: " + name + "not found")));
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	@SuppressWarnings("serial")
	static class UserAccountDetails implements UserDetails {

		private final String username;
		private final String password;
		private final boolean isEnabled;
		private final Collection<? extends GrantedAuthority> authorities;

		public UserAccountDetails(UserAccount userAccount) {

			this.username = userAccount.getUsername();
			this.password = userAccount.getPassword().toString();
			this.isEnabled = userAccount.isEnabled();
			this.authorities = userAccount.getRoles() //
					.map(role -> new SimpleGrantedAuthority(role.getName())) //
					.toList();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}
	}
}
