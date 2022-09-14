/*
 * Copyright 2017-2022 the original author or authors.
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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.PrePersist;

import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.types.DomainEvent;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * A user account aggregate.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAccount extends AbstractAggregateRoot<UserAccountIdentifier> {

	private @EmbeddedId UserAccountIdentifier id;

	@Getter //
	@Setter(AccessLevel.PACKAGE) //
	@Column(nullable = false) //
	private EncryptedPassword password;

	private @Getter @Setter String firstname;
	private @Getter @Setter String lastname;
	private @Getter @Setter @Column(unique = true) String email;

	@ElementCollection(fetch = FetchType.EAGER) //
	private Set<Role> roles = new TreeSet<Role>();

	private @Getter @Setter boolean enabled;

	UserAccount(UserAccountIdentifier userAccountIdentifier, EncryptedPassword password, Role... roles) {
		this(userAccountIdentifier, password, null, null, null, List.of(roles));
	}

	UserAccount(UserAccountIdentifier userAccountIdentifier, EncryptedPassword password, Iterable<Role> roles) {
		this(userAccountIdentifier, password, null, null, null, roles);
	}

	UserAccount(UserAccountIdentifier userAccountIdentifier, EncryptedPassword password, String firstname,
			String lastname, String email, Iterable<Role> roles) {

		Assert.notNull(userAccountIdentifier, "User account identifier must not be null");
		Assert.notNull(password, "Password must not be null");
		Assert.notNull(roles, "Roles must not be null");

		this.enabled = true;
		this.id = userAccountIdentifier;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		roles.forEach(this.roles::add);
	}

	/**
	 * Get the unique identifier of this {@link UserAccount}.
	 *
	 * @return the {@link UserAccountIdentifier} of this <code>UserAccount</code>
	 */
	@Override
	public UserAccountIdentifier getId() {
		return id;
	}

	/**
	 * Returns the user's username.
	 *
	 * @return will never be {@literal null}.
	 */
	public String getUsername() {
		return id.toString();
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
	 * @return A <code>Streamable/code> with all {@link Role}s of the user
	 */
	public Streamable<Role> getRoles() {
		return Streamable.of(roles);
	}

	@PrePersist
	void onCreate() {
		registerEvent(UserAccountCreated.of(this));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("UserAccount(\"%s\")", id);
	}

	/**
	 * {@link UserAccountIdentifier} serves as an identifier type for {@link UserAccount} objects. The main reason for its
	 * existence is type safety for identifier across the Salespoint Framework. <br />
	 * {@link UserAccountIdentifier} instances serve as primary key attribute in {@link UserAccount}, but can also be used
	 * as a key for non-persistent, {@link Map}-based implementations.
	 *
	 * @author Hannes Weisbach
	 * @author Oliver Gierke
	 */
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class UserAccountIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -3010760283726584012L;

		private final String userAccountId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return userAccountId;
		}
	}

	@Value(staticConstructor = "of")
	public static class UserAccountCreated implements DomainEvent {
		UserAccount account;
	}
}
