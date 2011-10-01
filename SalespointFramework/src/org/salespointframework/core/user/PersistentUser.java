package org.salespointframework.core.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 *
 */
@Entity
public class PersistentUser implements User, Comparable<PersistentUser>
{

	@EmbeddedId
	private UserIdentifier userIdentifier;

	private String password;

	@ElementCollection
	private Set<UserCapability> capabilities = new HashSet<UserCapability>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentUser()
	{
	}
	
	/**
	 * Creates an new PersistentUser
	 * @param userIdentifier the {@link UserIdentifier} of the user
	 * @param password the password of the user
	 * @param capabilities an <code>Array</code> of {@link UserCapability}s for the user 
	 */
	public PersistentUser(UserIdentifier userIdentifier, String password, UserCapability... capabilities)
	{
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier");
		this.password = Objects.requireNonNull(password, "password");
		Objects.requireNonNull(capabilities, "capabilities");
		this.capabilities.addAll(Arrays.asList(capabilities));
	}

	@Override
	public UserIdentifier getIdentifier()
	{
		return userIdentifier;
	}
	
	@Override
	public boolean addCapability(UserCapability capability)
	{
		Objects.requireNonNull(capability, "capability");
		return capabilities.add(capability);
	}

	@Override
	public boolean removeCapability(UserCapability capability)
	{
		Objects.requireNonNull(capability, "capability");
		return capabilities.remove(capability);
	}

	@Override
	public boolean hasCapability(UserCapability capability)
	{
		Objects.requireNonNull(capability, "capability");
		return capabilities.contains(capability);
	}

	@Override
	public Iterable<UserCapability> getCapabilities()
	{
		return Iterables.from(capabilities);
	}

	@Override
	public boolean verifyPassword(String password)
	{
		Objects.requireNonNull(password, "password");
		
		if (this.password.equals(password))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean changePassword(String newPassword, String oldPassword)
	{
		Objects.requireNonNull(newPassword, "newPassword");
		Objects.requireNonNull(oldPassword, "oldPassword");
		if (verifyPassword(oldPassword))
		{
			this.password = newPassword;
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof PersistentUser)
		{
			return this.userIdentifier.equals(((PersistentUser)other).userIdentifier);
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
	public int compareTo(PersistentUser other)
	{
		return this.userIdentifier.compareTo(other.getIdentifier());
	}
}
