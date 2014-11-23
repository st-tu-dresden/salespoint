package org.salespointframework.useraccount;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import org.salespointframework.core.AbstractEntity;
import org.springframework.util.Assert;

/**
 * Domain class for a user.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
@Entity
public class UserAccount extends AbstractEntity<UserAccountIdentifier> {

	private static final long serialVersionUID = -795038599473743418L;

	@EmbeddedId//
	@AttributeOverride(name = "id", column = @Column(name = "USERACCOUNT_ID"))//
	private UserAccountIdentifier userAccountIdentifier;

	@Column(nullable = false)//
	private Password password;

	private String firstname;
	private String lastname;
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)//
	private Set<Role> roles = new TreeSet<Role>();

	private boolean enabled;

	@Deprecated
	protected UserAccount() {

	}

	UserAccount(UserAccountIdentifier userAccountIdentifier, String password, Role... roles) {
		this(userAccountIdentifier, password, null, null, null, Arrays.asList(roles));
	}

	UserAccount(UserAccountIdentifier userAccountIdentifier, String password, String firstname, String lastname,
			String email, Collection<Role> roles) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		Assert.notNull(password, "password must not be null");
		Assert.notNull(roles, "roles must not be null");

		this.enabled = true;
		this.userAccountIdentifier = userAccountIdentifier;
		this.password = new Password(password);
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.roles.addAll(roles);
	}

	/**
	 * Get the unique identifier of this {@link UserAccount}.
	 * 
	 * @return the {@link UserAccountIdentifier} of this <code>UserAccount</code>
	 */
	public final UserAccountIdentifier getIdentifier() {
		return userAccountIdentifier;
	}

	/**
	 * Adds a {@link Role} to the {@link UserAccount}.
	 * 
	 * @param role {@code role} which the {@code user} will receive, must not be {@literal null}.
	 * @return {@literal true} if successful, {@literal false} otherwise.
	 */
	public boolean add(Role role) {
		Assert.notNull(role, "role must not be null");
		return roles.add(role);
	}

	/**
	 * Removes a {@link Role} from a {@link UserAccount}.
	 * 
	 * @param role {@code role} which will be removed from {@code user}, must not be {@literal null}.
	 * @return {@literal true} if successful, {@literal false} otherwise.
	 */
	public boolean remove(Role role) {
		Assert.notNull(role, "role must not be null");
		return roles.remove(role);
	}

	/**
	 * Checks if a {@link UserAccount} has a specific {@link Role}
	 * 
	 * @param role {@link Role} for which the {@code user} will be checked for, must not be {@literal null}.
	 * @return {@literal true} if {@code role} was granted to {@code user}
	 */
	public boolean hasRole(Role role) {
		Assert.notNull(role, "role must not be null");
		return roles.contains(role);
	}

	/**
	 * @return An <code>Iterable/code> with all {@link Role}s of the user
	 */
	public Iterable<Role> getRoles() {
		return roles;
	}

	public Password getPassword() {
		return password;
	}

	void setPassword(Password password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	void setEnabled(boolean isEnabled) {
		this.enabled = isEnabled;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
