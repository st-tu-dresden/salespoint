package org.salespointframework.core.user;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.SalespointPasswordEncoder;

import java.util.Objects;

/**
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 *
 */
@Entity
public class User implements Comparable<User>
{

	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
	private UserIdentifier userIdentifier;

	private String encodetPassword;

	@ElementCollection
	private Set<Capability> capabilities = new TreeSet<Capability>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected User()
	{
	}
	
	/**
	 * Creates an new PersistentUser
	 * @param userIdentifier the {@link UserIdentifier} of the user
	 * @param password the password of the user
	 * @param capabilities an <code>Array</code> of {@link Capability}s for the user 
	 */
	public User(UserIdentifier userIdentifier, String password, Capability... capabilities)
	{
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
		
		Objects.requireNonNull(password, "password must not be null");
		this.encodetPassword = new SalespointPasswordEncoder().encode(password);
		
		Objects.requireNonNull(capabilities, "capabilities must not be null");
		this.capabilities.addAll(Arrays.asList(capabilities));
	}

	/**
	 * Get the unique identifier of this <code>User</code>.
	 * 
	 * @return the {@link UserIdentifier} of this <code>User</code>
	 */
	public final UserIdentifier getIdentifier()
	{
		return userIdentifier;
	}
	
	/**
	 * Adds a {@link Capability} to a <code>User</code>
	 * 
	 * @param capability
	 *            <code>capability</code> which the <code>user</code> will
	 *            receive.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 * @throws NullPointerException if capability is null
	 */
	public boolean addCapability(Capability capability)
	{
		Objects.requireNonNull(capability, "capability must not be null");
		return capabilities.add(capability);
	}

	/**
	 * Removes a {@link Capability} from a <code>User</code>.
	 * 
	 * @param capability
	 *            <code>capability</code> which will be removed from
	 *            <code>user</code>
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 * @throws NullPointerException if capability is null
	 */
	public boolean removeCapability(Capability capability)
	{
		Objects.requireNonNull(capability, "capability must not be null");
		return capabilities.remove(capability);
	}

	/**
	 * Checks if a <code>User</code> has a specific {@link Capability}
	 * 
	 * @param capability
	 *            {@link Capability} for which the <code>user</code>
	 *            will be checked for.
	 * @return <code>true</code> if <code>capability</code> was granted to
	 *         <code>user</code>
	 * @throws NullPointerException if capability is null
	 */
	public boolean hasCapability(Capability capability)
	{
		Objects.requireNonNull(capability, "capability must not be null");
		return capabilities.contains(capability);
	}

	/**
	 * 
	 * @return An <code>Iterable/code> with all {@link Capability}s of the user 
	 */
	public Iterable<Capability> getCapabilities()
	{
		return Iterables.of(capabilities);
	}

	/**
	 * Checks a given password against the user's password.
	 * 
	 * @param password
	 *            The password to be checked.
	 * @return <code>true</code>, if the password matches, <code>false</code>
	 *         otherwise.
	 * @throws NullPointerException if password is null
	 */
	public boolean verifyPassword(String password)
	{
		Objects.requireNonNull(password, "password must not be null");
		
		return new SalespointPasswordEncoder().matches(password, this.encodetPassword);
	}

	/**
	 * Changes the password of the <code>User</code> to <code>newPassword</code>
	 * 
	 * @param newPassword
	 *            new password.
	 * @throws NullPointerException if newPassword is null
	 */
	public void changePassword(String password)
	{
		Objects.requireNonNull(password, "password must not be null");
		this.encodetPassword = new SalespointPasswordEncoder().encode(password);
	}

	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof User)
		{
			return this.userIdentifier.equals(((User)other).userIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return userIdentifier.hashCode();
	}

	@Override
	public String toString()
	{
		return userIdentifier.toString();
	}

	@Override
	public int compareTo(User other)
	{
		return this.userIdentifier.compareTo(other.getIdentifier());
	}

	public String getPassword() {
		return encodetPassword;
	}
}
