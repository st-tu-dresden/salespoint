package org.salespointframework.useraccount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
class SpringSecurityAuthenticationManager implements AuthenticationManager {

	private final @NonNull UserAccountRepository repository;
	private final @NonNull PasswordEncoder passwordEncoder;

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.AuthenticationManager#getCurrentUser()
	 */
	@Override
	public Optional<UserAccount> getCurrentUser() {

		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).//
				flatMap(authentication -> repository.findOne(new UserAccountIdentifier(authentication.getName())));
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
}
