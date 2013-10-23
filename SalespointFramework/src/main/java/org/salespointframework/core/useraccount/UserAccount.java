package org.salespointframework.core.useraccount;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;








import org.salespointframework.util.Iterables;


/**
 * Domain class for a user.
 *
 * @author  Oliver Gierke
 * @author Paul Henke
 */
@Entity
//@NamedQuery(name = "UserAccount.findByUsername", query = "from UserAccount u where u.username = ?")
public class UserAccount {

	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "USERACCOUNT_ID"))
	private UserAccountIdentifier userAccountIdentifier;

    @Column(nullable = false)
    private Password password;

	private String firstname;
    private String lastname;
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role> roles = new TreeSet<Role>();
	
    private boolean isEnabled;

    @Deprecated
    protected UserAccount() {
    	
    }
    
    UserAccount(UserAccountIdentifier userAccountIdentifier, String password, Role... roles) {
    	this.isEnabled = true;
    	
		this.userAccountIdentifier = Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		
		Objects.requireNonNull(password, "password must not be null");
		this.password = new Password(password);
		
		Objects.requireNonNull(roles, "roles must not be null");
		this.roles.addAll(Arrays.asList(roles));
    }
    
    UserAccount(UserAccountIdentifier userAccountIdentifier, String password, String firstname, String lastname, String email, Collection<Role> roles) {
    	this.isEnabled = true;
    	
		this.userAccountIdentifier = Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		
		Objects.requireNonNull(password, "password must not be null");
		this.password = new Password(password);
		
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		
		
		Objects.requireNonNull(roles, "roles must not be null");
		this.roles.addAll(roles);
    }

	/**
	 * Get the unique identifier of this <code>User</code>.
	 * 
	 * @return the {@link UserAccountIdentifier} of this <code>UserAccount</code>
	 */
	public final UserAccountIdentifier getIdentifier()
	{
		return userAccountIdentifier;
	}
	
	/**
	 * Adds a {@link Role} to a <code>User</code>
	 * 
	 * @param role
	 *            <code>role</code> which the <code>user</code> will
	 *            receive.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 * @throws NullPointerException if role is null
	 */
	public boolean addRole(Role role)
	{
		Objects.requireNonNull(role, "role must not be null");
		return roles.add(role);
	}

	/**
	 * Removes a {@link Role} from a <code>User</code>.
	 * 
	 * @param role
	 *            <code>role</code> which will be removed from
	 *            <code>user</code>
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 * @throws NullPointerException if role is null
	 */
	public boolean removeRole(Role role)
	{
		Objects.requireNonNull(role, "role must not be null");
		return roles.remove(role);
	}

	/**
	 * Checks if a <code>User</code> has a specific {@link Role}
	 * 
	 * @param role
	 *            {@link Role} for which the <code>user</code>
	 *            will be checked for.
	 * @return <code>true</code> if <code>role</code> was granted to
	 *         <code>user</code>
	 * @throws NullPointerException if role is null
	 */
	public boolean hasRole(Role role)
	{
		Objects.requireNonNull(role, "role must not be null");
		return roles.contains(role);
	}

	/**
	 * 
	 * @return An <code>Iterable/code> with all {@link Role}s of the user 
	 */
	public Iterable<Role> getRoles()
	{
		return Iterables.of(roles);
	}

    public Password getPassword() {

        return password;
    }


    void setPassword(String password) {

        this.password = new Password(password);
    }

    public boolean isEnabled() {

        return isEnabled;
    }

    void setEnabled(boolean isEnabled) {

        this.isEnabled = isEnabled;
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
