package org.salespointframework.useraccount;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * {@link UserDetailsService} implementation using the {@link UserAccountRepository} to obtain user information for
 * login.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @see http://docs.spring.io/spring-security/site/docs/3.2.x/reference/html/technical-overview.html
 */
@Service
class UserAccountDetailService implements UserDetailsService {

	private final UserAccountRepository repository;

	/**
	 * Creates a new {@link UserAccountDetailService} using the given {@link UserAccountRepository}.
	 * 
	 * @param repository must not be {@literal null}.
	 */
	@Autowired
	public UserAccountDetailService(UserAccountRepository repository) {

		Assert.notNull(repository, "UserAccountRepository must not be null!");
		this.repository = repository;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		
		Optional<UserAccount> candidate = repository.findOne(new UserAccountIdentifier(name));
		UserAccount account = candidate.orElseThrow(() -> new UsernameNotFoundException("Useraccount: " + name + "not found"));
		
		return new UserAccountDetails(account);
	}

	@SuppressWarnings("serial")
	static class UserAccountDetails implements UserDetails {

		private final String username;
		private final String password;
		private final boolean isEnabled;
		private final List<GrantedAuthority> authorities = new LinkedList<>();

		public UserAccountDetails(UserAccount userAccount) {
			
			this.username = userAccount.getIdentifier().toString();
			this.password = userAccount.getPassword().toString();
			this.isEnabled = userAccount.isEnabled();

			for (Role role : userAccount.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getUsername() {
			return username;
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

		@Override
		public boolean isEnabled() {
			return isEnabled;
		}

		@Override
		public String toString() {
			return username;
		}

		@Override
		public int hashCode() {
			return username.hashCode();
		}

		@Override
		public final boolean equals(Object other) {
			if (other == null) {
				return false;
			}
			if (other == this) {
				return true;
			}
			if (other instanceof UserAccountDetails) {
				return this.username.equals(((UserAccountDetails) other).username);
			}
			return false;
		}
	}
}
