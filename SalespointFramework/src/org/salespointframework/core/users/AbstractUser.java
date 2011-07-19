package org.salespointframework.core.users;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class AbstractUser {
	

	@Id
	private String userId;
	private String password;
	@SuppressWarnings("unused")
	private boolean deleted=false;
	
	@Deprecated
	protected AbstractUser() {}

	
	public AbstractUser(String username, String password) {
		this.userId=username;
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

}
