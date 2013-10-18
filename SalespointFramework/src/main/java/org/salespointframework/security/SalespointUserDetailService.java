package org.salespointframework.security;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.salespointframework.core.user.Capability;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.core.user.UserManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//http://docs.spring.io/spring-security/site/docs/3.2.x/reference/html/technical-overview.html
public class SalespointUserDetailService implements UserDetailsService {

	private UserManager userManager;
	
	public SalespointUserDetailService(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException {
		UserIdentifier userIdentifier = new UserIdentifier(name);
		User user = userManager.get(User.class, userIdentifier);

		if(user == null) throw new UsernameNotFoundException("User: " + name + "not found");
		
		return new SalespointUserDetails(user);
	}

	@SuppressWarnings("serial")
	private class SalespointUserDetails implements UserDetails {

		// TODO
		// caching von den Capabilities ist wahrscheinlich nicht so sinnvoll ...
		// nachdenken
		private final String username;
		private final String password;
		private final List<GrantedAuthority> authorities = new LinkedList<>();

		public SalespointUserDetails(User user) {
			this.username = user.getIdentifier().toString();
			this.password = user.getPassword();
			
			for (Capability capability : user.getCapabilities()) {
				authorities
						.add(new SimpleGrantedAuthority(capability.getName()));
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
			return true;
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
			if (other instanceof SalespointUserDetails) {
				return this.username
						.equals(((SalespointUserDetails) other).username);
			}
			return false;
		}

	}

}
