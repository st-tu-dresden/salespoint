package org.salespointframework.core.users;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;


@MappedSuperclass
public class AbstractUser implements User{
	

	@EmbeddedId
	private UserIdentifier userId;
	private String password;
	@SuppressWarnings("unused")
	private boolean deleted=false;
	

	@ElementCollection
	Set<UserCapability> capabilities = new HashSet<UserCapability>();
	
	@Deprecated
	protected AbstractUser() {}


	void addCapability(UserCapability capability) {
		capabilities.add(capability);
	}
	
	void removeCapability(UserCapability capability) {
		capabilities.remove(capability);
	}
	
	Iterable<UserCapability> getCapabilities() {
		return Iterables.from(capabilities);
	}

	
	public AbstractUser(UserIdentifier userId, String password) {
		Objects.requireNonNull(userId, "CreateUser_userId");
		Objects.requireNonNull(password, "CreateUser_password");
		this.userId=userId;
		this.password=password;
	}

	public boolean verifyPassword(String password) {
		if(this.password.equals(password))return true;
		return false;
	}
	
	/**
	 * Changes Password of the User to newPassword if oldPassword is correct
	 * @param newPassword the Password you want to give the User
	 * @param oldPassword the Password the User already has (will be checked)
	 */
	public boolean changePassword(String newPassword, String oldPassword) {
		if(verifyPassword(oldPassword)){
			this.password=newPassword;
			return true;
		}
		return false;
	}
	
	
	public UserIdentifier getUserIdentifier(){
		return userId;
	}
	
	public boolean equals(User user){
		if(userId.equals(user.getUserIdentifier())) return true;
		return false;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof User){
			return equals((User)o);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return userId.hashCode();
	}

}
