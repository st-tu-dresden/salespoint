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
import java.util.Objects;
import org.salespointframework.util.Utility;

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
	@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
	private UserIdentifier userIdentifier;

	private String hashedPassword;

	@ElementCollection
	private Set<Capability> capabilities = new TreeSet<Capability>();

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
	 * @param capabilities an <code>Array</code> of {@link Capability}s for the user 
	 */
	public PersistentUser(UserIdentifier userIdentifier, String password, Capability... capabilities)
	{
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
		
		Objects.requireNonNull(password, "password must not be null");
		this.hashedPassword = Utility.hashPassword(password);
		
		Objects.requireNonNull(capabilities, "capabilities must not be null");
		this.capabilities.addAll(Arrays.asList(capabilities));
	}

	@Override
	public final UserIdentifier getIdentifier()
	{
		return userIdentifier;
	}
	
	@Override
	public boolean addCapability(Capability capability)
	{
		Objects.requireNonNull(capability, "capability must not be null");
		return capabilities.add(capability);
	}

	@Override
	public boolean removeCapability(Capability capability)
	{
		Objects.requireNonNull(capability, "capability must not be null");
		return capabilities.remove(capability);
	}

	@Override
	public boolean hasCapability(Capability capability)
	{
		Objects.requireNonNull(capability, "capability must not be null");
		return capabilities.contains(capability);
	}

	@Override
	public Iterable<Capability> getCapabilities()
	{
		return Iterables.of(capabilities);
	}

	@Override
	public boolean verifyPassword(String password)
	{
		Objects.requireNonNull(password, "password must not be null");
		
		return Utility.verifyPassword(password, this.hashedPassword);
	}

	@Override
	public void changePassword(String password)
	{
		Objects.requireNonNull(password, "password must not be null");
		this.hashedPassword = Utility.hashPassword(password);
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
