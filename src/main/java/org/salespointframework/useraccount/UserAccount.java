package org.salespointframework.useraccount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import org.salespointframework.core.Streamable;
import org.springframework.util.Assert;

/**
 * Domain class for a user.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */

@Entity
@NoArgsConstructor
public class UserAccount extends AbstractEntity<UserAccountIdentifier> {

	private static final long serialVersionUID = -795038599473743418L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "USERACCOUNT_ID")) //
	private UserAccountIdentifier userAccountIdentifier;

	@Getter @Setter //
	@Column(nullable = false) //
	private Password password;

	private @Getter @Setter String firstname;
	private @Getter @Setter String lastname;
	private @Getter @Setter String email;

	@ElementCollection(fetch = FetchType.EAGER) //
	private Set<Role> roles = new TreeSet<Role>();

	private @Getter @Setter boolean enabled;

	UserAccount(UserAccountIdentifier userAccountIdentifier, String password, Role... roles) {
		this(userAccountIdentifier, password, null, null, null, Arrays.asList(roles));
	}

	UserAccount(UserAccountIdentifier userAccountIdentifier, String password, String firstname, String lastname,
			String email, Collection<Role> roles) {

		Assert.notNull(userAccountIdentifier, "User account identifier must not be null");
		Assert.notNull(password, "Password must not be null");
		Assert.notNull(roles, "Roles must not be null");

		this.enabled = true;
		this.userAccountIdentifier = userAccountIdentifier;
		this.password = Password.unencrypted(password);
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
	@Override
	public UserAccountIdentifier getId() {
		return userAccountIdentifier;
	}

	/**
	 * Returns the user's username.
	 * 
	 * @return will never be {@literal null}.
	 */
	public String getUsername() {
		return userAccountIdentifier.getIdentifier();
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
	public Streamable<Role> getRoles() {
		return Streamable.of(roles);
	}
}
