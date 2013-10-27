package org.salespointframework.core.useraccount;

import java.util.LinkedList;
import java.util.List;

public class UserAccountBuilder {

	private UserAccountIdentifier uid;
	private String password;

	private String firstname;
    private String lastname;
	private String email;

	private List<Role> roles = new LinkedList<>();
	
	
	public UserAccount build() {
		return new UserAccount(uid, password, firstname, lastname, email, roles);
	}
	
	public UserAccountBuilder id(String uid) {
		this.id(new UserAccountIdentifier(uid));
		return this;
	}
	
	public UserAccountBuilder id(UserAccountIdentifier uid) {
		this.uid = uid;
		return this;
	}
	
	
	public UserAccountBuilder firstname(String firstname) {
		this.firstname = firstname;
		return this;
	}
	
	public UserAccountBuilder lastname(String lastname) {
		this.lastname = lastname;
		return this;
	}
	
	public UserAccountBuilder email(String email) {
		this.email = email;
		return this;
	}
	
	public UserAccountBuilder role(Role role) {
		this.roles.add(role);
		return this;
	}
	
	public UserAccountBuilder role(String role) {
		this.role(new Role(role));
		return this;
	}
	
}
