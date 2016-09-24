package org.salespointframework.useraccount;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link UserAccountRepository}.
 * 
 * @author Oliver Gierke
 */
@Transactional
public class UserAccountRepositoryIntegrationTests extends AbstractIntegrationTests {

	@Autowired UserAccountRepository repository;

	UserAccount firstUser, secondUser;

	@Before
	public void setUp() {

		this.firstUser = createAccount();
		this.firstUser.setPassword(Password.encrypted("encrypted"));
		this.firstUser = repository.save(firstUser);

		this.secondUser = createAccount();
		this.secondUser.setPassword(Password.encrypted("encrypted2"));
		this.secondUser.setEnabled(false);
		this.secondUser = repository.save(secondUser);
	}

	@Ignore
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void preventsUnencryptedPasswordsFromBeingPersisted() {
		repository.save(createAccount());
	}

	@Test
	public void findsEnabledUsers() {

		Iterable<UserAccount> result = repository.findByEnabledTrue();

		assertThat(result, is(Matchers.<UserAccount>iterableWithSize(1)));
		assertThat(result, hasItem(firstUser));
	}

	@Test
	public void findsDisabledUsers() {

		Iterable<UserAccount> result = repository.findByEnabledFalse();

		assertThat(result, is(Matchers.<UserAccount>iterableWithSize(1)));
		assertThat(result, hasItem(secondUser));
	}

	/**
	 * @see #55
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void rejectsUserAccountWithSameUsername() {
		repository.save(new UserAccount(firstUser.getId(), "someotherPassword"));
	}

	static UserAccount createAccount() {

		UserAccountIdentifier identifier = new UserAccountIdentifier(UUID.randomUUID().toString());
		return new UserAccount(identifier, "password", Role.of("USER"));
	}
}
