package videoshop.controller;

import javax.validation.Valid;

import org.salespointframework.core.useraccount.Role;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;
import videoshop.model.validation.RegistrationForm;

@Controller
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

	// (｡◕‿◕｡)
	// Über @Valid können wir die Eingaben automagisch prüfen lassen, ob es Fehler gab steht im BindingResult,
	// dies muss direkt nach dem @Valid Parameter folgen.
	// Siehe außerdem videoshop.model.validation.RegistrationForm
	// Lektüre: http://docs.spring.io/spring/docs/3.2.4.RELEASE/spring-framework-reference/html/validation.html
	@RequestMapping("/registerNew")
	public String registerNew(@ModelAttribute("registrationForm") @Valid RegistrationForm registrationForm, BindingResult result) {

		if(result.hasErrors()) {
			return "register";
		}
		
		// (｡◕‿◕｡)
		// Falles alles in Ordnung ist legen wir einen UserAccount und einen passenden Customer an und speichern beides.
		UserAccountIdentifier userAccountIdentifier = new UserAccountIdentifier(registrationForm.getName());
		UserAccount userAccount = userAccountManager.create(userAccountIdentifier, registrationForm.getPassword(), new Role("ROLE_CUSTOMER"));
		userAccountManager.save(userAccount);
		
		Customer customer = new Customer(userAccount, registrationForm.getAddress());
		customerRepository.save(customer);

		return "redirect:/";
	}


	@RequestMapping("/register")
	public String register(ModelMap modelMap) {
		modelMap.addAttribute("registrationForm", new RegistrationForm());
		return "register";
	}

}
