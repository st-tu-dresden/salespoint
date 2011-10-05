package dvdshop.model;

import javax.persistence.Entity;

import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;


@Entity
public class Customer extends PersistentUser {

	@SuppressWarnings("unused")
	private String adress;
	
	@Deprecated
	protected Customer() {
		
	}
	
	public Customer(UserIdentifier useridentifier, String password, String adress) {
		super(useridentifier, password);
		this.adress = adress;
	}
	
}
