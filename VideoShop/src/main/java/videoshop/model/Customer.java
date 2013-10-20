package videoshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.core.useraccount.UserAccount;



@Entity
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
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
