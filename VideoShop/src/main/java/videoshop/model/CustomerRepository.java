package videoshop.model;

import org.salespointframework.core.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

interface CustomerRepository extends CrudRepository<Customer, String> {

	public Customer findByUserAccount(UserAccount userAccount);
	
}
