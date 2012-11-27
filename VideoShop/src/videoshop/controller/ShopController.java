package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.user.Capability;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.web.WebLoginLogoutManager;
import org.salespointframework.web.annotation.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Customer;

@Controller
public class ShopController {

	@Autowired
	private PersistentUserManager userManager;
	
	@RequestMapping({"/", "/index"})
	@LoggedInUser(value=User.class, name = "user")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/registerNew")
	public String registerNew(HttpSession session,
			@RequestParam("name") UserIdentifier userIdentifier,
			@RequestParam("password") String password,
			@RequestParam("street") String street,
			@RequestParam("city") String city) {
		
		Customer customer = new Customer(userIdentifier, password, street + "\n" + city);
		userManager.add(customer);
		WebLoginLogoutManager.INSTANCE.login(customer, password, session);

		return "redirect:/";
	}
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(HttpSession session, ModelMap modelMap,
			@RequestParam("SP_LOGIN_PARAM_IDENTIFIER") UserIdentifier userIdentifier,
			@RequestParam("SP_LOGIN_PARAM_PASSWORD") String password) 
	{
 		PersistentUser user = userManager.get(PersistentUser.class, userIdentifier);
		if(user != null) {
			if(!WebLoginLogoutManager.INSTANCE.login(user, password, session)) {
				modelMap.addAttribute("error", "falsches Passwort");
			} 
		} else {
			modelMap.addAttribute("error", "unbekannter Nutzer");
		}

		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		WebLoginLogoutManager.INSTANCE.logout(session);
		return "redirect:/";
	}
	
	@RequestMapping("/register")
	public String register() {
		return "register";
	}
	
	@RequestMapping("/money")
	public String foobar(@RequestParam("xyz") Money foo ) {
		System.out.println(foo.toString());
		return "index";
	}
	
	@RequestMapping("/capability")
	public String foobar(@RequestParam("xyz") Capability foo ) {
		System.out.println(foo.toString());
		return "index";
	}

	
	@RequestMapping("/units")
	public String foobar(@RequestParam("xyz") Units foo ) {
		System.out.println(foo.toString());
		return "index";
	}

	
}
