package videoshop.model;

import org.salespointframework.core.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;


// (｡◕‿◕｡)
// Spring biete sehr guten Support um schnell eigene "Managerklassen" wie Catalog oder UserAccountManager zu schreiben
// Pflichtlektüre: http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/repositories.html
// via http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	public Customer findByUserAccount(UserAccount userAccount);
	
}
