package org.salespointframework.core.useraccount;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserAccountManager {
	
	public UserAccount create(UserAccountIdentifier userAccountIdentifier, String password, Role... roles);
	
	public UserAccount get(UserAccountIdentifier userAccountIdentifier);
	
	public void save(UserAccount userAccount);
	
	public void enable(UserAccountIdentifier userAccountIdentifier);
	
	public void disable(UserAccountIdentifier userAccountIdentifier);
	
	public void changePassword(UserAccount userAccount, String password);
	
	public boolean contains(UserAccountIdentifier userAccountIdentifier);
	
	public Iterable<UserAccount> findAll();
	
	public Iterable<UserAccount> findEnabled();
	
	public Iterable<UserAccount> findDisabled();
	
	public PasswordEncoder getPasswordEncoder();

}
