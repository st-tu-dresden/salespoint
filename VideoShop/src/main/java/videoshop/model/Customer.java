package videoshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.core.useraccount.UserAccount;

// (｡◕‿◕｡)
// Salespoint bietet nur eine UserAccount Verwaltung, für weitere Attribute sollte eine extra Klasse geschriebenw werden 
// Unser Kunde hat noch eine Adresse, das bietet der UserAccount nicht
// Um den Customer in die Datenbank zu bekommen, schreiben wir ein CustomerRepository 

@Entity
public class Customer {

	@Id
	@GeneratedValue
	private long id;

	private String address;

	// (｡◕‿◕｡)
	// Jedem Customer ist genau ein UserAccount zugeordnet, um später über den UserAccount an den Customer zu kommen, speichern wir den hier
	@OneToOne
	private UserAccount userAccount;

	@Deprecated
	protected Customer() {
	}

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

	public UserAccount getUserAccount() {
		return userAccount;
	}

}
