package org.salespointframework.core.users;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

@Entity
public class PersistentUser implements User
{

	@EmbeddedId
	private UserIdentifier userIdentifier;

	private String password;

	@ElementCollection
	Set<UserCapability> capabilities = new HashSet<UserCapability>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentUser()
	{
	}

	public PersistentUser(UserIdentifier userIdentifier, String password)
	{
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier");
		this.password = Objects.requireNonNull(password, "password");
	}

	boolean addCapability(UserCapability capability)
	{
		return capabilities.add(capability);
	}

	boolean removeCapability(UserCapability capability)
	{
		return capabilities.remove(capability);
	}

	boolean hasCapability(UserCapability capability)
	{
		return capabilities.contains(capability);
	}

	Iterable<UserCapability> getCapabilities()
	{
		return Iterables.from(capabilities);
	}

	@Override
	public boolean verifyPassword(String password)
	{
		if (this.password.equals(password))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean changePassword(String newPassword, String oldPassword)
	{
		if (verifyPassword(oldPassword))
		{
			this.password = newPassword;
			return true;
		}
		return false;
	}

	@Override
	public UserIdentifier getUserIdentifier()
	{
		return userIdentifier;
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
		if (!(other instanceof PersistentUser))
		{
			return false;
		}
		return equals((PersistentUser) other);
	}

	public final boolean equals(PersistentUser other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		return this.userIdentifier.equals(other.userIdentifier);
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
}
