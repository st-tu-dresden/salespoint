package org.salespointframework.core.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.salespointframework.util.Objects;


@MappedSuperclass
public class AbstractUser implements User{
	

	@Id
	private String userId;
	private String password;
	@SuppressWarnings("unused")
	private boolean deleted=false;
	@OneToMany
	private List<UserCapability> capabilities= new ArrayList<UserCapability>();
	
	@Deprecated
	protected AbstractUser() {}

	
	public AbstractUser(String userId, String password) {
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
	
	
	public String getUserId(){
		return userId;
	}
	
//	//package visibility
//	List<UserCapability> getCapabilities(){
//		return capabilities;
//	}
//	
//	//package visibility
//	void addCapability(UserCapability uc){
//		capabilities.add(uc);
//	}
//	
//	//package visibility
//	void removeCapability(UserCapability uc){
//		capabilities.remove(uc);
//	}
//	
//	//package visibility
//	boolean hasCapability(UserCapability uc){
//		if (capabilities.contains(uc)) return true;
//		return false;
//	}

}
