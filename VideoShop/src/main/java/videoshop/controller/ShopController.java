package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.useraccount.Role;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;

@Controller
@LoggedInUser
class ShopController {

	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;

	@Autowired
	public ShopController(UserAccountManager userAccountManager, CustomerRepository customerRepository) {
		this.userAccountManager = userAccountManager;
		this.customerRepository = customerRepository;
	}

	@RequestMapping({ "/", "/index" })
	public String index() {
		return "index";
	}

	@RequestMapping("/registerNew")
	public String registerNew(HttpSession session, @RequestParam("name") UserAccountIdentifier userAccountIdentifier,
			@RequestParam("password") String password, @RequestParam("street") String street,
			@RequestParam("city") String city) {

		UserAccount userAccount = userAccountManager.create(userAccountIdentifier, password, new Role("ROLE_CUSTOMER"));
		userAccountManager.save(userAccount);
		
		
		
		Customer customer = new Customer(userAccount, street + "\n" + city);
		customerRepository.save(customer);

		return "redirect:/";
	}


	@RequestMapping("/register")
	public String register() {
		return "register";
	}
}
