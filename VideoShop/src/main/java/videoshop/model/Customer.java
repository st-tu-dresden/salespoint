package videoshop.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.salespointframework.core.useraccount.UserAccount;



@Entity
@Table(name="WHATTHEFUCK")
public class Customer {

	@Id
	private String id;
	
	private String address;
	
	@OneToOne
	private UserAccount userAccount;
	
	@Deprecated
	protected Customer() {}
	
	public Customer(UserAccount userAccount, String address) {
		this.userAccount = userAccount;
		this.address = address;
		this.id = UUID.randomUUID().toString();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public UserAccount getUserAccount() {
		return userAccount;
	}

}
