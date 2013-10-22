package videoshop.model;

import org.salespointframework.core.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

	public Customer findByUserAccount(UserAccount userAccount);
	
}
