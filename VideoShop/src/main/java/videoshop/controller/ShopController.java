package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.core.user.UserManager;
import org.salespointframework.web.WebAuthenticationManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Customer;

@Controller
@LoggedInUser
class ShopController {

	private final UserManager<PersistentUser> userManager;

	@Autowired
	public ShopController(UserManager<PersistentUser> userManager) {
		this.userManager = userManager;
	}

	@RequestMapping({ "/", "/index" })
	public String index() {
		return "index";
	}

	@RequestMapping("/registerNew")
	public String registerNew(HttpSession session, @RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password, @RequestParam("street") String street,
			@RequestParam("city") String city) {

		Customer customer = new Customer(userIdentifier, password, street + "\n" + city);
		userManager.add(customer);
		WebAuthenticationManager.INSTANCE.login(customer, password, session);

		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@PathVariable("identifier") PersistentUser user, @RequestParam("password") String password,
			HttpSession session) {
		WebAuthenticationManager.INSTANCE.login(user, password, session);
		return "redirect:/";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		WebAuthenticationManager.INSTANCE.logout(session);
		return "redirect:/";
	}

	@RequestMapping("/register")
	public String register() {
		return "register";
	}
}
