package org.salespointframework.useraccount;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
class SpringSecurityAuthenticationManager implements AuthenticationManager {

	private final UserAccountRepository repository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Creates a new {@link SpringSecurityAuthenticationManager} using the given {@link UserAccountRepository}.
	 * 
	 * @param repository must not be {@literal null}.
	 */
	@Autowired
	public SpringSecurityAuthenticationManager(UserAccountRepository repository, PasswordEncoder passwordEncoder) {

		Assert.notNull(repository, "UserAccountRepository must not be null!");
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.AuthenticationManager#getCurrentUser()
	 */
	@Override
	public Optional<UserAccount> getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return null;
		}

		UserAccountIdentifier userAccountIdentifier = new UserAccountIdentifier(authentication.getName());
		return repository.findOne(userAccountIdentifier);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.AuthenticationManager#matches(org.salespointframework.useraccount.Password, org.salespointframework.useraccount.Password)
	 */
	@Override
	public boolean matches(Password candidate, Password existing) {

		Assert.notNull(existing);

		if (candidate == null) {
			return false;
		}

		return passwordEncoder.matches(candidate.toString(), existing.toString());
	}
}
