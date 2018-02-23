/*
 * Copyright 2017 the original author or authors.
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
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * {@link UserDetailsService} implementation using the {@link UserAccountRepository} to obtain user information for
 * login.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @see http://docs.spring.io/spring-security/site/docs/3.2.x/reference/html/technical-overview.html
 */
@Service
@RequiredArgsConstructor
class UserAccountDetailService implements UserDetailsService {

	private final @NonNull UserAccountRepository repository;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Optional<UserAccount> candidate = repository.findById(new UserAccountIdentifier(name));

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
			this.authorities = userAccount.getRoles().stream().//
					map(role -> new SimpleGrantedAuthority(role.getName())).//
					collect(Collectors.toList());
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
